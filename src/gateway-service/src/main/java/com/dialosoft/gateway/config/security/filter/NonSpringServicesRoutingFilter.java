package com.dialosoft.gateway.config.security.filter;

import lombok.Data;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.core.io.buffer.DataBuffer;
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

    @Data
    public static class Config {
        private String belongServiceName;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String baseUrl = getGatewayBaseUrl(config.getBelongServiceName());
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
                                .flatMap(body -> {
                                    DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(body);
                                    return exchange.getResponse().writeWith(Mono.just(dataBuffer));
                                });
                    });

            return responseMono.then(Mono.defer(() -> {
                // Ensure the chain is continued only if the response has not been committed
                if (!exchange.getResponse().isCommitted()) {
                    return chain.filter(exchange);
                }
                return Mono.empty(); // If committed, terminate the chain
            }));
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