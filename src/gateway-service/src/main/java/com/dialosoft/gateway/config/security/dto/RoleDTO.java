package com.dialosoft.gateway.config.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDTO implements Serializable {

    @JsonProperty("role_type")
    private String roleName;

    @JsonProperty("admin_role")
    private Boolean adminRole;

    @JsonProperty("mod_role")
    private Boolean modRole;

    @JsonProperty("permission")
    private Integer permission;
}