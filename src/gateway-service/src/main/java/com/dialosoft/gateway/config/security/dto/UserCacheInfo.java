package com.dialosoft.gateway.config.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties when serializing or deserializing
public class UserCacheInfo implements Serializable {

    private String uuid;
    private String username;
    private RoleDTO role;
    private boolean locked;
    private boolean disable;
}
