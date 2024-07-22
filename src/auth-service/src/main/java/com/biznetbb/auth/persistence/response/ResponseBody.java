package com.biznetbb.auth.persistence.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBody {
    Integer statusCode;
    String message;
    Object metadata;
}
