package com.biznetbb.postmanager.mapper;

import com.biznetbb.postmanager.models.entities.PostEntity;
import com.biznetbb.postmanager.models.web.request.PostManagerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostManagerMapper {
    @Mapping(source = "image", target = "multimedia")
    @Mapping(source = "content", target = "content")
    PostEntity toEntity(PostManagerRequest request);
}
