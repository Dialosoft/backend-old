package com.biznetbb.auth.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biznetbb.auth.persistence.response.ResponseBody;
import com.biznetbb.auth.service.AuthService;
import com.biznetbb.auth.service.dto.LoginDto;
import com.biznetbb.auth.service.dto.RegisterDto;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService auth;

    @PostMapping("/register")
    public ResponseEntity<ResponseBody> register(@RequestBody RegisterDto registerDto){
        return auth.register(registerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        return auth.login(loginDto);
    }
}
