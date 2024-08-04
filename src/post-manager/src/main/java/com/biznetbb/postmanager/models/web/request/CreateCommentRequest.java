package com.biznetbb.postmanager.models.web.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCommentRequest {
    String username;
    String postId;
    String content;
}
