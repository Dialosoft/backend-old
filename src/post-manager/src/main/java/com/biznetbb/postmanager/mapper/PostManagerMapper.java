package com.biznetbb.postmanager.mapper;

import com.biznetbb.postmanager.models.entities.PostEntity;
import com.biznetbb.postmanager.models.web.request.PostManagerRequest;
import com.biznetbb.postmanager.models.web.response.PostManagerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring",imports = {Base64.class})
public interface PostManagerMapper {
    @Mapping(target = "multimedia", expression = "java(Base64.getDecoder().decode(request.getImage()))")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "username", target = "username")
    PostEntity toEntity(PostManagerRequest request);


    @Mapping(target = "image", expression = "java(Base64.getEncoder().encodeToString(entity.getMultimedia()))")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "username", target = "postOwner")
    PostManagerResponse toResponse(PostEntity entity);

   List<PostManagerResponse> toResponseList(List<PostEntity> entity);
}
