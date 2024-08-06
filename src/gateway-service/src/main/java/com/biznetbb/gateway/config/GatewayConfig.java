package com.biznetbb.gateway.config;

import com.biznetbb.gateway.config.security.filter.NonSpringServicesRoutingFilter;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public NonSpringServicesRoutingFilter customRoutingFilter(DiscoveryClient discoveryClient, WebClient.Builder webClientBuilder) {
        return new NonSpringServicesRoutingFilter(discoveryClient, webClientBuilder);
    }
}