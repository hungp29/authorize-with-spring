package org.example.authorize.exception;

/**
 * Authentication Key Exception.
 */
public class AuthenticationKeyException extends RuntimeException {

    public AuthenticationKeyException(String message) {
        super(message);
    }

    public AuthenticationKeyException(String message, Throwable thr) {
        super(message, thr);
    }
}
