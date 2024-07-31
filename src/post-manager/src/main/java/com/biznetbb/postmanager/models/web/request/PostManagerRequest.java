package com.biznetbb.postmanager.models.web.request;

import com.biznetbb.postmanager.models.Comments;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostManagerRequest {
    Long id;
    String username;
    String content;
    Byte image;
    Comments comments;
}
