package org.example.authorize.response;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import org.example.authorize.exception.handler.ResponseCode;

import java.time.LocalDateTime;

/**
 * Response class.
 *
 * @param <T> class of body
 */
@Data
public class Response<T> {

    private ResponseCode code;

    private String message;

    private T data;

    private LocalDateTime timestamp;
}
