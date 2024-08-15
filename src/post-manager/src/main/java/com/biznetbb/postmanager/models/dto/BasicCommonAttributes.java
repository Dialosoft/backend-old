package com.biznetbb.postmanager.models.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasicCommonAttributes {
    String id;
    String username;
    String content;
}
