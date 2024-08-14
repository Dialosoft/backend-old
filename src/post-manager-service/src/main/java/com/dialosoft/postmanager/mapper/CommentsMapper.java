package com.dialosoft.postmanager.mapper;

import com.dialosoft.postmanager.models.Comments;
import com.dialosoft.postmanager.models.entities.CommentsEntity;
import com.dialosoft.postmanager.models.web.request.CreateCommentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentsMapper {
    @Mapping(source = "username", target = "username")
    @Mapping(source = "content", target = "content")
    CommentsEntity toEntity(CreateCommentRequest comments);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "content", target = "content")
    Comments toComment(CommentsEntity comments);

    List<Comments> toCommentList(List<CommentsEntity> commentsEntityList);

}
