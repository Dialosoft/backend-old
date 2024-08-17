package com.dialosoft.gateway.config.security.util;

import com.dialosoft.gateway.config.security.dto.RoleDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthUtils {

    @Autowired
    private JwtUtils jwtUtils;

    public String getAuthorizationHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    public boolean isAuthorizationHeaderMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    public void mutateRequestWithAuthHeaders(ServerWebExchange exchange, String token) {

        var mainRole = jwtUtils.getMainRole(token);
        String mainRoleJson = convertRoleDTOToJson(mainRole);

        exchange.getRequest().mutate()
                .header("X-Auth-Role", mainRoleJson)
                .header("X-Auth-Username", jwtUtils.getUsername(token))
                .header("X-Auth-UserId", jwtUtils.getUserId(token))
                .build();
    }

    private String convertRoleDTOToJson(RoleDTO roleDTO) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(roleDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing RoleDTO to JSON", e);
        }
    }

}
