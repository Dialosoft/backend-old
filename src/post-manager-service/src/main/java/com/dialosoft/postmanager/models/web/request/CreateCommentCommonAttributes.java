package com.dialosoft.postmanager.models.web.request;

import com.dialosoft.postmanager.models.dto.BasicCommonAttributes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCommentCommonAttributes extends BasicCommonAttributes {
    String commentId;
    String postId;
}