package com.biznetbb.postmanager.models.web.response;

import com.biznetbb.postmanager.models.dto.Comments;
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
    String postId;
    Integer positiveReaction;
    Integer negativeReaction;
}
