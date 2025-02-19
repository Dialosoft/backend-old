package com.dialosoft.postmanager.models.web.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SavePostAsFavoriteFromUserRequest {
    String postId;
    Boolean isFavorite;
}
