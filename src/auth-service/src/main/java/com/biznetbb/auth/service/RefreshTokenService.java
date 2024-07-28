package com.biznetbb.auth.service;

import com.biznetbb.auth.persistence.entity.RefreshToken;
import com.biznetbb.auth.persistence.entity.UserEntity;
import com.biznetbb.auth.persistence.repository.RefreshTokenRepository;
import com.biznetbb.auth.web.config.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserSecurityService userSecurityService;
    @Autowired
    private JwtUtil jwtUtil;

    public RefreshToken createRefreshToken(String username) {

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);
        String stringRefreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername(), userDetails.getAuthorities());

        UserEntity userEntity = userSecurityService.getUserByUserName(username);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(userEntity)
                .refreshToken(stringRefreshToken)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public RefreshToken verifyRefreshTokenExpiration(RefreshToken refreshTokenEntity) {
        if (!jwtUtil.isValid(refreshTokenEntity.getRefreshToken())) {
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new RuntimeException(refreshTokenEntity.getRefreshToken() + " Refresh refreshTokenEntity is expired. Please make a new login..!");
        }
        return refreshTokenEntity;
    }

}
