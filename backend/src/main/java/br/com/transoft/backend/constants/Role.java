package br.com.transoft.backend.constants;

public enum RoleEnum {
    DRIVER("DRIVER"),
    PASSENGER("PASSENGER"),
    MANAGER("MANAGER");

    private String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public RoleEnum fromString(String roleString) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.role.equals(roleString)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Invalid role: " + roleString);
    }

    public String getRole() {
        return role;
    }

}
