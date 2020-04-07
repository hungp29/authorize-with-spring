package org.example.authorize.exception;

/**
 * Generator Exception.
 */
public class GeneratorException extends RuntimeException {

    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(Throwable thr) {
        super(thr);
    }

    public GeneratorException(String message, Throwable thr) {
        super(message, thr);
    }
}
