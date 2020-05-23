package org.example.authorize.component.httpdefault;

import org.example.authorize.component.httpdefault.fieldconverter.DefaultFieldConverter;
import org.example.authorize.component.httpdefault.fieldconverter.FieldConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to defined mapping between Entity and DTO.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapField {

    String from() default "";

    String mapTo() default "";

    SpecialValue specialFrom() default SpecialValue.NONE;

    Class<? extends FieldConverter<?, ?>> converter() default DefaultFieldConverter.class;
}
