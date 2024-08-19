package com.dialosoft.gateway.config.security.util;

import com.dialosoft.gateway.config.security.dto.RoleDTO;
import com.dialosoft.gateway.config.security.dto.UserCacheInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

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

    public void mutateRequestWithAuthHeaders(ServerWebExchange exchange, UserCacheInfo userCacheInfo) {

        // We mutate the request with the user information obtained from Redis
        var mainRole = userCacheInfo.getRole();
        String mainRoleJson = convertRoleDTOToJson(mainRole);

        exchange.getRequest().mutate()
                .header("X-Auth-Role", mainRoleJson)
                .header("X-Auth-Username", userCacheInfo.getUsername())
                .header("X-Auth-UserId", userCacheInfo.getUuid())
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
