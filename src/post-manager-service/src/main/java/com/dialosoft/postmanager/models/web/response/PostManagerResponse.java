package com.dialosoft.postmanager.models.web.response;

import com.dialosoft.postmanager.models.Comments;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostManagerResponse {
    String postOwner;
    String content;
    String image;
    List<Comments> comments;
}
