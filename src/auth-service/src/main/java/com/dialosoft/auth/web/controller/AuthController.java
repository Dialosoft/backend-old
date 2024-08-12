package com.dialosoft.auth.web.controller;

import com.dialosoft.auth.persistence.response.JwtResponseDTO;
import com.dialosoft.auth.persistence.response.ResponseBody;
import com.dialosoft.auth.service.AuthService;
import com.dialosoft.auth.service.dto.LoginDto;
import com.dialosoft.auth.service.dto.RefreshTokenDto;
import com.dialosoft.auth.service.dto.RegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user",
            description = "Registers a new user in the system. Validates that the username and email do not already exist and stores the new user's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class))),
            @ApiResponse(responseCode = "409", description = "Username or email already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseBody<?>> register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @Operation(summary = "User login",
            description = "Authenticates a user using their username/email and password. If the credentials are correct, generates a JWT token and a refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseBody<?>> login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @Operation(summary = "JWT token renewal",
            description = "Renews the JWT token using a valid refresh token. This endpoint is used when the JWT token has expired, and a new token is needed without re-authenticating the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens successfully renewed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class))),
            @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class)))
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseBody<JwtResponseDTO>> refreshtoken(@Valid @RequestBody RefreshTokenDto request) {
        return authService.refreshTokens(request);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseBody<?>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return authService.logout(token);
    }

}