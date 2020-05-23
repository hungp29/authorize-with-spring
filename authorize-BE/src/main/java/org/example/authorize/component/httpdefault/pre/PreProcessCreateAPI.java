package org.example.authorize.component.httpdefault.pre;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.authorize.component.httpdefault.dtoconfig.CreateRequestClassDTO;
import org.example.authorize.component.httpdefault.DefaultHttpRestController;
import org.example.authorize.utils.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Preprocess to create entity.
 * Convert Map object to DTO object and pass it to create controller.
 */
@Slf4j
@Aspect
@Component
public class PreProcessCreateAPI {

    private static final Class<?>[] SUPPORT_CLASS = {Map.class};

    /**
     * Process for create method.
     *
     * @param joinPoint join point
     * @param arg       argument
     * @return response DTO of entity
     * @throws Throwable
     */
    @Around("execution(* org.example.authorize.component.httpdefault.DefaultHttpRestController.create(..)) && args(arg)")
    public Object proceedCreateMethod(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
        Object proceed;
        if (isSupport(arg)) {
            Class<?> createRequestDTOClass = getClassForCreateRequestDTO(joinPoint);
            // Convert to create request DTO
            Object createRequest = convertToDTO(arg, createRequestDTOClass);
            proceed = joinPoint.proceed(new Object[]{createRequest});
        } else {

            proceed = joinPoint.proceed();
        }
        return proceed;
    }

    /**
     * Convert to DTO.
     *
     * @param arg                   arguments
     * @param createRequestDTOClass create request DTO class
     * @return dto object
     */
    private Object convertToDTO(Object arg, Class<?> createRequestDTOClass) {
        Object createRequestDTO = null;
        if (Map.class.isAssignableFrom(arg.getClass())) {
            createRequestDTO = ObjectUtils.convertMapToObject((Map) arg, createRequestDTOClass);
        }
        return createRequestDTO;
    }

    /**
     * Get class of Create Request DTO.
     *
     * @param joinPoint join point
     * @return class of create request DTO
     */
    private Class<?> getClassForCreateRequestDTO(ProceedingJoinPoint joinPoint) {
        if (ObjectUtils.hasSuperClass(joinPoint.getTarget(), DefaultHttpRestController.class)) {
            Class<?> controllerClass = joinPoint.getTarget().getClass();
            Class<?> entityClass = ObjectUtils.getGenericClass(controllerClass);

            if (ObjectUtils.hasAnnotation(entityClass, CreateRequestClassDTO.class)) {
                return ObjectUtils.getAnnotation(entityClass, CreateRequestClassDTO.class).value();
            }
        }
        return null;
    }

    /**
     * Checking class of argument is supported.
     *
     * @param args arguments object
     * @return true if it's supported
     */
    private boolean isSupport(Object args) {
        if (null != args) {
            for (Class<?> supportClass : SUPPORT_CLASS) {
                if (supportClass.isAssignableFrom(args.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }
}
