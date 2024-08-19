package com.dialosoft.auth.web.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dialosoft.auth.service.TokenBlacklistService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${app.security.jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${app.security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private Algorithm algorithmWithSecret;
    private final TokenBlacklistService tokenBlacklistService;

    @PostConstruct
    public void init() {
        this.algorithmWithSecret = Algorithm.HMAC256(secretKey);
    }

    public String generateAccessToken(UUID userId, String username, Collection<? extends GrantedAuthority> authorities) {
        return createToken(userId, username,
                authorities.stream().map(GrantedAuthority::getAuthority).toList(),
                TimeUnit.MINUTES.toMillis(accessTokenExpiration));
    }

    public String generateRefreshToken(UUID userId, String username, Collection<? extends GrantedAuthority> authorities) {
        return createToken(userId, username,
                authorities.stream().map(GrantedAuthority::getAuthority).toList(),
                TimeUnit.MINUTES.toMillis(refreshTokenExpiration));
    }

    public String createToken(UUID userId, String username, List<String> roles, long expiredTime) {
        // helps to identify token together with username
        String uuid = UUID.randomUUID().toString();
        String mainRole = roles.get(0);

        return JWT.create()
                .withJWTId(uuid)
                .withSubject(username)
                .withClaim("role", mainRole)
                .withClaim("userId", userId.toString())
                .withIssuer(String.format("%s-service", issuer))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiredTime))
                .sign(algorithmWithSecret);
    }

    public Long getExpirationInSeconds(String jwt) {
        try {
            Date expiresAt = JWT.require(algorithmWithSecret)
                    .build()
                    .verify(jwt)
                    .getExpiresAt();

            long currentTimeMillis = System.currentTimeMillis();
            long expiresInMillis = expiresAt.getTime() - currentTimeMillis;

            return TimeUnit.MILLISECONDS.toSeconds(expiresInMillis);
        } catch (JWTVerificationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid(String jwt) {
        try {

            if (tokenBlacklistService.isRedisAvailable()) {
                // Check if the token is blacklisted first
                if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                    return false;
                }
            }

            JWTVerifier verifier = JWT.require(algorithmWithSecret)
                    .withIssuer(String.format("%s-service", issuer))
                    .build();

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
}
