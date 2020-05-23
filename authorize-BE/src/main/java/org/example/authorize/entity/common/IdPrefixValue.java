package org.example.authorize.entity.common;


import java.lang.annotation.*;


/**
 * Id generator annotation.
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdPrefixValue {

    /**
     * Prefix value
     *
     * @return the prefix of id
     */
    String value() default "";

    /**
     * Get data from this field to generate id
     *
     * @return return the field to get value to generator id
     */
    String field() default "";

    /**
     * Max length of id.
     *
     * @return max length of id
     */
    int maxLengthId() default 35;
}
