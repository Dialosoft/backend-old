package com.biznetbb.auth.service;

import com.biznetbb.auth.persistence.entity.RefreshToken;
import com.biznetbb.auth.persistence.entity.UserEntity;
import com.biznetbb.auth.persistence.repository.RefreshTokenRepository;
import com.biznetbb.auth.web.config.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserSecurityService userSecurityService;
    @Autowired
    private JwtUtil jwtUtil;

    public RefreshToken getOrCreateRefreshTokenByUserName(String username) {

        UserEntity userEntity = userSecurityService.getUserByUserName(username);

        return refreshTokenRepository.findByUserId(userEntity.getId())
                .filter(this::isRefreshTokenValid)
                .orElseGet(() -> createRefreshToken(userEntity));
    }

    private RefreshToken createRefreshToken(UserEntity userEntity) {

        UserDetails userDetails = userSecurityService.loadUserByUsername(userEntity.getUsername());
        String stringRefreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername(), userDetails.getAuthorities());

        RefreshToken refreshToken = RefreshToken.builder()
                .user(userEntity)
                .refreshToken(stringRefreshToken)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public Optional<RefreshToken> findByUserId(UUID userId){
        return refreshTokenRepository.findByUserId(userId);
    }

    public RefreshToken verifyRefreshTokenExpiration(RefreshToken refreshToken) {
        if (!isRefreshTokenValid(refreshToken)) {
            throw new RuntimeException(refreshToken.getRefreshToken() + " Refresh token is expired. Please login again.");
        }
        return refreshToken;
    }

    private boolean isRefreshTokenValid(RefreshToken refreshToken) {
        if (!jwtUtil.isValid(refreshToken.getRefreshToken())) {
            refreshTokenRepository.delete(refreshToken);
            return false;
        }
        return true;
    }

}
