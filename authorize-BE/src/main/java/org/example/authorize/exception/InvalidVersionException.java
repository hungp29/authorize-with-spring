package org.example.authorize.exception;

/**
 * Invalid Version Exception.
 */
public class InvalidVersionException extends RuntimeException {

    public InvalidVersionException(String message) {
        super(message);
    }

    public InvalidVersionException(String message, Throwable thr) {
        super(message, thr);
    }
}
