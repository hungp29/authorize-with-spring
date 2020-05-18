package org.example.authorize.component.aspect;

import lombok.extern.slf4j.Slf4j;

/**
 * Base function for Aspect.
 */
@Slf4j
public class BaseAspect {
    private static final String PREFIX_TEMPLATE = "[%9s] ";

    protected static final String START = "START";
    protected static final String STOP = "STOP";
    protected static final String TIME = "TIME";
    protected static final String ARGUMENT = "ARGUMENT";
    protected static final String RETURNING = "RETURNING";

    /**
     * Log tracking method.
     *
     * @param flag    START, STOP, EXECUTION TIME
     * @param message the message
     */
    protected void logWithPrefix(String flag, String message) {
        log.info(String.format(PREFIX_TEMPLATE, flag) + message);
    }
}
