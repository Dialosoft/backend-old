package com.biznetbb.gateway.config.security.filter;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NonSpringServicesRoutingFilter extends AbstractGatewayFilterFactory<NonSpringServicesRoutingFilter.Config> {

    private final DiscoveryClient discoveryClient;
    private final WebClient.Builder webClientBuilder;

    public NonSpringServicesRoutingFilter(DiscoveryClient discoveryClient, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.discoveryClient = discoveryClient;
        this.webClientBuilder = webClientBuilder;
    }

    public static class Config {
        // Config properties if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String serviceName = "user-microservice"; // or extract from request if dynamic

            String baseUrl = getGatewayBaseUrl(serviceName);
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

            return webClient.get()
                    .uri(request.getURI().getPath())
                    .exchangeToMono(clientResponse -> {
                        exchange.getResponse().setStatusCode(clientResponse.statusCode());
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(body -> {
                                    exchange.getResponse().getHeaders().addAll(clientResponse.headers().asHttpHeaders());
                                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                                            .bufferFactory().wrap(body.getBytes())));
                                });
                    });
        };
    }

    private String getGatewayBaseUrl(String serviceName) {
        return discoveryClient.getInstances(serviceName)
                .stream()
                .findFirst()
                .map(instance -> "http://" + ((EurekaServiceInstance) instance).getInstanceInfo().getIPAddr() + ":" + instance.getPort())
                .orElseThrow(() -> new IllegalStateException("No instances found for service: " + serviceName));
    }
}