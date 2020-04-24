package org.example.authorize.enums;

public enum AuthType {

    USERNAME_PASSWORD("USERNAME_PASSWORD"),
    EMAIL_PASSWORD("EMAIL_PASSWORD"),
    PHONE_NUMBER("PHONE_NUMBER"),
    REFRESH_TOKEN("REFRESH_TOKEN");

    private String code;

    AuthType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
