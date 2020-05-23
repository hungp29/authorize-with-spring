package org.example.authorize.exception;

/***
 * Max Length Id exception.
 */
public class MaxLengthIdException extends RuntimeException {

    public MaxLengthIdException(String message) {
        super(message);
    }

    public MaxLengthIdException(String message, Throwable thr) {
        super(message, thr);
    }
}
