package org.example.authorize.component.aspect.tracking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.authorize.component.aspect.BaseAspect;
import org.example.authorize.config.prop.ApplicationProperties;
import org.springframework.stereotype.Component;

/**
 * Log Before and After method.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogTracking extends BaseAspect {

    private final ApplicationProperties appProps;

    /**
     * Aspect before.
     *
     * @param joinPoint point cut
     */
    @Before("org.example.authorize.component.aspect.JoinPointConfiguration.pointCutPackagesNeedLog()")
    public void before(JoinPoint joinPoint) {
        if (appProps.isLogTracking()) {
            logWithPrefix(START, joinPoint.getSignature().toShortString());
        }
    }

    /**
     * Aspect after.
     *
     * @param joinPoint point cut
     */
    @After("org.example.authorize.component.aspect.JoinPointConfiguration.pointCutPackagesNeedLog()")
    public void after(JoinPoint joinPoint) {
        if (appProps.isLogTracking()) {
            logWithPrefix(STOP, joinPoint.getSignature().toShortString());
        }
    }

    /**
     * Measuring time to run method.
     *
     * @param joinPoint point cut
     * @return the result of proceeding
     * @throws Throwable if the invoked proceed throws anything
     */
    @Around("org.example.authorize.component.aspect.JoinPointConfiguration.pointCutPackagesNeedLog()")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed;
        if (appProps.isLogTracking()) {
            long start = System.currentTimeMillis();
            // Processing method
            proceed = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            logWithPrefix(EXECUTION_TIME, joinPoint.getSignature().toShortString() + " executed in " + executionTime + "ms");
        } else {
            proceed = joinPoint.proceed();
        }

        return proceed;
    }
}
