package com.dialosoft.gateway.config.error.exception;

import com.dialosoft.gateway.config.error.dto.ErrorDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class CustomTemplateException extends RuntimeException {

    protected final Throwable innerException;
    protected final HttpStatus httpStatus;

    public CustomTemplateException(String message) {
        super(message);
        this.innerException = null;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CustomTemplateException(String message, Throwable cause) {
        super(message, cause);
        this.innerException = cause;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CustomTemplateException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.innerException = cause;
        this.httpStatus = httpStatus;
    }

    public ErrorDTO toErrorDTO(String path) {

        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(httpStatus.value())
                .message(this.getMessage())
                .path(path)
                .exception(this.getClass().getSimpleName())
                .innerException(innerException != null ? innerException.getMessage() : null)
                .build();
    }
}