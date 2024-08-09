package com.dialosoft.auth.web.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
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

    private Algorithm algorithmWithSecret;

    @PostConstruct
    public void init() {
        this.algorithmWithSecret = Algorithm.HMAC256(secretKey);
    }

    public String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return createToken(username,
                authorities.stream().map(GrantedAuthority::getAuthority).toList(),
                TimeUnit.MINUTES.toMillis(5));
    }

    public String generateRefreshToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return createToken(username,
                authorities.stream().map(GrantedAuthority::getAuthority).toList(),
                TimeUnit.MINUTES.toMillis(5));
    }

    public String createToken(String username, List<String> roles, long expiredTime) {
        // helps to identify token together with username
        String uuid = UUID.randomUUID().toString();

        return JWT.create()
                .withJWTId(uuid)
                .withSubject(username)
                .withArrayClaim("role", roles.toArray(new String[0]))
                .withIssuer(String.format("%s-service", issuer))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiredTime))
                .sign(algorithmWithSecret);
    }

    public boolean isValid(String jwt) {
        try {
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
