package org.example.authorize.component.aspect.executiontime;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Log Execution Time Aspect.
 */
@Slf4j
@Aspect
@Component
public class LogExecutionTimeAspect {

    @Around("org.example.authorize.component.aspect.JoinPointConfiguration.pointCutExecutionTime()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(String.format("%-12s %s", "[START]", joinPoint.getSignature().toShortString()));
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        log.info(String.format("%-12s %s", "[END]", joinPoint.getSignature().toShortString()));
        log.info(String.format("%-12s %s executed in %sms", "[TOTAL TIME]", joinPoint.getSignature().toShortString(), executionTime));
        return proceed;
    }
}
