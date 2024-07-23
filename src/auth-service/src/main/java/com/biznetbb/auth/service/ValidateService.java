package com.biznetbb.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.biznetbb.auth.persistence.response.ResponseBody;
import com.biznetbb.auth.web.config.jwt.JwtUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ValidateService {
    private final JwtUtil jwtUtil;

    public ResponseEntity<ResponseBody> validate(String token){
        ResponseBody response = new ResponseBody();

        if(token == null || !token.startsWith("Bearer ")){
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid authorization header");
            response.setMetadata(null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String jwt = token.substring(7).trim();

        if(!this.jwtUtil.isValid(jwt)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("is not a valid token");
            response.setMetadata(null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }else {
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("is a valid token");
            response.setMetadata(null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}