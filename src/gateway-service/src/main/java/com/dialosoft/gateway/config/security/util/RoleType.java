package com.dialosoft.gateway.config.security.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType {

    USER("ROLE_USER", 1),
    ADMIN("ROLE_ADMIN", 2),
    MOD("ROLE_MOD", 3);

    private final String roleName;
    private final Integer permissionName;

    public static RoleType getRoleType(String roleName) {
        return Arrays.stream(RoleType.values())
                .filter(role -> role.getRoleName().equals(roleName))
                .findFirst()
                .orElse(RoleType.USER);
    }
}
