package com.dialosoft.gateway.config.security.util;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
@ConfigurationProperties(prefix = "app")
@Setter
@Slf4j
public class RouterValidator {

    private List<String> openApiEndpoints;

    @PostConstruct
    public void init() {
        log.info("OpenApiEndpoints: {}", openApiEndpoints);
    }

    public boolean isSecured(ServerHttpRequest request) {

        Predicate<ServerHttpRequest> isSecured = requestParam ->
                openApiEndpoints.stream()
                        .filter(uri -> !uri.equalsIgnoreCase(""))
                        .noneMatch(uri -> requestParam.getURI().getPath().contains(uri));

        return isSecured.test(request);
    }
}

