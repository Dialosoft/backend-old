package com.biznetbb.postmanager.models.web.request;

import com.biznetbb.postmanager.models.Comments;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostManagerRequest {
    UUID id;
    String username;
    String content;
    String image;
}
