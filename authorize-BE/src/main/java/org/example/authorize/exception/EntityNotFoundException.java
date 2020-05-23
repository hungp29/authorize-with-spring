package org.example.authorize.exception;

/**
 * Entity Not Found Exception.
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable thr) {
        super(message, thr);
    }
}
