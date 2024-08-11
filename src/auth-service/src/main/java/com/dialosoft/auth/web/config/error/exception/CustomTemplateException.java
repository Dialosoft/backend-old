package com.dialosoft.auth.web.config.error.exception;

import com.dialosoft.auth.persistence.response.ErrorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CustomTemplateException extends RuntimeException {
    protected final String errorCode;
    protected final Throwable innerException;
    protected final HttpStatus httpStatus;

    public CustomTemplateException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.innerException = null;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CustomTemplateException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.innerException = cause;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CustomTemplateException(String message, String errorCode, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
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