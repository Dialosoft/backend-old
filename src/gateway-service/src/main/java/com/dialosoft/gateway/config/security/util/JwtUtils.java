package com.dialosoft.gateway.config.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dialosoft.gateway.config.security.dto.RoleDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.application.name}")
    private String issuer;

    private Algorithm algorithmWithSecret;

    @PostConstruct
    public void init() {
        this.algorithmWithSecret = Algorithm.HMAC256(secretKey);
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

    public List<String> getRolesAsString(String jwt) {
        return Arrays.asList(JWT.require(algorithmWithSecret)
                .build()
                .verify(jwt)
                .getClaim("role")
                .asArray(String.class));
    }

    public List<RoleDTO> getRoles(String jwt) {
        List<String> roleNames = Arrays.asList(JWT.require(algorithmWithSecret)
                .build()
                .verify(jwt)
                .getClaim("role")
                .asArray(String.class));

        return roleNames.stream().map(roleName -> {
            RoleType roleType = RoleType.getRoleType(roleName);
            boolean isAdmin = roleType == RoleType.ADMIN;
            boolean isMod = roleType == RoleType.MOD;

            return RoleDTO.builder()
                    .roleName(roleType.getRoleName())
                    .adminRole(isAdmin)
                    .modRole(isMod)
                    .build();
        }).toList();
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

}
