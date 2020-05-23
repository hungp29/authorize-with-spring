package org.example.authorize.exception;

/**
 * Convert Exception.
 */
public class ConvertException extends RuntimeException {

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable thr) {
        super(message, thr);
    }
}
