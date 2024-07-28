package com.biznetbb.auth.persistence.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseBody<T> {
    Integer statusCode;
    String message;
    T metadata;
}
