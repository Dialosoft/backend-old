package com.biznetbb.gateway.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {

    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
