package com.dialosoft.gateway.config.openapi.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "springdoc.swagger-ui")
@Data
public class SwaggerProperties {

    private List<SwaggerUrl> urls;

    @Data
    public static class SwaggerUrl {
        private String url;
        private String name;
        private String primaryName;
        private String belongServiceName;
    }
}