package com.dialosoft.auth.web.dto.response;

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
