package org.example.authorize.exception;

/**
 * Invalid Value Exception.
 */
public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String message) {
        super(message);
    }

    public InvalidValueException(String message, Throwable thr) {
        super(message, thr);
    }
}
