package org.example.authorize.exception;

/**
 * Convert Exception.
 */
public class MappingFieldException extends RuntimeException {

    public MappingFieldException(String message) {
        super(message);
    }

    public MappingFieldException(String message, Throwable thr) {
        super(message, thr);
    }
}
