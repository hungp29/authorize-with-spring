package org.example.authorize.exception;

/**
 * Invalid Entity Exception.
 */
public class InvalidEntityException extends RuntimeException {

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(String message, Throwable thr) {
        super(message, thr);
    }
}
