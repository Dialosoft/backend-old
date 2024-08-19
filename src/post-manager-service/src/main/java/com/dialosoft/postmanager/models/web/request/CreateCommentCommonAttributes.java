package com.dialosoft.postmanager.models.web.request;

import com.dialosoft.postmanager.models.dto.BasicCommonAttributes;
import com.dialosoft.postmanager.models.dto.Comments;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCommentCommonAttributes extends BasicCommonAttributes {
    String commentId;
    String postId;
    Comments parentComment;
    List<Comments> repliesComment;
}
