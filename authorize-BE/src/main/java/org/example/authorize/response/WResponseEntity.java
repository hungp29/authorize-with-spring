package org.example.authorize.response;

import org.example.authorize.exception.handler.ResponseCode;
import org.example.authorize.utils.constants.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * Custom response entity.
 *
 * @param <T> class of body
 */
public class WResponseEntity<T> extends ResponseEntity<Response<T>> {

    /**
     * Create a new {@code WResponseEntity} with the given entity body, and no headers.
     *
     * @param body the entity body
     */
    public WResponseEntity(@Nullable T body) {
        this(body, ResponseCode.SUCCESS_CODE, Constants.EMPTY_STRING, null, HttpStatus.OK);
    }

    /**
     * Create a new {@code WResponseEntity} with the given body and status code, and no headers.
     *
     * @param body   the entity body
     * @param status the status code
     */
    public WResponseEntity(@Nullable T body, HttpStatus status) {
        this(body, ResponseCode.SUCCESS_CODE, Constants.EMPTY_STRING, null, status);
    }

    /**
     * Create a new {@code WResponseEntity} with the given headers and status code, and no body.
     *
     * @param headers the entity headers
     * @param status  the status code
     */
    public WResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
        this(null, ResponseCode.SUCCESS_CODE, Constants.EMPTY_STRING, headers, status);
    }

    /**
     * Create a new {@code HttpEntity} with the given body, headers, and status code.
     *
     * @param body    the entity body
     * @param headers the entity headers
     * @param status  the status code
     */
    public WResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
        this(body, ResponseCode.SUCCESS_CODE, Constants.EMPTY_STRING, headers, status);
    }

    /**
     * Create a new {@code WResponseEntity} with the given body, response code and message.
     *
     * @param body    the entity body
     * @param code    the response code
     * @param message the response message
     */
    public WResponseEntity(@Nullable T body, @Nullable ResponseCode code, @Nullable String message) {
        this(body, code, message, null, HttpStatus.OK);
    }

    /**
     * Create a new {@code WResponseEntity} with the given body, response code, message and header.
     *
     * @param body    the entity body
     * @param code    the response code
     * @param message the response message
     * @param headers the entity header
     */
    public WResponseEntity(@Nullable T body, @Nullable ResponseCode code, @Nullable String message,
                           @Nullable MultiValueMap<String, String> headers) {
        this(body, code, message, headers, HttpStatus.OK);
    }

    /**
     * Create a new {@code HttpEntity} with the given body, headers, and status code.
     *
     * @param body    the entity body
     * @param code    the response code
     * @param message the response message
     * @param headers the entity headers
     * @param status  the status code
     */
    public WResponseEntity(@Nullable T body, @Nullable ResponseCode code, @Nullable String message,
                           @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
        super(wrapBody(body, code, message), headers, status);
    }

    /**
     * Wrap body.
     *
     * @param body         the body of response
     * @param responseCode the response code
     * @param message      the message
     * @return return Wrap body object
     */
    private static <T> Response<T> wrapBody(T body, ResponseCode responseCode, String message) {
        Assert.notNull(responseCode, "Response code cannot be null");
        Response<T> wrapBody = new Response<T>();
        wrapBody.setCode(responseCode);
        wrapBody.setMessage(!StringUtils.isEmpty(message) ? message : responseCode.getMessage());
        wrapBody.setData(body);
        wrapBody.setTimestamp(LocalDateTime.now());
        return wrapBody;
    }

    /**
     * A shortcut for creating a {@code WResponseEntity} with the given body and set to {@linkplain HttpStatus#OK OK}.
     *
     * @return the created {@code WResponseEntity}
     */
    public static <T> WResponseEntity<T> success(T body) {
        return new WResponseEntity<>(body);
    }

    /**
     * A shortcut for creating a {@code WResponseEntity} with the given message.
     *
     * @return the created {@code WResponseEntity}
     */
    public static <T> WResponseEntity<T> error(String message) {
        message = null != message ? message : "The application have error, please try again later!";
        return new WResponseEntity<>(null, ResponseCode.ERROR_CODE, message);
    }

    /**
     * A shortcut for creating a {@code WResponseEntity} with the given Response code.
     *
     * @return the created {@code WResponseEntity}
     */
    public static <T> WResponseEntity<T> error(ResponseCode code) {
        return new WResponseEntity<>(null, code, code.getMessage());
    }

    /**
     * A shortcut for creating a {@code WResponseEntity} with the given Response code and params of message.
     *
     * @param code   Response Code
     * @param params Params of message
     * @param <T>    Generic of WResponseEntity
     * @return the created {@code WResponseEntity}
     */
    public static <T> WResponseEntity<T> error(ResponseCode code, String... params) {
        return new WResponseEntity<>(null, code, code.getMessage(params));
    }
}
