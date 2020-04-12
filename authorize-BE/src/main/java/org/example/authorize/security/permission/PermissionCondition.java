package org.example.authorize.security.permission;

import org.example.authorize.rbac.AccessCondition;
import org.example.authorize.rbac.RequestValueResolver;

import java.lang.annotation.*;

@Documented
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
//@PreAuthorize("checkPermission()")
public @interface PermissionCondition {

    Class<? extends AccessCondition<?>> condition();

    Class<? extends RequestValueResolver<?>> resolver();
}
