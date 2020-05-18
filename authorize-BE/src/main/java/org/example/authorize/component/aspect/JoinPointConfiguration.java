package org.example.authorize.component.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Join Pointcut Configuration.
 */
public class JoinPointConfiguration {

    /**
     * Point cut for annotation Log Execution Time.
     */
    @Pointcut("@annotation(org.example.authorize.component.aspect.executiontime.LogExecutionTime)")
    public void pointCutExecutionTime() {
    }

    /**
     * Point cut for all package need to log.
     */
    @Pointcut("execution(* org.example.authorize.app..*.*(..)) || execution(* org.example.authorize.component.events..*.*(..))")
    public void pointCutPackagesNeedLog() {
    }
}
