package com.dialosoft.gateway.config.security.filter;

import lombok.Data;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Component
public class NonSpringServicesRoutingFilter extends AbstractGatewayFilterFactory<NonSpringServicesRoutingFilter.Config> {

    private final DiscoveryClient discoveryClient;
    private final WebClient.Builder webClientBuilder;
    private final Environment environment;

    public NonSpringServicesRoutingFilter(DiscoveryClient discoveryClient, WebClient.Builder webClientBuilder, Environment environment) {
        super(Config.class);
        this.discoveryClient = discoveryClient;
        this.webClientBuilder = webClientBuilder;
        this.environment = environment;
    }

    @Data
    public static class Config {
        private String belongServiceName;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (environment.acceptsProfiles(Profiles.of("local"))) {
                return chain.filter(exchange);
            }

            ServerHttpRequest request = exchange.getRequest();

            String baseUrl = getGatewayBaseUrl(config.getBelongServiceName());
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

            boolean isMultipart = MediaType.MULTIPART_FORM_DATA.isCompatibleWith(
                    exchange.getRequest().getHeaders().getContentType());

//            if (isMultipart) {
//                return applyToRequestMultiPart(exchange, webClient, request);
//            }

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

            return responseMono.then(chain.filter(exchange));
        };
    }

    private static Mono<Void> applyToRequestMultiPart(ServerWebExchange exchange, WebClient webClient, ServerHttpRequest request) {
        return exchange.getMultipartData()
                .flatMap(multipartData -> {
                    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

                    return Flux.fromIterable(multipartData.entrySet())
                            .flatMap(entry -> {
                                String key = entry.getKey();
                                FormFieldPart part = (FormFieldPart) entry.getValue();

                                if (part instanceof FilePart) {
                                    FilePart filePart = (FilePart) part;
                                    return filePart.content()
                                            .reduce(DataBuffer::write)
                                            .map(buffer -> {
                                                byte[] bytes = new byte[buffer.readableByteCount()];
                                                buffer.read(bytes);
                                                DataBufferUtils.release(buffer);
                                                return new ByteArrayResource(bytes) {
                                                    @Override
                                                    public String getFilename() {
                                                        return filePart.filename();
                                                    }
                                                };
                                            })
                                            .doOnNext(resource -> parts.add(key, resource));
                                } else {
                                    parts.add(key, part.value());
                                    return Mono.empty();
                                }
                            })
                            .then(Mono.defer(() -> {
                                return webClient
                                        .method(request.getMethod())
                                        .uri(request.getURI().getPath())
                                        .headers(httpHeaders -> httpHeaders.addAll(request.getHeaders()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .body(BodyInserters.fromMultipartData(parts))
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
                            }));
                });
    }

    private String getGatewayBaseUrl(String serviceName) {
        return discoveryClient.getInstances(serviceName)
                .stream()
                .findFirst()
                .map(instance -> "http://" + ((EurekaServiceInstance) instance).getInstanceInfo().getIPAddr() + ":" + instance.getPort())
                .orElseThrow(() -> new IllegalStateException("No instances found for service: " + serviceName));
    }

}