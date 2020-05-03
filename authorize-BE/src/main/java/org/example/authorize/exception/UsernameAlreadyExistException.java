package org.example.authorize.exception;

/**
 * Username Already Exist Exception
 */
public class UsernameAlreadyExistException extends RuntimeException {

    public UsernameAlreadyExistException(String message) {
        super(message);
    }

    public UsernameAlreadyExistException(String message, Throwable thr) {
        super(message, thr);
    }
}
