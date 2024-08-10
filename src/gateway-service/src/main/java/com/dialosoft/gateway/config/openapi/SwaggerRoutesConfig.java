package com.dialosoft.gateway.config.openapi;

import com.dialosoft.gateway.config.openapi.util.SwaggerProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@Data
public class SwaggerRoutesConfig {

    @Value("${spring.application.name}")
    private String gatewayMicroserviceName;
    @Autowired
    private SwaggerProperties swaggerProperties;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();

        swaggerProperties.getUrls().forEach(swaggerUrl -> {

            String belongServiceName = swaggerUrl.getBelongServiceName();
            if (!belongServiceName.equalsIgnoreCase(gatewayMicroserviceName)) {

            String serviceUriFromDiscovery = getServiceUriFromDiscovery(belongServiceName);
            routes.route(r -> r.path(swaggerUrl.getUrl())
                    .and()
                    .method(HttpMethod.GET)
                    .uri(serviceUriFromDiscovery));
            }
        });

        return routes.build();
    }

    private String getServiceUriFromDiscovery(String serviceId) {

        return discoveryClient.getInstances(serviceId).stream()
                .findFirst()
                .map(serviceInstance -> "lb://" + serviceInstance.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found to generate swagger: " + serviceId));
    }
}