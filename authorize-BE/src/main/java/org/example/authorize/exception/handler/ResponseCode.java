package org.example.authorize.exception.handler;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Response code.
 */
public enum ResponseCode {
    SUCCESS_CODE("00000", "Success"),
    ERROR_CODE("00099", "We are experiencing an internal server problem. Please try again later"),
    ACCESS_DENIED("00100", "Access Denied"),
    BAD_CREDENTIALS("00101", "Bad Credentials"),
    ACCOUNT_DISABLED("00102", "Account is disabled"),
    ACCOUNT_LOCKED("00103", "Account is locked"),
    CREDENTIALS_EXPIRED("00104", "Credentials have expired");

    private String code;
    private String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
