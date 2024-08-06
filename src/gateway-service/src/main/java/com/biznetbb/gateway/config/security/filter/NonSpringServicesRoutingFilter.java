package com.biznetbb.gateway.config.security.filter;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
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
            // TODO: add dynamic service name from external properties
            String serviceName = "user-microservice";

            String baseUrl = getGatewayBaseUrl(serviceName);
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

            // Create a dynamic request based on the original HTTP method
            Mono<Void> responseMono = webClient
                    .method(request.getMethod())
                    .uri(request.getURI().getPath())
                    .headers(httpHeaders -> httpHeaders.addAll(request.getHeaders()))
                    .body(BodyInserters.fromDataBuffers(request.getBody()))
                    .exchangeToMono(clientResponse -> {
                        exchange.getResponse().setStatusCode(clientResponse.statusCode());
                        exchange.getResponse().getHeaders().addAll(clientResponse.headers().asHttpHeaders());

                        return clientResponse.bodyToMono(byte[].class)
                                .defaultIfEmpty(new byte[0])
                                .flatMap(body -> exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                                        .bufferFactory().wrap(body))));
                    });

            return responseMono.then(chain.filter(exchange));
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