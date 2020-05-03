package org.example.authorize.exception;

/**
 * Token Key Invalid Exception.
 */
public class KeyNotFoundException extends RuntimeException {

    public KeyNotFoundException(String message) {
        super(message);
    }

    public KeyNotFoundException(String message, Throwable thr) {
        super(message, thr);
    }
}
