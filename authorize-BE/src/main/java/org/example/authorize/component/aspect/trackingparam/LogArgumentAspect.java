package org.example.authorize.component.aspect.trackingparam;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.authorize.component.aspect.BaseAspect;
import org.springframework.stereotype.Component;

/**
 * Log Argument Aspect.
 */
@Slf4j
@Aspect
@Component
public class LogArgumentAspect extends BaseAspect {

    @Before("org.example.authorize.component.aspect.JoinPointConfiguration.pointCutArgumentAnnotation()")
    public void logArgument(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (args.length > 0) {
            logWithPrefix(ARGUMENT, "List Arguments");
            for (Object arg : args) {
                logWithPrefix(ARGUMENT, arg.toString());
            }
        }
    }
}
