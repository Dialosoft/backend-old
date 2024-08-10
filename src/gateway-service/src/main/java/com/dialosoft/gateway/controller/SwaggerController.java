package com.dialosoft.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

//@RestController
//@RequiredArgsConstructor
//public class SwaggerController {
//
//    private static final String API_URI = "/v3/api-docs";
//    private final DiscoveryClient discoveryClient;
//
//    @GetMapping("/v3/api-docs/swagger-config")
//    public Map<String, Object> swaggerConfig(ServerHttpRequest serverHttpRequest) throws URISyntaxException {
//
//        URI uri = serverHttpRequest.getURI();
//        String url = new URI(uri.getScheme(), uri.getAuthority(), null, null, null).toString();
//        Map<String, Object> swaggerConfig = new LinkedHashMap<>();
//
//        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrls = new LinkedHashSet<>();
//        System.out.println("Services: " + discoveryClient.getServices());
//
//        discoveryClient.getServices()
//                .forEach(serviceName ->
//                {
//                    String urlOpenApiTemplate = String.format("%1s/%2s/%3s", url, serviceName, API_URI);
//                    swaggerUrls.add(
//                            new AbstractSwaggerUiConfigProperties.SwaggerUrl(serviceName, urlOpenApiTemplate, serviceName));
//                });
//
//        swaggerConfig.put("urls", swaggerUrls);
//        return swaggerConfig;
//
//
//    }
//}
