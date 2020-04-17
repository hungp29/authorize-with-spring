package org.example.authorize.app.authentication;

import lombok.Data;

@Data
public class AuthReq {

    private String username;

    private String password;

    private boolean rememberMe;
}
