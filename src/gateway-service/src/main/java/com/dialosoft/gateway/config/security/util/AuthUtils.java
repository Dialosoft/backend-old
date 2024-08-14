package com.dialosoft.gateway.config.security.util;

import com.dialosoft.gateway.config.security.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthUtils {

    @Autowired
    private JwtUtils jwtUtils;

    public String getAuthorizationHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    public boolean isAuthorizationHeaderMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    public void mutateRequestWithAuthHeaders(ServerWebExchange exchange, String token) {

        var roles = jwtUtils.getRoles(token).stream()
                .map(RoleDTO::getRoleName)
                .toArray(String[]::new);
        exchange.getRequest().mutate()
                .header("roles", String.join(",", roles))
                .header("X-Auth-Username", jwtUtils.getUsername(token))
                .header("X-Auth-UserId", jwtUtils.getUserId(token))
                .build();
    }

    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        var response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, String errorMessage) {
        var response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("error", errorMessage);
        return response.setComplete();
    }
}
