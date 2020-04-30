package org.example.authorize.entity.common;


import java.lang.annotation.*;


/**
 * Id generator annotation.
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdGenerator {

    /**
     * Prefix value
     *
     * @return the prefix of id
     */
    String value() default "";

    /**
     * Get field to generate id
     *
     * @return return the field to get value to generator id
     */
    String field() default "";
}
