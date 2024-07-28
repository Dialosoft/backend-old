package com.biznetbb.auth.service;

import com.biznetbb.auth.persistence.entity.RefreshToken;
import com.biznetbb.auth.persistence.entity.RoleEntity;
import com.biznetbb.auth.persistence.entity.UserEntity;
import com.biznetbb.auth.persistence.repository.RoleRepository;
import com.biznetbb.auth.persistence.repository.UserRepository;
import com.biznetbb.auth.persistence.response.JwtResponseDTO;
import com.biznetbb.auth.persistence.response.ResponseBody;
import com.biznetbb.auth.service.dto.LoginDto;
import com.biznetbb.auth.service.dto.RefreshTokenDto;
import com.biznetbb.auth.service.dto.RegisterDto;
import com.biznetbb.auth.service.utils.RoleType;
import com.biznetbb.auth.web.config.SecurityConfig;
import com.biznetbb.auth.web.config.jwt.JwtUtil;
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

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserSecurityService userSecurityService;
    private final RefreshTokenService refreshTokenService;
    private final SecurityConfig securityConfig;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ResponseEntity<ResponseBody<?>> register(RegisterDto registerDto) {

        Optional<UserEntity> userEntityOp = userRepository.findByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail());

        if (userEntityOp.isPresent()) {

            ResponseBody<?> response = ResponseBody.builder()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .message("any of the parameters already exists")
                    .metadata(null)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        UserEntity newUser;

        try {
            RoleEntity defaultRole = roleRepository.findByRoleType(RoleType.USER)
                    .orElseThrow(() -> new RuntimeException("Default role not found"));

            UserEntity userEntity = UserEntity.builder()
                    .username(registerDto.getUsername())
                    .email(registerDto.getEmail())
                    .password(securityConfig.encoder().encode(registerDto.getPassword()))
                    .roles(Collections.singleton(defaultRole))
                    .build();

            newUser = userRepository.save(userEntity);
        } catch (Exception e) {
            ResponseBody response = ResponseBody.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An error occurred while creating the user")
                    .metadata(null)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        ResponseBody<?> response = ResponseBody.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(String.format("User %s created sucessfully", newUser.getId()))
                .metadata(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseBody<?>> login(LoginDto loginDto) {

        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        );

        try {
            Authentication authentication = this.authenticationManager.authenticate(login);

            if(authentication.isAuthenticated()) {

                String accessToken = jwtUtil.generateAccessToken(loginDto.getUsername(), authentication.getAuthorities());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginDto.getUsername());

                JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken.getRefreshToken())
                        .build();

                ResponseBody<JwtResponseDTO> response = ResponseBody.<JwtResponseDTO>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Authentication successfully")
                        .metadata(jwtResponseDTO)
                        .build();

                return new ResponseEntity<>(response, HttpStatus.OK);

            } else {
                throw new UsernameNotFoundException("invalid user request..!!");
            }

        } catch (BadCredentialsException e) {

            ResponseBody<?> response = ResponseBody.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthorized")
                    .metadata(null)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<ResponseBody<JwtResponseDTO>> refreshTokens(RefreshTokenDto refreshTokenDto) {

        return refreshTokenService.findByToken(refreshTokenDto.getRefreshToken())
                .map(refreshTokenService::verifyRefreshTokenExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {

                    String username = userInfo.getUsername();
                    UserDetails userDetails = userSecurityService.loadUserByUsername(username);
                    String accessToken = jwtUtil.generateAccessToken(username,userDetails.getAuthorities());

                    JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenDto.getRefreshToken()).build();

                    ResponseBody<JwtResponseDTO> response = ResponseBody.<JwtResponseDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Pair tokens created successfully")
                            .metadata(jwtResponseDTO)
                            .build();

                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseThrow(() -> new RuntimeException(String.format("Refresh token: '%s' was not found in our system", refreshTokenDto.getRefreshToken())));
    }
}
