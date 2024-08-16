package com.dialosoft.gateway.config.security.filter;

import com.dialosoft.gateway.config.security.util.JwtUtils;
import com.dialosoft.gateway.config.security.util.AuthUtils;
import com.dialosoft.gateway.config.security.util.RouterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthUtils authUtils;
    @Autowired
    private RouterValidator routerValidator;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
        // Configuration properties if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();

            if (routerValidator.isSecured(request)) {

                if (authUtils.isAuthorizationHeaderMissing(request)) {
                    return authUtils.onError(exchange, HttpStatus.UNAUTHORIZED, "Missing Authorization header");
                }

                String token = authUtils.getAuthorizationHeader(request);

                if (token == null || !token.startsWith("Bearer ")) {
                    return authUtils.onError(exchange, HttpStatus.FORBIDDEN, "Missing or invalid Authorization header");
                }

                token = token.substring(7);

                if (!jwtUtils.isValid(token)) {
                    return authUtils.onError(exchange, HttpStatus.FORBIDDEN);
                }

                authUtils.mutateRequestWithAuthHeaders(exchange, token);
            }
            return chain.filter(exchange);
        };
    }


}