package com.dialosoft.gateway.config.security.filter;

import com.dialosoft.gateway.config.error.exception.CustomTemplateException;
import com.dialosoft.gateway.config.redis.RedisCacheService;
import com.dialosoft.gateway.config.security.dto.UserCacheInfo;
import com.dialosoft.gateway.config.security.util.AuthUtils;
import com.dialosoft.gateway.config.security.util.JwtUtils;
import com.dialosoft.gateway.config.security.util.RouterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthUtils authUtils;
    @Autowired
    private RouterValidator routerValidator;
    @Autowired
    private RedisCacheService redisCacheService;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
        // Configuration properties if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> applyFilter(exchange, chain, config);
    }

    private Mono<Void> applyFilter(ServerWebExchange exchange, GatewayFilterChain chain, Config config) {

        var request = exchange.getRequest();

        if (routerValidator.isSecured(request)) {

            if (authUtils.isAuthorizationHeaderMissing(request)) {

                throw new CustomTemplateException("Missing Authorization header", null, HttpStatus.UNAUTHORIZED);
            }

            String token = authUtils.getAuthorizationHeader(request);

            if (token == null || !token.startsWith("Bearer ")) {
                throw new CustomTemplateException("Missing or invalid Authorization header", null, HttpStatus.UNAUTHORIZED);
            }

            token = token.substring(7);

            if (!jwtUtils.isValid(token)) {
                throw new CustomTemplateException("Invalid token", null, HttpStatus.UNAUTHORIZED);
            }

            String userId = jwtUtils.getUserId(token);

            // Check if Redis is available
            if (!redisCacheService.isRedisAvailable()) {
                // If Redis is not available, do the mutation as before
                authUtils.mutateRequestWithAuthHeaders(exchange, token);

                return chain.filter(exchange);
            }

            // Attempt to retrieve user info from Redis
            UserCacheInfo cachedUserInfo = redisCacheService.isKeyPresent(token, userId)
                    ? redisCacheService.getInfoFromCache(token, userId, UserCacheInfo.class)
                    : null;

            if (cachedUserInfo != null && cachedUserInfo.getUuid().equals(userId)) {
                authUtils.mutateRequestWithAuthHeaders(exchange, cachedUserInfo);

                return chain.filter(exchange);
            }

            // If not in Redis, proceed as before
            authUtils.mutateRequestWithAuthHeaders(exchange, token);

        }
        return chain.filter(exchange);
    }


}