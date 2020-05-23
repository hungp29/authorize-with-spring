package org.example.authorize.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * Object Utils.
 */
@Slf4j
public class ObjectUtils {

    /**
     * Prevent new instance.
     */
    private ObjectUtils() {
    }

    /**
     * Check object has annotation or not.
     *
     * @param object          object to check
     * @param annotationClass the annotation class
     * @return return true if object has annotation, otherwise return false
     */
    public static boolean hasAnnotation(Object object, Class<? extends Annotation> annotationClass) {
        if (null != object && null != annotationClass && annotationClass.isAnnotation()) {
            if (object instanceof Field) {
                return null != ((Field) object).getAnnotation(annotationClass);
            } else if (object instanceof Method) {
                return null != ((Method) object).getAnnotation(annotationClass);
            } else if (object instanceof Class<?>) {
                return null != ((Class<?>) object).getAnnotation(annotationClass);
            } else if (object instanceof Annotation) {
                return null != ((Annotation) object).annotationType().getAnnotation(annotationClass);
            } else {
                return null != object.getClass().getAnnotation(annotationClass);
            }
        }
        return false;
    }

    /**
     * Get Annotation from object if it's exist.
     *
     * @param object          object
     * @param annotationClass annotation class
     * @param <T>             generic of annotation
     * @return return annotation get from object
     */
    public static <T extends Annotation> T getAnnotation(Object object, Class<T> annotationClass) {
        if (null != object && null != annotationClass && annotationClass.isAnnotation()) {
            if (object instanceof Field) {
                return ((Field) object).getAnnotation(annotationClass);
            } else if (object instanceof Method) {
                return ((Method) object).getAnnotation(annotationClass);
            } else if (object instanceof Class<?>) {
                return ((Class<?>) object).getAnnotation(annotationClass);
            } else if (object instanceof Annotation) {
                return ((Annotation) object).annotationType().getAnnotation(annotationClass);
            } else {
                return object.getClass().getAnnotation(annotationClass);
            }
        }
        return null;
    }

    /**
     * Get Field from object, which have annotation is determine.
     *
     * @param object          object
     * @param annotationClass annotation class
     * @return return field if it exist, otherwise return false
     */
    public static Field getFieldHasAnnotation(Object object, Class<? extends Annotation> annotationClass) {
        if (null != object && null != annotationClass && annotationClass.isAnnotation()) {
            Field[] fields;
            if (object instanceof Class) {
                fields = ((Class<?>) object).getDeclaredFields();
            } else {
                fields = object.getClass().getDeclaredFields();
            }

            for (Field field : fields) {
                if (ObjectUtils.hasAnnotation(field, annotationClass)) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * Get Field from object, which have annotation specify.
     *
     * @param object          object
     * @param annotationClass annotation class
     * @param superClass      checking in super class if object don't have field
     * @return return field if it exist, otherwise return false
     */
    public static Field getFieldHasAnnotation(Object object, Class<? extends Annotation> annotationClass, boolean superClass) {
        Field field = null;
        if (null != object) {
            Class<?> classChecking = object.getClass();
            do {
                field = ObjectUtils.getFieldHasAnnotation(classChecking, annotationClass);
                classChecking = superClass ? classChecking.getSuperclass() : null;
            } while (null != classChecking && null == field);
        }
        return field;
    }

    /**
     * Check object have field or not.
     *
     * @param object    object to check
     * @param fieldName field name
     * @return return true if object have field, otherwise return false
     */
    public static boolean hasField(Object object, String fieldName) {
        if (null != object && !StringUtils.isEmpty(fieldName)) {
            try {
                return null != object.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                log.warn(object.getClass().getName() + " don't have field " + fieldName);
            }
        }
        return false;
    }

    /**
     * Get value of field by using get method.
     *
     * @param object           object
     * @param fieldName        the field name
     * @param returnValueClass return value class
     * @param <T>              generic of return value
     * @return return value
     */
    public static <T> T getValueOfFieldByGetMethod(Object object, String fieldName, Class<T> returnValueClass) {
        if (null != object && !StringUtils.isEmpty(fieldName)) {
            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            return ObjectUtils.getValueOfFieldByMethod(object, getMethodName, returnValueClass);
        }
        return null;
    }

    /**
     * Get value of field by using method.
     *
     * @param object           object
     * @param methodName       the method name
     * @param returnValueClass return value class
     * @param <T>              generic of return value
     * @return return value
     */
    public static <T> T getValueOfFieldByMethod(Object object, String methodName, Class<T> returnValueClass) {
        if (null != object && !StringUtils.isEmpty(methodName)) {
            try {
                Method getMethod = object.getClass().getMethod(methodName);
                Object value = getMethod.invoke(object);

                if (returnValueClass.isAssignableFrom(value.getClass())) {
                    return returnValueClass.cast(value);
                }
            } catch (NoSuchMethodException e) {
                log.warn(object.getClass().getName() + " don't have method " + methodName);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.warn("Cannot get value from method " + methodName + " of object " + object.getClass().getName());
            }
        }
        return null;
    }

    /**
     * Get generic class.
     *
     * @param clazz class contain generic
     * @param index index of generic
     * @return Class of generic, if cannot found then return null
     */
    public static Class<?> getGenericClass(Class<?> clazz, int index) {
        if (null != clazz) {
            Type[] types = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
            if (null != types && types.length > 0 && index < types.length) {
                return (Class<?>) types[index];
            }
        }
        return null;
    }

    /**
     * Get first generic class.
     *
     * @param clazz clazz contain generic
     * @return Class of generic, if cannot found then return null
     */
    public static Class<?> getGenericClass(Class<?> clazz) {
        return ObjectUtils.getGenericClass(clazz, 0);
    }

    /**
     * Get super class of object.
     *
     * @param object     object to check and get super class
     * @param superClass super class
     * @return super class if it's exist
     */
    public static Class<?> getSuperClass(Object object, Class<?> superClass) {
        if (null != object && null != superClass) {
            Class<?> checkingClass = object.getClass();
            do {
                checkingClass = checkingClass.getSuperclass();
                if (superClass.equals(checkingClass)) {
                    return checkingClass;
                }
            } while (null != checkingClass);
        }
        return null;
    }

    /**
     * Checking object has super class.
     *
     * @param object     object need to checking
     * @param superClass super class
     * @return true if object have super class, otherwise return false
     */
    public static boolean hasSuperClass(Object object, Class<?> superClass) {
        return null != ObjectUtils.getSuperClass(object, superClass);
    }
}
