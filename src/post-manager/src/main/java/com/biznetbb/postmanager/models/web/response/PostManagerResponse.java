package com.biznetbb.postmanager.models.web.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostManagerResponse {
    String postOwner;
    String content;
    String image;
}
