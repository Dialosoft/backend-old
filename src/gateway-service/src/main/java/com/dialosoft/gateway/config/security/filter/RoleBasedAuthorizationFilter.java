package com.dialosoft.gateway.config.security.filter;

import com.dialosoft.gateway.config.security.util.JwtUtils;
import com.dialosoft.gateway.config.security.dto.RoleDTO;
import com.dialosoft.gateway.config.security.util.AuthUtils;
import com.dialosoft.gateway.config.security.util.RoleType;
import com.dialosoft.gateway.config.security.util.RouterValidator;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RoleBasedAuthorizationFilter extends AbstractGatewayFilterFactory<RoleBasedAuthorizationFilter.Config> {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthUtils authUtils;
    @Autowired
    private RouterValidator routerValidator;

    public RoleBasedAuthorizationFilter() {
        super(Config.class);
    }

    @Data
    public static class Config {
        private List<String> requiredRoles = List.of(RoleType.USER.getRoleName());
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();

            if (routerValidator.isSecured(request)) {

                if (authUtils.isAuthorizationHeaderMissing(request)) {
                    return authUtils.onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                String token = authUtils.getAuthorizationHeader(request);

                if (token == null || !token.startsWith("Bearer ")) {
                    return authUtils.onError(exchange, HttpStatus.FORBIDDEN, "Missing or invalid Authorization header");
                }

                token = token.substring(7);

                if (!jwtUtils.isValid(token)) {
                    return authUtils.onError(exchange, HttpStatus.FORBIDDEN);
                }

                return checkRequiredRole(exchange, chain, config, token);

            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> checkRequiredRole(ServerWebExchange exchange, GatewayFilterChain chain, Config config, String token) {

        try {
            boolean hasRole = hasRequiredRole(token, config);

            if (!hasRole) {
                return authUtils.onError(exchange, HttpStatus.FORBIDDEN, "You do not have the necessary permissions to access this resource");
            }
        } catch (Exception e) {
            return authUtils.onError(exchange, HttpStatus.FORBIDDEN, "Error processing roles");
        }

        return chain.filter(exchange);
    }

    private boolean hasRequiredRole(String token, Config config) {

        List<String> userRoles = jwtUtils.getRoles(token).stream()
                .map(RoleDTO::getRoleName)
                .toList();

        return userRoles.stream().anyMatch(config.requiredRoles::contains);
    }
}
