package org.example.authorize.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response class.
 *
 * @param <T> class of body
 */
@Data
public class Response<T> {

    private String code;

    private String message;

    private T data;

    private LocalDateTime timestamp;
}
