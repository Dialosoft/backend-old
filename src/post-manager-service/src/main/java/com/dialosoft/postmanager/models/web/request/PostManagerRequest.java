package com.dialosoft.postmanager.models.web.request;

import com.dialosoft.postmanager.models.Comments;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostManagerRequest {
    String id;
    String username;
    String content;
    String image;
}
