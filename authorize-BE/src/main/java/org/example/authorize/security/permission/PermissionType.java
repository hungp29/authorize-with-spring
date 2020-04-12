package org.example.authorize.security.permission;

/**
 * Permission Type.
 */
public enum PermissionType {

    READ("READ"),
    WRITE("WRITE"),
    UNKNOWN("UNKNOWN");

    private String code;

    PermissionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
