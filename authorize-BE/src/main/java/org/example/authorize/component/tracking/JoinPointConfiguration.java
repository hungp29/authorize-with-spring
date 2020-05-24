package org.example.authorize.component.tracking;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Join Pointcut Configuration.
 */
public class JoinPointConfiguration {

    /**
     * Point cut for annotation Log Argument.
     */
    @Pointcut("@annotation(org.example.authorize.component.tracking.param.LogArgument)")
    public void pointCutArgumentAnnotation() {
    }

    /**
     * Point cut for annotation Log Returning.
     */
    @Pointcut("@annotation(org.example.authorize.component.tracking.param.LogReturning)")
    public void pointCutReturningAnnotation() {
    }

    /**
     * Point cut for package App.
     */
    @Pointcut("execution(* org.example.authorize.app..*.*(..))")
    public void pointCutPackageApp() {
    }

    /**
     * Point cut for package Event.
     */
    @Pointcut("execution(* org.example.authorize.component.events..*.*(..))")
    public void pointCutPackageEvent() {
    }

    /**
     * Point cut for package Generic Conroller.
     */
    @Pointcut("execution(* org.example.authorize.component.httpdefault..*.*(..))")
    public void pointCutPackageHttpDefault() {
    }

    /**
     * Point cut for all package need to log.
     */
    @Pointcut("pointCutPackageApp() || pointCutPackageEvent() || pointCutPackageHttpDefault()")
    public void pointCutPackagesNeedLog() {
    }
}
