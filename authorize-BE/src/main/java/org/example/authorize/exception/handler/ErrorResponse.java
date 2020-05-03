package org.example.authorize.exception.handler;

import lombok.Data;

@Data
public class ErrorResponse {

    private String code;
    private String message;
}
