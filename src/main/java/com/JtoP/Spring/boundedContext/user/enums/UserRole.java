package com.JtoP.Spring.boundedContext.user.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
    // UserRole.ADMIN.value = "ROLE_ADMIN"
    // UserRole role = UserRole.ADMIN;
    // System.out.println(role.getValue()); // "ROLE_ADMIN" 출력
}
