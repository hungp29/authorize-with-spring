package org.example.authorize.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.authorize.rbac.AbstractAccessCondition;
import org.example.authorize.rbac.AccessCondition;
import org.example.authorize.rbac.ConditionPrototype;
import org.example.authorize.rbac.RequestValueResolver;
import org.example.authorize.security.permission.PermissionCondition;
import org.example.authorize.security.permission.PermissionType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class PermissionUtils {

    /**
     * Prevents new instance.
     */
    private PermissionUtils() {
    }

    /**
     * Get condition prototype.
     *
     * @param permissionCondition permission condition annotation
     * @return condition prototype
     */
    public static ConditionPrototype getConditionPrototype(PermissionCondition permissionCondition) {
        ConditionPrototype conditionPrototype = null;

        try {
            Class<? extends AccessCondition<?>> clazz = permissionCondition.condition();
            Constructor<? extends AccessCondition<?>> cons = clazz.getConstructor(RequestValueResolver.class);
            AccessCondition<?> object = cons.newInstance(new Object[]{null});
            conditionPrototype = object.getConditionPrototype();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        return conditionPrototype;
    }

    /**
     * Get default permission type by request method.
     *
     * @param requestMethod request method
     * @return return "read" if request method is GET, "write" if request method is POST, PUT or DELETE, otherwise return "unknown"
     */
    public static String getDefaultPermissionType(RequestMethod requestMethod) {
        switch (requestMethod) {
            case GET:
                return PermissionType.READ.getCode();
            case POST:
            case PUT:
            case DELETE:
                return PermissionType.WRITE.getCode();
            default:
                return PermissionType.UNKNOWN.getCode();
        }
    }
}
