package org.example.authorize.enums;

public enum AuthType {

    EMAIL_PASSWORD("EMAIL_PASSWORD");

    private String code;

    AuthType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
