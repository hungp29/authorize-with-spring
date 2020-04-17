package org.example.authorize.exception;

/**
 * Token Expiration Exception.
 */
public class TokenExpirationException extends RuntimeException {

    public TokenExpirationException(String message) {
        super(message);
    }

    public TokenExpirationException(String message, Throwable thr) {
        super(message, thr);
    }
}
