package com.dialosoft.auth.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

    @NotNull(message = "Refresh token is required.")
    private String refreshToken;
}
