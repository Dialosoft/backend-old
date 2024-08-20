package com.dialosoft.auth.web.controller;

import com.dialosoft.auth.persistence.response.JwtResponseDTO;
import com.dialosoft.auth.persistence.response.RecoverTokenResponse;
import com.dialosoft.auth.persistence.response.ResponseBody;
import com.dialosoft.auth.service.AuthService;
import com.dialosoft.auth.service.RecoverService;
import com.dialosoft.auth.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RecoverService recoverService;

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

    @Operation(
            summary = "Logs out the user by invalidating the JWT token",
            description = "This endpoint logs out the user by invalidating the provided JWT token.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBody.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - The token is missing or invalid",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Access is denied",
                    content = @Content)
    })
    @GetMapping("/logout")
    public ResponseEntity<ResponseBody<?>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Extract the token from the Authorization header
        return authService.logout(token);
    }

    @PostMapping("/recover-token")
    public ResponseEntity<ResponseBody<RecoverTokenResponse>> recoverToken(
            @Valid @RequestBody @Schema(implementation = RecoverDto.class) RecoverDto request) {

        return ResponseEntity.ok(recoverService.checkHashPhraseAndGetRecoverToken(request));
    }

    @PutMapping("/recover-password")
    public ResponseEntity<?> recoverPassword(@RequestBody RecoverChangePasswordDto request, @RequestHeader("Recover") String header) {

        return ResponseEntity.ok(recoverService.applyRecoverChangePassword(request, header));
    }

}