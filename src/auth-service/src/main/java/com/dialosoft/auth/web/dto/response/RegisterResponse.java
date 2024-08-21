package com.dialosoft.auth.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterResponse {

    @Schema(description = "User Seed phrase")
    private List<String> seedPhrase;
}
