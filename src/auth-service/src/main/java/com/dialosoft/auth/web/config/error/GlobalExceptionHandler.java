package com.dialosoft.auth.web.config.error;

import com.dialosoft.auth.web.config.error.exception.CustomTemplateException;
import com.dialosoft.auth.web.dto.response.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomTemplateException.class)
    public ResponseEntity<ErrorDTO> handleCustomTemplateException(CustomTemplateException ex, HttpServletRequest request) {

        ErrorDTO errorDTO = ex.toErrorDTO(request.getRequestURI());
        return ResponseEntity.status(errorDTO.getStatusCode()).body(errorDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Extracting validation error messages
        List<String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        // Building the ErrorDTO with validation errors
        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message("Validation failed for one or more fields")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .exception(ex.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGlobalException(Exception ex, HttpServletRequest request) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .exception(ex.getClass().getSimpleName())
                .innerException(ex.getCause() != null ? ex.getCause().getMessage() : null)
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}