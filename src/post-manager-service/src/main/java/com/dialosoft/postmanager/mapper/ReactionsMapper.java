package com.dialosoft.postmanager.mapper;

import com.dialosoft.postmanager.models.entities.ReactionsEntity;
import com.dialosoft.postmanager.models.web.request.CreateReactionCommonAttributes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionsMapper {

    ReactionsEntity toEntity(CreateReactionCommonAttributes source);
}
