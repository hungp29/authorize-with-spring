package org.example.authorize.security.permission;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("checkPermission()")
public @interface PermissionConditions {

    String value() default "";

    PermissionType type() default PermissionType.NONE;

    PermissionCondition[] conditions() default {};
}
