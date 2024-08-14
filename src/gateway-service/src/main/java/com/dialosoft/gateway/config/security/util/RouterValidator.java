package com.dialosoft.gateway.config.security.util;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
@ConfigurationProperties(prefix = "app")
@Setter
public class RouterValidator {

    private List<String> openApiEndpoints;

    @PostConstruct
    public void init() {
        System.out.println("OpenApiEndpoints: " + openApiEndpoints);
    }

    public boolean isSecured(ServerHttpRequest request) {

        Predicate<ServerHttpRequest> isSecured = requestParam ->
                openApiEndpoints.stream()
                        .noneMatch(uri -> requestParam.getURI().getPath().contains(uri));

        return isSecured.test(request);
    }
}

