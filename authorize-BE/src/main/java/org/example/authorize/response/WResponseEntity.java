package org.example.authorize.response;

import org.example.authorize.utils.constants.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;

/**
 * Custom response entity.
 *
 * @param <T> class of body
 */
public class WResponseEntity<T> extends ResponseEntity<Response<T>> {

    public static final String SUCCESS_CODE = "00000";
    public static final String ERROR_CODE = "00099";

    /**
     * Create a new {@code WResponseEntity} with the given entity body, and no headers.
     *
     * @param body the entity body
     */
    public WResponseEntity(@Nullable T body) {
        this(body, SUCCESS_CODE, Constants.EMPTY_STRING, null, HttpStatus.OK);
    }

    /**
     * Create a new {@code WResponseEntity} with the given body and status code, and no headers.
     *
     * @param body   the entity body
     * @param status the status code
     */
    public WResponseEntity(@Nullable T body, HttpStatus status) {
        this(body, SUCCESS_CODE, Constants.EMPTY_STRING, null, status);
    }

    /**
     * Create a new {@code WResponseEntity} with the given headers and status code, and no body.
     *
     * @param headers the entity headers
     * @param status  the status code
     */
    public WResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
        this(null, SUCCESS_CODE, Constants.EMPTY_STRING, headers, status);
    }

    /**
     * Create a new {@code HttpEntity} with the given body, headers, and status code.
     *
     * @param body    the entity body
     * @param headers the entity headers
     * @param status  the status code
     */
    public WResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
        this(body, SUCCESS_CODE, Constants.EMPTY_STRING, headers, status);
    }

    /**
     * Create a new {@code WResponseEntity} with the given body, response code and message.
     *
     * @param body    the entity body
     * @param code    the response code
     * @param message the response message
     */
    public WResponseEntity(@Nullable T body, @Nullable String code, @Nullable String message) {
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
    public WResponseEntity(@Nullable T body, @Nullable String code, @Nullable String message,
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
    public WResponseEntity(@Nullable T body, @Nullable String code, @Nullable String message,
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
    private static <T> Response<T> wrapBody(T body, String responseCode, String message) {
        Response<T> wrapBody = new Response<T>();
        wrapBody.setCode(responseCode);
        wrapBody.setMessage(message);
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
        return new WResponseEntity<T>(body);
    }

    /**
     * A shortcut for creating a {@code WResponseEntity} with the given body and set to {@linkplain HttpStatus#OK OK}.
     *
     * @return the created {@code WResponseEntity}
     */
    public static <T> WResponseEntity<T> error(String message) {
        message = null != message ? message : "The application have error, please try again later!";
        return new WResponseEntity<T>(null, ERROR_CODE, message);
    }
}
