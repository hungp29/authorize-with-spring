package org.example.authorize.exception;

/**
 * Invalid token.
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable thr) {
        super(message, thr);
    }
}
