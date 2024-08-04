package com.biznetbb.postmanager.mapper;

import com.biznetbb.postmanager.models.entities.PostEntity;
import com.biznetbb.postmanager.models.web.request.PostManagerRequest;
import com.biznetbb.postmanager.models.web.response.PostManagerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostManagerMapper {
   // @Mapping(target = "multimedia", expression = "java(Base64.getDecoder().decode(request.getImage()))")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "username", target = "username")
    PostEntity toEntity(PostManagerRequest request);


    @Mapping(source = "content", target = "content")
    @Mapping(source = "username", target = "postOwner")
    PostManagerResponse toResponse(PostEntity entity);
}
