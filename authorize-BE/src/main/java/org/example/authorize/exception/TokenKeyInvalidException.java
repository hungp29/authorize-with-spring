package org.example.authorize.exception;

/**
 * Token Key Invalid Exception.
 */
public class TokenKeyInvalidException extends RuntimeException {

    public TokenKeyInvalidException(String message) {
        super(message);
    }

    public TokenKeyInvalidException(String message, Throwable thr) {
        super(message, thr);
    }
}
