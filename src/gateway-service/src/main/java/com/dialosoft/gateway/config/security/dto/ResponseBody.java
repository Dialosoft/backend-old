package com.dialosoft.gateway.config.security.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseBody<T> {
    Integer statusCode;
    String message;
    T data;
}
