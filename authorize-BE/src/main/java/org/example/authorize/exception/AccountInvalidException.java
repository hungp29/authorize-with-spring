package org.example.authorize.exception;

/**
 * Account invalid exception.
 */
public class AccountInvalidException extends RuntimeException {

    public AccountInvalidException(String message) {
        super(message);
    }

    public AccountInvalidException(String message, Throwable thr) {
        super(message, thr);
    }
}
