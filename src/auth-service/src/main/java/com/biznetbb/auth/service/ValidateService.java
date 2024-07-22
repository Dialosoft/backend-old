package com.biznetbb.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.biznetbb.auth.web.config.jwt.JwtUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ValidateService {
    private final JwtUtil jwtUtil;

    public ResponseEntity<?> validate(String token){
        if(token == null || !token.startsWith("Bearer ")){
            return ResponseEntity.badRequest().body("Invalid authorization header");
        }

        String jwt = token.substring(7).trim();

        if(!this.jwtUtil.isValid(jwt)){
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }else {
            return new ResponseEntity<>("valid", HttpStatus.OK);
        }
    }
}