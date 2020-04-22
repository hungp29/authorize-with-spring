package org.example.authorize.enums;

public enum AuthType {

    USERNAME_PASSWORD("USERNAME_PASSWORD"),
    EMAIL_PASSWORD("EMAIL_PASSWORD"),
    REFRESH_TOKEN("REFRESH_TOKEN");

    private String code;

    AuthType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
