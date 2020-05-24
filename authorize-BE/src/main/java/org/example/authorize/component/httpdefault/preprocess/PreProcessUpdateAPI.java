package org.example.authorize.component.httpdefault.preprocess;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.authorize.component.httpdefault.DefaultHttpRestController;
import org.example.authorize.component.httpdefault.dtoconfig.UpdateRequestClassDTO;
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
public class PreProcessUpdateAPI {

    private static final Class<?>[] SUPPORT_CLASS = {Map.class};

    /**
     * Process for create method.
     *
     * @param joinPoint join point
     * @param arg       argument
     * @return response DTO of entity
     * @throws Throwable
     */
    @Around("execution(* org.example.authorize.component.httpdefault.DefaultHttpRestController.update(..)) && args(id, entity)")
    public Object proceedCreateMethod(ProceedingJoinPoint joinPoint, Object id, Object entity) throws Throwable {
        Object proceed;
        if (isSupport(entity)) {
            Class<?> createRequestDTOClass = getClassForUpdateRequestDTO(joinPoint);
            // Convert to create request DTO
            Object createRequest = convertToDTO(entity, createRequestDTOClass);
            proceed = joinPoint.proceed(new Object[]{id, createRequest});
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
    private Class<?> getClassForUpdateRequestDTO(ProceedingJoinPoint joinPoint) {
        if (ObjectUtils.hasSuperClass(joinPoint.getTarget(), DefaultHttpRestController.class)) {
            Class<?> controllerClass = joinPoint.getTarget().getClass();
            Class<?> entityClass = ObjectUtils.getGenericClass(controllerClass);

            if (ObjectUtils.hasAnnotation(entityClass, UpdateRequestClassDTO.class)) {
                return ObjectUtils.getAnnotation(entityClass, UpdateRequestClassDTO.class).value();
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
