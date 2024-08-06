package com.biznetbb.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

//@Service
//@RequiredArgsConstructor
//public class WebClientService {
//
//    private final DiscoveryClient discoveryClient;
//    private final WebClient.Builder webClientBuilder;
//
//    private String getGatewayBaseUrl(String serviceName) {
//        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
//        if (instances != null && !instances.isEmpty()) {
//            return instances.get(0).getUri().toString();
//        }
//        throw new IllegalStateException("No instances found for service: " + serviceName);
//    }
//
//    public WebClient getWebClientForService(String serviceName) {
//        String baseUrl = getGatewayBaseUrl(serviceName);
//        return webClientBuilder.baseUrl(baseUrl).build();
//    }
//}