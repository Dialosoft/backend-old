package com.dialosoft.gateway.config.error.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorDTO {
    private LocalDateTime timestamp;
    private int statusCode;
    private String message;
    private String path;
    private String exception;
    private String innerException;
}
