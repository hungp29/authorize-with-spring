package org.example.authorize.app.authentication.req;

import lombok.Data;
import lombok.ToString;

@Data
public class AuthReq {

    private String username;

    @ToString.Exclude
    private String password;

    private String phone;

    private String otp;

    private boolean rememberMe;
}
