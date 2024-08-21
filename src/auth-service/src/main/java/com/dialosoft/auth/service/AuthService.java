package com.dialosoft.auth.service;

import com.dialosoft.auth.persistence.entity.RefreshToken;
import com.dialosoft.auth.persistence.entity.RoleEntity;
import com.dialosoft.auth.persistence.entity.SeedPhraseEntity;
import com.dialosoft.auth.persistence.entity.UserEntity;
import com.dialosoft.auth.persistence.repository.RoleRepository;
import com.dialosoft.auth.persistence.repository.SeedPhraseRepository;
import com.dialosoft.auth.persistence.repository.UserRepository;
import com.dialosoft.auth.persistence.response.JwtResponseDTO;
import com.dialosoft.auth.persistence.response.RegisterResponse;
import com.dialosoft.auth.persistence.response.ResponseBody;
import com.dialosoft.auth.service.dto.LoginDto;
import com.dialosoft.auth.service.dto.RefreshTokenDto;
import com.dialosoft.auth.service.dto.RegisterDto;
import com.dialosoft.auth.service.utils.RoleType;
import com.dialosoft.auth.web.config.SecurityConfig;
import com.dialosoft.auth.web.config.error.exception.CustomTemplateException;
import com.dialosoft.auth.web.config.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserSecurityService userSecurityService;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;
    private final SecurityConfig securityConfig;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SeedPhraseRepository seedPhraseRepository;
    private final RecoverService recoverService;


    public ResponseEntity<ResponseBody<?>> register(RegisterDto registerDto) {

        Optional<UserEntity> userEntityOp = userRepository.findByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail());

        if (userEntityOp.isPresent()) {

            throw new CustomTemplateException("any of the parameters already exists", null, HttpStatus.CONFLICT);
        }

        UserEntity newUser;
        List<String> seedPhrase;

        try {
            RoleEntity defaultRole = roleRepository.findByRoleType(RoleType.USER)
                    .orElseThrow(() -> new RuntimeException("Default role not found"));

            UserEntity userEntity = UserEntity.builder()
                    .username(registerDto.getUsername())
                    .email(registerDto.getEmail())
                    .password(securityConfig.encoder().encode(registerDto.getPassword()))
                    .role(defaultRole)
                    .build();

            newUser = userRepository.save(userEntity);

            seedPhrase = recoverService.generateSeedPhrase(12);

            generatedAndSaveSelectedSeedPhrase(userEntity, seedPhrase);

        } catch (Exception e) {

            throw new CustomTemplateException("An error occurred while creating the user", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseBody<?> response = ResponseBody.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(String.format("User %s created sucessfully", newUser.getId()))
                .data(RegisterResponse.builder()
                        .seedPhrase(seedPhrase)
                        .build())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private void generatedAndSaveSelectedSeedPhrase(UserEntity userEntity, List<String> seedPhrase) {

        SeedPhraseEntity seedPhraseEntity = SeedPhraseEntity.builder()
                .hashPhrase(recoverService.hashSeedPhrase(seedPhrase))
                .userId(userEntity.getId())
                .build();

        seedPhraseRepository.save(seedPhraseEntity);
    }

    public ResponseEntity<ResponseBody<?>> login(LoginDto loginDto) {

        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        );

        try {
            Authentication authentication = this.authenticationManager.authenticate(login);

            if (authentication.isAuthenticated()) {

                UserEntity userEntity = userSecurityService.getUserByUserName(loginDto.getUsername());

                String accessToken = jwtUtil.generateAccessToken(userEntity.getId(), loginDto.getUsername(), authentication.getAuthorities());
                Long accessTokenExpiresInSeconds = jwtUtil.getExpirationInSeconds(accessToken);
                RefreshToken refreshToken = refreshTokenService.getOrCreateRefreshTokenByUserName(loginDto.getUsername());
                Long refreshTokenExpiresInSeconds = jwtUtil.getExpirationInSeconds(refreshToken.getRefreshToken());

                JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                        .accessToken(accessToken)
                        .accessTokenExpiresInSeconds(accessTokenExpiresInSeconds)
                        .refreshToken(refreshToken.getRefreshToken())
                        .refreshTokenExpiresInSeconds(refreshTokenExpiresInSeconds)
                        .build();

                ResponseBody<JwtResponseDTO> response = ResponseBody.<JwtResponseDTO>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Authentication successfully")
                        .data(jwtResponseDTO)
                        .build();

                return new ResponseEntity<>(response, HttpStatus.OK);

            } else {
                throw new UsernameNotFoundException("invalid user request..!!");
            }

        } catch (BadCredentialsException e) {

            throw new CustomTemplateException("Invalid credentials", e, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<ResponseBody<JwtResponseDTO>> refreshTokens(RefreshTokenDto refreshTokenDto) {

        return refreshTokenService.findByToken(refreshTokenDto.getRefreshToken())
                .map(refreshTokenService::verifyRefreshTokenExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {

                    UserDetails userDetails = userSecurityService.loadUserByUsername(userInfo.getUsername());
                    String accessToken = jwtUtil.generateAccessToken(userInfo.getId(), userInfo.getUsername(), userDetails.getAuthorities());
                    Long accessTokenExpiresInSeconds = jwtUtil.getExpirationInSeconds(accessToken);

                    JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .accessTokenExpiresInSeconds(accessTokenExpiresInSeconds)
                            .refreshToken(refreshTokenDto.getRefreshToken())
                            .build();

                    ResponseBody<JwtResponseDTO> response = ResponseBody.<JwtResponseDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Pair tokens created successfully")
                            .data(jwtResponseDTO)
                            .build();

                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseThrow(() -> new CustomTemplateException("Refresh token not found in our system, this commonly means that was expired", null, HttpStatus.UNAUTHORIZED));
    }

    public ResponseEntity<ResponseBody<?>> logout(String accessToken) {
        // Get the token user
        String username = jwtUtil.getUsername(accessToken);

        // Find the refresh token associated with the user
        RefreshToken refreshToken = refreshTokenService.getOrCreateRefreshTokenByUserName(username);

        // Save both tokens in the Redis blacklist
        tokenBlacklistService.addToBlacklist(accessToken, jwtUtil.getExpirationInSeconds(accessToken));

        // Delete the refresh token from the database
        refreshTokenService.deleteRefreshTokenByToken(refreshToken.getRefreshToken());

        ResponseBody<?> response = ResponseBody.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Logout successfully")
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
