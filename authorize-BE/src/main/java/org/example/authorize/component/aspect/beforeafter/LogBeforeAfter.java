package org.example.authorize.component.aspect.beforeafter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.authorize.component.aspect.executiontime.LogExecutionTime;
import org.example.authorize.utils.ObjectUtils;
import org.springframework.stereotype.Component;

/**
 * Log Before and After method.
 */
@Slf4j
@Aspect
@Component
public class LogBeforeAfter {

    /**
     * Aspect before.
     *
     * @param joinPoint point cut
     */
    @Before("org.example.authorize.component.aspect.JoinPointConfiguration.pointCutPackagesNeedLog()")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        if (!ObjectUtils.hasAnnotation(methodSignature.getMethod(), LogExecutionTime.class)) {
            log.info(String.format("%-12s %s", "[START]", joinPoint.getSignature().toShortString()));
        }
    }

    /**
     * Aspect after.
     *
     * @param joinPoint point cut
     */
    @After("org.example.authorize.component.aspect.JoinPointConfiguration.pointCutPackagesNeedLog()")
    public void after(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        if (!ObjectUtils.hasAnnotation(methodSignature.getMethod(), LogExecutionTime.class)) {
            log.info(String.format("%-12s %s", "[END]", joinPoint.getSignature().toShortString()));
        }
    }
}
