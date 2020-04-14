package org.example.authorize.exception;

/**
 * Save entity exception.
 */
public class SaveEntityException extends RuntimeException {

    public SaveEntityException(String message) {
        super(message);
    }

    public SaveEntityException(String message, Throwable thr) {
        super(message, thr);
    }
}
