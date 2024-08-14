package com.dialosoft.gateway.config.openapi;

import com.dialosoft.gateway.config.openapi.util.SwaggerProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.Objects;

@Configuration
@Data
public class SwaggerRoutesConfig {

    @Value("${spring.application.name}")
    private String gatewayServiceName;
    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();

        swaggerProperties.getUrls().stream()
                .filter(swaggerUrlConfig -> Objects.nonNull(swaggerUrlConfig.getBelongServiceName()))
                .filter(swaggerUrlConfig -> !swaggerUrlConfig.getBelongServiceName().equals(gatewayServiceName))
                .forEach(swaggerUrlConfig -> {
                    String pathOpenApiService = swaggerUrlConfig.getUrl();
                    String baseUriService = "lb://" + swaggerUrlConfig.getBelongServiceName();
                    routes.route(r -> r.path(pathOpenApiService)
                            .and()
                            .method(HttpMethod.GET)
                            .uri(baseUriService));
                });


        return routes.build();
    }

}