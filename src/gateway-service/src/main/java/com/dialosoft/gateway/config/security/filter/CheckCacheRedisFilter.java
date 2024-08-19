package com.dialosoft.gateway.config.security.filter;

import com.dialosoft.gateway.config.error.exception.CustomTemplateException;
import com.dialosoft.gateway.config.redis.RedisCacheService;
import com.dialosoft.gateway.config.security.dto.ResponseBody;
import com.dialosoft.gateway.config.security.dto.UserCacheInfo;
import com.dialosoft.gateway.config.security.util.JwtUtils;
import com.dialosoft.gateway.config.security.util.RouterValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
@ConfigurationProperties(prefix = "app")
@Setter
public class CheckCacheRedisFilter extends AbstractGatewayFilterFactory<CheckCacheRedisFilter.Config> {

    private List<String> cacheableRoutes;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RouterValidator routerValidator;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private ObjectMapper objectMapper;

    public CheckCacheRedisFilter() {
        super(CheckCacheRedisFilter.Config.class);
    }

    public static class Config {
        // Configuration properties if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (!redisCacheService.isRedisAvailable()) {
                // Si Redis no está disponible, devolver la respuesta normal
                return chain.filter(exchange);
            }

            if (isCacheableRequest(request)) {
                // Si la respuesta está en caché, devolverla directamente
                return getCachedResponse(request, response)
                        .flatMap(cachedResponse -> chain.filter(exchange.mutate().response(cachedResponse).build()))
                        .switchIfEmpty(chain.filter(exchange)); // Si no está en caché, continuar con la solicitud normal
            }

            return chain.filter(exchange); // Continuar con la solicitud si no es cacheable
        };
    }

    private boolean isCacheableRequest(ServerHttpRequest request) {
        return routerValidator.isSecured(request) && cacheableRoutes.contains(request.getURI().getPath());
    }

    private Mono<ServerHttpResponse> getCachedResponse(ServerHttpRequest request, ServerHttpResponse response) {
        try {
            String token = jwtUtils.extractToken(request);
            String userId = jwtUtils.getUserId(token);

            // Intentar recuperar la respuesta en caché
            UserCacheInfo cachedUserInfo = redisCacheService.getInfoFromCache(token, userId, UserCacheInfo.class);
            if (cachedUserInfo != null && cachedUserInfo.getUuid().equals(userId)) {
                return Mono.just(createJsonResponse(response, HttpStatus.OK, "Cached UserInfo successfully retrieved", cachedUserInfo));
            }
        } catch (Exception e) {
            log.error("Error retrieving cached response: {}", e.getMessage());
            throw new CustomTemplateException("Error retrieving cached response", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return Mono.empty(); // Devolver vacío si no hay caché
    }


    private ServerHttpResponse createJsonResponse(ServerHttpResponse response, HttpStatus status, String message, UserCacheInfo data) {
        try {
            ResponseBody<UserCacheInfo> responseBody = ResponseBody.<UserCacheInfo>builder()
                    .statusCode(status.value())
                    .message(message)
                    .data(data)
                    .build();

            String jsonResponse = objectMapper.writeValueAsString(responseBody);
            DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));

            // Configura el tipo de contenido y el cuerpo de la respuesta
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.setStatusCode(status);

            return new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    return writeResponse(this, buffer);
                }
            };
        } catch (JsonProcessingException e) {
            log.error("Error creating JSON response: {}", e.getMessage());
            throw new CustomTemplateException("Error creating JSON response", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, DataBuffer buffer) {
        return response.writeWith(Mono.just(buffer));
    }

}