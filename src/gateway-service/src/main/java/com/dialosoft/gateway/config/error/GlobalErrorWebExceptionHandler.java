package com.dialosoft.gateway.config.error;

import com.dialosoft.gateway.config.error.dto.ErrorDTO;
import com.dialosoft.gateway.config.error.exception.CustomTemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@Slf4j
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(
            GlobalErrorAttributes errorAttributes,
            ApplicationContext applicationContext,
            ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        ErrorAttributeOptions options = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE);
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, options);
        Throwable throwable = getError(request);
        ErrorDTO errorDto = determineErrorDto(throwable, errorPropertiesMap, request);

        errorPropertiesMap.put("status", errorDto.getStatusCode());
        errorPropertiesMap.remove("error");

        return ServerResponse.status(errorDto.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorDto));
    }

    private ErrorDTO determineErrorDto(Throwable throwable, Map<String, Object> errorPropertiesMap, ServerRequest request) {
        if (throwable instanceof ResponseStatusException) {

            HttpStatusCode statusCode = ((ResponseStatusException) throwable).getStatusCode();

            return defaultErrorDTO(throwable, errorPropertiesMap, request, statusCode);

        } else if (throwable instanceof CustomTemplateException) {

            return ((CustomTemplateException) throwable).toErrorDTO(request.path());
        } else {

            return defaultErrorDTO(throwable, errorPropertiesMap, request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ErrorDTO defaultErrorDTO(Throwable throwable, Map<String, Object> errorPropertiesMap, ServerRequest request, HttpStatusCode httpStatus) {

        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(httpStatus.value())
                .message((String) errorPropertiesMap.get("message"))
                .path(request.path())
                .exception(throwable.getClass().getName())
                .innerException(throwable.getCause() != null ? throwable.getCause().toString() : null)
                .build();
    }
}