package com.dialosoft.postmanager.mapper;

import com.dialosoft.postmanager.models.dto.Comments;
import com.dialosoft.postmanager.models.entities.CommentsEntity;
import com.dialosoft.postmanager.models.web.request.CreateCommentCommonAttributes;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentsMapper {


    CommentsEntity toEntity(CreateCommentCommonAttributes comments);



    Comments toComment(CommentsEntity comments);

    List<Comments> toCommentList(List<CommentsEntity> commentsEntityList);

}
