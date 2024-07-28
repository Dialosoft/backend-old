package com.biznetbb.auth.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {

    @NotNull(message = "Refresh token is required.")
    private String refreshToken;
}
