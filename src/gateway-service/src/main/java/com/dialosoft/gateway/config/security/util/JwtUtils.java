package com.dialosoft.gateway.config.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dialosoft.gateway.config.redis.TokenBlacklistService;
import com.dialosoft.gateway.config.security.dto.RoleDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.application.name}")
    private String issuer;

    private Algorithm algorithmWithSecret;
    private final TokenBlacklistService tokenBlacklistService;

    @PostConstruct
    public void init() {
        this.algorithmWithSecret = Algorithm.HMAC256(secretKey);
    }

    public boolean isValid(String jwt) {
        try {

            if (tokenBlacklistService.isRedisAvailable()) {
                // Check if the token is blacklisted first
                if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                    return false;
                }
            }

            JWTVerifier verifier = JWT.require(algorithmWithSecret).build();

            verifier.verify(jwt);

            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsername(String jwt) {
        return JWT.require(algorithmWithSecret)
                .build()
                .verify(jwt).getSubject();
    }

    public String getUserId(String jwt) {
        return JWT.require(algorithmWithSecret)
                .build()
                .verify(jwt)
                .getClaim("userId")
                .asString();
    }

    public String getRoleAsString(String jwt) {

        return JWT.require(algorithmWithSecret)
                .build()
                .verify(jwt)
                .getClaim("role")
                .asString();
    }

    public RoleDTO getMainRole(String jwt) {

        String mainRoleName = getRoleAsString(jwt);

        RoleType roleType = RoleType.getRoleType(mainRoleName);
        boolean isAdmin = roleType == RoleType.ADMIN;
        boolean isMod = roleType == RoleType.MOD;

        return RoleDTO.builder()
                .roleName(roleType.getRoleName())
                .adminRole(isAdmin)
                .modRole(isMod)
                .build();
    }

    public Date getExpirationDate(String jwt) {
        return JWT.require(algorithmWithSecret)
                .build()
                .verify(jwt)
                .getExpiresAt();
    }

    public boolean isTokenExpired(String jwt) {
        Date expiration = getExpirationDate(jwt);
        return expiration.before(new Date());
    }

    public DecodedJWT decodeTokenWithoutVerification(String jwt) {
        return JWT.decode(jwt);
    }

    public String extractToken(ServerHttpRequest request) {
        return Objects.requireNonNull(request.getHeaders().getFirst("Authorization")).substring(7);
    }

}
