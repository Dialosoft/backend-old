package com.dialosoft.auth.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

    USER("ROLE_USER", 1),
    ADMIN("ROLE_ADMIN", 2),
    MOD("ROLE_MOD", 3);

    private final String roleName;
    private final Integer permissionName;
}
