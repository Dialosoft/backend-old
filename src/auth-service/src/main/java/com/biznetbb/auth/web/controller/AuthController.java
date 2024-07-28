package com.biznetbb.auth.web.controller;

import com.biznetbb.auth.persistence.response.JwtResponseDTO;
import com.biznetbb.auth.persistence.response.ResponseBody;
import com.biznetbb.auth.service.AuthService;
import com.biznetbb.auth.service.RefreshTokenService;
import com.biznetbb.auth.service.dto.LoginDto;
import com.biznetbb.auth.service.dto.RefreshTokenDto;
import com.biznetbb.auth.service.dto.RegisterDto;
import com.biznetbb.auth.web.config.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ResponseBody<?>> register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBody<?>> login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseBody<JwtResponseDTO>> refreshtoken(@Valid @RequestBody RefreshTokenDto request) {
        return authService.refreshTokens(request);
    }
}
