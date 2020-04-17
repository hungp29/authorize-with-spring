package org.example.authorize.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Response<T> {

    private String code;

    private String message;

    private T data;

    private LocalDateTime timestamp;
}
