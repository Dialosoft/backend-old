package com.dialosoft.auth.persistence.response;

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
