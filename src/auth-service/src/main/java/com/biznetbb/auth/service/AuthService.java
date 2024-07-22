package com.biznetbb.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.biznetbb.auth.persistence.entity.UserEntity;
import com.biznetbb.auth.persistence.repository.UserRepository;
import com.biznetbb.auth.persistence.response.ResponseBody;
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
    
    public ResponseEntity<ResponseBody> register(RegisterDto registerDto){
        ResponseBody response = new ResponseBody();

        if(userRepository.findByUsername(registerDto.getUsername()) != null &&
            userRepository.findByEmail(registerDto.getEmail()) != null){

            response.setStatusCode(HttpStatus.CONFLICT.value());
            response.setMessage("any of the parameters already exists");
            response.setMetadata(null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setEmail(registerDto.getEmail());
        userEntity.setPassword(config.encoder().encode(registerDto.getPassword()));
        UserEntity newUser = userRepository.save(userEntity);

        if(newUser == null){
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("user could not be created");
            response.setMetadata(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("User " + newUser.getUuid() + " created sucessfully");
        response.setMetadata(null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseBody> login(LoginDto loginDto){
        ResponseBody response = new ResponseBody();
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(
            loginDto.getUsername(),
            loginDto.getPassword()
            );
        try {
            Authentication authentication = this.authenticationManager.authenticate(login);
            System.out.println(authentication.getPrincipal());
            String jwt = jwtUtil.create(loginDto.getUsername());

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("authenticated successfully");
            response.setMetadata(jwt);
            return new ResponseEntity<>(response, HttpStatus.OK);
            
            
        } catch (BadCredentialsException e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("Unauthorized");
            response.setMetadata(null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
