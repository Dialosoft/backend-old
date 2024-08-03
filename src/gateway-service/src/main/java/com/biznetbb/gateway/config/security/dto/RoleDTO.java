package com.biznetbb.gateway.config.security.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {
    private String roleName;
    private Boolean adminRole;
    private Boolean modRole;
}
