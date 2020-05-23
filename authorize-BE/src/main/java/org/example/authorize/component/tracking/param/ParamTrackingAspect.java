package org.example.authorize.component.tracking.param;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.authorize.component.tracking.BaseAspect;
import org.springframework.stereotype.Component;

/**
 * Log Argument Aspect.
 */
@Slf4j
@Aspect
@Component
public class ParamTrackingAspect extends BaseAspect {

    /**
     * Log Arguments.
     *
     * @param joinPoint Point cut
     */
    @Before("org.example.authorize.component.tracking.JoinPointConfiguration.pointCutArgumentAnnotation()")
    public void logArgument(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (args.length > 0) {
            logWithPrefix(ARGUMENT, ">>> " + (args.length > 1 ? "List Arguments" : "Argument") + " (" + args.length + ")");
            for (Object arg : args) {
                logWithPrefix(ARGUMENT, arg.toString());
            }
            logWithPrefix(ARGUMENT, ">>> END " + (args.length > 1 ? "List Arguments" : "Argument") );
        }
    }

    /**
     * Log return value.
     *
     * @param retVal    return value
     */
    @AfterReturning(value = "org.example.authorize.component.tracking.JoinPointConfiguration.pointCutReturningAnnotation()", returning = "retVal")
    public void logReturning(Object retVal) {
        logWithPrefix(RETURNING, retVal.toString());
    }
}
