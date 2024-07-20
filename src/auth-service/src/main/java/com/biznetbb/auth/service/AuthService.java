package com.biznetbb.auth.service;

import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.biznetbb.auth.persistence.entity.UserEntity;
import com.biznetbb.auth.persistence.repository.UserRepository;
import com.biznetbb.auth.service.dto.LoginDto;
import com.biznetbb.auth.service.dto.RegisterDto;
import com.biznetbb.auth.web.config.SecurityConfig;
import com.biznetbb.auth.web.config.jwt.JwtUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final SecurityConfig config;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    public ResponseEntity<?> register(RegisterDto registerDto){
        if(userRepository.findByUsername(registerDto.getUsername()) != null &&
            userRepository.findByEmail(registerDto.getEmail()) != null){
            return new ResponseEntity<>("any of the parameters already exists", HttpStatus.CONFLICT);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setEmail(registerDto.getEmail());
        userEntity.setPassword(config.encoder().encode(registerDto.getPassword()));
        UserEntity newUser = userRepository.save(userEntity);

        if(newUser == null){
            return new ResponseEntity<>("User could not be created", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("User " + newUser.getUuid() + " created sucessfully", HttpStatus.CREATED);
    }

    public ResponseEntity<?> login(LoginDto loginDto){
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(
            loginDto.getUsername(),
            loginDto.getPassword()
            );
        try {
            Authentication authentication = this.authenticationManager.authenticate(login);
            System.out.println(authentication.getPrincipal());
            String jwt = jwtUtil.create(loginDto.getUsername());
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
}
