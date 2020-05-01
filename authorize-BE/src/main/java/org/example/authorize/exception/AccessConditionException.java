package org.example.authorize.exception;

/**
 * Access Condition Exception.
 */
public class AccessConditionException extends RuntimeException {

    public AccessConditionException(String message) {
        super(message);
    }

    public AccessConditionException(String message, Throwable thr) {
        super(message, thr);
    }
}
