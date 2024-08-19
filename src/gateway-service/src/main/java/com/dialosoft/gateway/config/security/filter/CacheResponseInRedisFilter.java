package com.dialosoft.gateway.config.security.filter;

import com.dialosoft.gateway.config.error.exception.CustomTemplateException;
import com.dialosoft.gateway.config.redis.RedisCacheService;
import com.dialosoft.gateway.config.security.dto.ResponseBody;
import com.dialosoft.gateway.config.security.dto.UserCacheInfo;
import com.dialosoft.gateway.config.security.util.JwtUtils;
import com.dialosoft.gateway.config.security.util.RouterValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
@ConfigurationProperties(prefix = "app")
@Setter
public class CacheResponseInRedisFilter implements WebFilter {

    private List<String> cacheableRoutes;
    private final RedisCacheService redisCacheService;
    private final JwtUtils jwtUtils;
    private final RouterValidator routerValidator;
    private final ObjectMapper objectMapper;

    public CacheResponseInRedisFilter(RedisCacheService redisCacheService, JwtUtils jwtUtils, RouterValidator routerValidator, ObjectMapper objectMapper) {

        this.redisCacheService = redisCacheService;
        this.jwtUtils = jwtUtils;
        this.routerValidator = routerValidator;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        log.info("cacheableRoutes: {}", cacheableRoutes);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        ServerHttpResponseDecorator decoratedResponse = getDecoratedResponse(response, request);
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private boolean isCacheableRequest(ServerHttpRequest request) {
        return routerValidator.isSecured(request) && cacheableRoutes.contains(request.getURI().getPath());
    }

    private ServerHttpResponseDecorator getDecoratedResponse(ServerHttpResponse response, ServerHttpRequest request) {
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux && isCacheableRequest(request)) {
                    return super.writeWith(((Flux<? extends DataBuffer>) body).buffer().map(dataBuffers -> {
                        String responseBody = extractResponseBody(dataBuffers);
                        if (redisCacheService.isRedisAvailable()) {
                            saveResponseInRedis(request, responseBody);
                        }
                        return wrapResponse(responseBody, response.bufferFactory());
                    })).onErrorResume(err -> {
                        log.error("Error while decorating response: {}", err.getMessage());
                        return Mono.error(err);
                    });
                }
                return super.writeWith(body);
            }
        };
    }

    private String extractResponseBody(List<? extends DataBuffer> dataBuffers) {
        DefaultDataBuffer joinedBuffers = new DefaultDataBufferFactory().join(dataBuffers);
        byte[] content = new byte[joinedBuffers.readableByteCount()];
        joinedBuffers.read(content);
        return new String(content, StandardCharsets.UTF_8);
    }

    private DataBuffer wrapResponse(String responseBody, DataBufferFactory bufferFactory) {
        return bufferFactory.wrap(responseBody.getBytes(StandardCharsets.UTF_8));
    }

    private void saveResponseInRedis(ServerHttpRequest request, String responseBody) {
        try {
            String token = jwtUtils.extractToken(request);
            String userId = jwtUtils.getUserId(token);
            Long expirationTime = calculateExpirationTime(token);

            if (!redisCacheService.isKeyPresent(token, userId)) {
                ResponseBody<?> responseBodyMapped = objectMapper.readValue(responseBody, ResponseBody.class);
                UserCacheInfo userCacheInfoMapped = objectMapper.convertValue(responseBodyMapped.getData(), UserCacheInfo.class);
                redisCacheService.addInfoToCache(token, userId, userCacheInfoMapped, expirationTime);
            }
        } catch (JsonProcessingException e) {
            throw new CustomTemplateException("Error parsing response to UserCacheInfo in saveResponseInRedis()", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Long calculateExpirationTime(String token) {
        return jwtUtils.getExpirationDate(token).getTime() - System.currentTimeMillis();
    }
    
}
