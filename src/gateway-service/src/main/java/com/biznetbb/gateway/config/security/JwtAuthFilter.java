package com.biznetbb.gateway.config.security;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public JwtAuthFilter() {
        super(Config.class);
    }

    public static class Config {
        // config properties if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return this.onError(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
            
            if (!token.startsWith("Bearer ")){
                return this.onError(exchange, "Invalid authorization", HttpStatus.UNAUTHORIZED);
            }

            token = token.substring(7);

            return webClientBuilder.build()
                .post()
                .uri("null") // validator address here
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class)
                .flatMap(response -> chain.filter(exchange))
                .onErrorResume(e -> this.onError(exchange, "invalid token", HttpStatus.UNAUTHORIZED));
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

}
