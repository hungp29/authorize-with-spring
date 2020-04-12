package org.example.authorize.security.permission;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionConditions {

    String value() default "";

    String type() default "";

    PermissionCondition[] conditions() default {};
}
