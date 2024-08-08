package com.dialosoft.gateway.config.security.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MOD("ROLE_MOD");

    private final String roleName;

    public static RoleType getRoleType(String roleName) {
        return Arrays.stream(RoleType.values())
                .filter(role -> role.getRoleName().equals(roleName))
                .findFirst()
                .orElse(RoleType.USER);
    }
}
