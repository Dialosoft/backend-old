package com.biznetbb.postmanager.models.web.request;

import com.biznetbb.postmanager.models.dto.BasicCommonAttributes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostManagerCommonAttributes extends BasicCommonAttributes {
    String image;
}
