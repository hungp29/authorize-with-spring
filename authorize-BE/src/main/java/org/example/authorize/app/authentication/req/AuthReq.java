package org.example.authorize.app.authentication.req;

import lombok.Data;

@Data
public class AuthReq {

    private String username;

    private String password;

    private String phone;

    private String otp;

    private boolean rememberMe;
}
