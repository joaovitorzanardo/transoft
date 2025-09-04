package br.com.transoft.backend.constants;

import lombok.Getter;

@Getter
public enum Role {

    DRIVER("DRIVER"),
    PASSENGER("PASSENGER"),
    MANAGER("MANAGER"),
    SYS_ADMIN("SYS_ADMIN");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public static Role fromString(String roleString) {
        for (Role role : Role.values()) {
            if (role.role.equals(roleString)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Invalid role: " + roleString);
    }

}
