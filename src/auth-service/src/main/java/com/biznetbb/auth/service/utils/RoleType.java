package com.biznetbb.auth.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MOD("ROLE_MOD");

    private final String roleName;
}
