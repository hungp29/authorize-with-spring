package org.example.authorize.exception;

/**
 * Algorithm Exception.
 */
public class AlgorithmException extends RuntimeException {

    public AlgorithmException(String message) {
        super(message);
    }

    public AlgorithmException(String message, Throwable thr) {
        super(message, thr);
    }
}
