package com.dialosoft.auth.web.controller;

import com.dialosoft.auth.persistence.response.ResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello-world")
@Tag(name = "Hello World", description = "Simple hello world endpoint that requires JWT authentication")
public class SimpleController {

    @Operation(summary = "Validates the service and returns a Hello World message",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseBody.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Access is denied",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<ResponseBody<?>> validate() {
        return ResponseEntity.ok(ResponseBody.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Hello world successful")
                .metadata(null)
                .build());
    }
}