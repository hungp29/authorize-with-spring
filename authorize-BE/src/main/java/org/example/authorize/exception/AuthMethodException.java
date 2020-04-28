package org.example.authorize.exception;

/**
 * Auth Method Exception.
 */
public class AuthMethodException extends RuntimeException {

    public AuthMethodException(String message) {
        super(message);
    }

    public AuthMethodException(String message, Throwable thr) {
        super(message, thr);
    }
}
