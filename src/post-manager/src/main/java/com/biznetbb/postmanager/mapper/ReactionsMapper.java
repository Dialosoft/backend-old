package com.biznetbb.postmanager.mapper;

import com.biznetbb.postmanager.models.entities.ReactionsEntity;
import com.biznetbb.postmanager.models.web.request.CreateReactionCommonAttributes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionsMapper {

    ReactionsEntity toEntity(CreateReactionCommonAttributes source);
}
