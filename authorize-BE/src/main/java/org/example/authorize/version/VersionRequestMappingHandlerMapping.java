package org.example.authorize.version;

import org.example.authorize.utils.ObjectUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * Version Request Mapping Handler Mapping.
 */
public class VersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<VersionCondition> getCustomMethodCondition(Method method) {
        APIVersion controllerAnnotation = ObjectUtils.getAnnotation(method.getDeclaringClass(), APIVersion.class);
        APIVersion methodAnnotation = ObjectUtils.getAnnotation(method, APIVersion.class);

        if (null != methodAnnotation) {
            return createVersionCondition(methodAnnotation);
        } else if (null != controllerAnnotation) {
            return createVersionCondition(controllerAnnotation);
        } else {
            return null;
        }
    }

    /**
     * Create Version Condition.
     *
     * @param apiVersion Annotation APIVersion
     * @return return RequestCondition instance
     */
    private RequestCondition<VersionCondition> createVersionCondition(APIVersion apiVersion) {
        return new VersionCondition(apiVersion.value());
    }
}
