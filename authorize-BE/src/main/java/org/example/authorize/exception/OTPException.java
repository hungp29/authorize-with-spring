package org.example.authorize.exception;

/**
 * OTP Exception.
 */
public class OTPException extends RuntimeException {

    public OTPException(String message) {
        super(message);
    }

    public OTPException(String message, Throwable thr) {
        super(message, thr);
    }
}
