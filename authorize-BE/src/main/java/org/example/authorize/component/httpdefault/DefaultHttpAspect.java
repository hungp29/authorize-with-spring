package org.example.authorize.component.httpdefault;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.authorize.utils.ObjectUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class DefaultHttpAspect {

    @Around("execution(* org.example.authorize.component.httpdefault.DefaultHttpRestController.create(..)) && args(param)")
    public Object proceedCreateMethod(ProceedingJoinPoint joinPoint, Object param) throws Throwable {
        CreateRequestClassDTO createRequestClassDTO = null;
        if (ObjectUtils.hasSuperClass(joinPoint.getTarget(), DefaultHttpRestController.class)) {
            Class<?> controllerClass = joinPoint.getTarget().getClass();
            Class<?> entityClass = ObjectUtils.getGenericClass(controllerClass);

            if (null != entityClass) {
                createRequestClassDTO = ObjectUtils.getAnnotation(entityClass, CreateRequestClassDTO.class);
            }
        }
        if (null != createRequestClassDTO) {

        }
        Object proceed = joinPoint.proceed();
        return proceed;
    }
}
