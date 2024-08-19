package com.dialosoft.postmanager.models.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comments extends BasicCommonAttributes{
    Integer positiveReaction;
    Integer negativeReaction;
    LocalDateTime creationTime;
    String parentCommentId;
    List<Comments> repliesComment;
}
