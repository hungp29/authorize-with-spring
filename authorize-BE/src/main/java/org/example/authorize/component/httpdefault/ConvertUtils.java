package org.example.authorize.component.httpdefault;

import org.example.authorize.component.httpdefault.fieldconverter.FieldConverter;
import org.example.authorize.exception.MappingFieldException;
import org.example.authorize.utils.ObjectUtils;
import org.example.authorize.utils.constants.Constants;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Convert Utils.
 */
public class ConvertUtils {

    private ConvertUtils() {
    }

    /**
     * Convert Entity to DTO.
     *
     * @param entity   entity instance
     * @param dtoClass DTO class
     * @param <T>      generic of entity
     * @return dto instance
     */
    public static <T> Object convertEntityToDTO(T entity, Class<?> dtoClass) {
        Object dto;
        try {
            dto = ObjectUtils.newInstanceFromClass(dtoClass);
            List<Field> dtoFields = ObjectUtils.getFields(dtoClass);
            List<Field> entityFields = ObjectUtils.getFields(entity.getClass());

            // Mapping data from Entity to DTO
            for (Field dtoField : dtoFields) {
                IgnoreMapping ignoreMapping = ObjectUtils.getAnnotation(dtoField, IgnoreMapping.class);
                if (!checkingIgnoreMapping(ignoreMapping, entity.getClass())) {
                    Field entityField = findFieldByMapFieldFrom(entityFields, dtoField.getName());
                    MapField mapField = ObjectUtils.getAnnotation(entityField, MapField.class);

                    Object value;
                    if (null != mapField) {
                        String entityFieldPath = mapField.mapTo();
                        // If don't have any mapTo configuration, using entity field name instead
                        if (StringUtils.isEmpty(entityFieldPath)) {
                            entityFieldPath = entityField.getName();
                        }
                        // Get value from field of entiy
                        Class<?> converterClass = mapField.converter();
                        FieldConverter converter = (FieldConverter) ObjectUtils.newInstanceFromClass(converterClass);
                        value = converter.convertFieldEntityToDTO(getValueOfField(entity, entityFieldPath));
                    } else {
                        value = getValueOfField(entity, dtoField.getName());
                    }
                    setValueForField(dto, dtoField.getName(), value);
                }
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new MappingFieldException("Error occurs while convert Entity to DTO", e);
        }
        return dto;
    }

    /**
     * Find field by annotation MapField by from.
     *
     * @param fields list fields
     * @param from   from value
     * @return field if it's exist
     */
    private static Field findFieldByMapFieldFrom(List<Field> fields, String from) {
        if (!CollectionUtils.isEmpty(fields) && !StringUtils.isEmpty(from)) {
            for (Field field : fields) {
                MapField mapField = ObjectUtils.getAnnotation(field, MapField.class);
                if (null != mapField && (from.equals(mapField.from())) || from.equals(field.getName())) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * Convert DTO to Entity.
     *
     * @param dto         dto object
     * @param entityClass entity class
     * @param <T>         generic entity
     * @return entity
     */
    public static <T> T convertDTOToEntity(Object dto, Class<T> entityClass) {
        T entity;
        try {
            entity = ObjectUtils.newInstanceFromClass(entityClass);

            List<Field> entityFields = ObjectUtils.getFields(entityClass);

            // Mapping data from DTO to Entity
            for (Field entityField : entityFields) {
                IgnoreMapping ignoreMapping = ObjectUtils.getAnnotation(entityField, IgnoreMapping.class);
                if (!checkingIgnoreMapping(ignoreMapping, dto.getClass())) {
                    MapField mapField = ObjectUtils.getAnnotation(entityField, MapField.class);
                    // If field have configuration annotation MapField
                    if (null != mapField) {
                        String dtoFieldName = mapField.from();
                        String entityFieldPath = mapField.mapTo();
                        // If don't have any mapTo configuration, using entity field name instead
                        if (StringUtils.isEmpty(entityFieldPath)) {
                            entityFieldPath = entityField.getName();
                        }

                        // Get value of DTO
                        Object dtoFieldValue = getValueOfField(dto, dtoFieldName);

                        // Check and get data for special field
                        if (!SpecialValue.NONE.equals(mapField.specialFrom())) {
                            dtoFieldValue = SpecialValueImpl.getSpecialValue(mapField.specialFrom());
                        }

                        // Convert value of field from DTO to Entity
                        Class<?> converterClass = mapField.converter();
                        FieldConverter converter = (FieldConverter) ObjectUtils.newInstanceFromClass(converterClass);
                        dtoFieldValue = converter.convertFieldDTOToEntity(dtoFieldValue);

                        // Set value to entity
                        setValueForField(entity, entityFieldPath, dtoFieldValue);
                    } else {
                        // In case don't have any configuration MapField, set value for entity field by the field name same with DTO
                        Object dtoFieldValue = getValueOfField(dto, entityField.getName());
                        ObjectUtils.setValueForField(entity, entityField.getName(), dtoFieldValue);
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new MappingFieldException("Error occurs while convert DTO to Entity", e);
        }
        return entity;
    }

    /**
     * Get value of field of object.
     *
     * @param object    object contain field
     * @param fieldPath field path
     * @return value of field
     * @throws IllegalAccessException if cannot access field
     */
    private static Object getValueOfField(Object object, String fieldPath) throws IllegalAccessException {
        if (null != object && !StringUtils.isEmpty(fieldPath)) {
            String[] paths = fieldPath.split(Constants.DOT_REGEX);
            if (paths.length == 1) {
                return ObjectUtils.getValueOfField(object, paths[0]);
            } else {
                Object subObject = ObjectUtils.getValueOfField(object, paths[0]);
                String nextPath = fieldPath.substring(fieldPath.indexOf(Constants.DOT) + 1);
                return getValueOfField(subObject, nextPath);
            }
        }
        return null;
    }

    /**
     * Set value for field of object.
     *
     * @param object    object
     * @param fieldPath field path
     * @param value     value to set
     * @throws IllegalAccessException    throw exception if cannot access field
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class.
     * @throws InvocationTargetException if the underlying constructor
     *                                   throws an exception.
     */
    private static void setValueForField(Object object, String fieldPath, Object value) throws IllegalAccessException,
            NoSuchMethodException, InstantiationException, InvocationTargetException {
        if (null != object && !StringUtils.isEmpty(fieldPath) && null != value) {
            String[] paths = fieldPath.split(Constants.DOT_REGEX);
            if (paths.length == 1) {
                ObjectUtils.setValueForField(object, paths[0], value);
            } else {
                Field field = ObjectUtils.getField(object, paths[0]);
                if (null != field) {
                    Object subObject = ObjectUtils.newInstanceFromClass(field.getType());
                    String nextPath = fieldPath.substring(fieldPath.indexOf(Constants.DOT) + 1);
                    setValueForField(subObject, nextPath, value);
                    ObjectUtils.setValueForField(object, paths[0], subObject);
                }
            }
        }
    }

    /**
     * Checking ignore mapping field.
     *
     * @param ignoreMapping Annotation IgnoreMapping
     * @param checkingClass Class need to checking
     * @return if checking class exists in IgnoreMapping value array then ignore mapping this field
     */
    private static boolean checkingIgnoreMapping(IgnoreMapping ignoreMapping, Class<?> checkingClass) {
        boolean ignore = false;
        if (null != ignoreMapping && null != checkingClass) {
            Class<?>[] classes = ignoreMapping.value();
            for (Class<?> clazz : classes) {
                if (clazz.equals(checkingClass)) {
                    ignore = true;
                    break;
                }
            }
        }
        return ignore;
    }

    /**
     * Copy value from DTO to Entity.
     *
     * @param dto    dto instance
     * @param entity entity instance
     */
    public static void copyValueFromDTOToEntity(Object dto, Object entity) {
        if (null != dto && null != entity) {
            try {
                List<Field> entityFields = ObjectUtils.getFields(entity.getClass());

                // Copy value from DTO to Entity
                for (Field entityField : entityFields) {
                    IgnoreMapping ignoreMapping = ObjectUtils.getAnnotation(entityField, IgnoreMapping.class);
                    if (!checkingIgnoreMapping(ignoreMapping, dto.getClass())) {
                        MapField mapField = ObjectUtils.getAnnotation(entityField, MapField.class);
                        // If field have configuration annotation MapField
                        if (null != mapField) {
                            String dtoFieldName = mapField.from();
                            String entityFieldPath = mapField.mapTo();

                            // If don't have any mapTo configuration, using entity field name instead
                            if (StringUtils.isEmpty(entityFieldPath)) {
                                entityFieldPath = entityField.getName();
                            }

                            // Get value of DTO
                            Object dtoFieldValue = getValueOfField(dto, dtoFieldName);

                            // Check and get data for special field
                            if (!SpecialValue.NONE.equals(mapField.specialFrom())) {
                                dtoFieldValue = SpecialValueImpl.getSpecialValue(mapField.specialFrom());
                            }

                            // Convert value of field from DTO to Entity
                            Class<?> converterClass = mapField.converter();
                            FieldConverter converter = (FieldConverter) ObjectUtils.newInstanceFromClass(converterClass);
                            dtoFieldValue = converter.convertFieldDTOToEntity(dtoFieldValue);

                            // Set value to entity
                            setValueForField(entity, entityFieldPath, dtoFieldValue);
                        } else {
                            // In case don't have any configuration MapField, set value for entity field by the field name same with DTO
                            Object dtoFieldValue = getValueOfField(dto, entityField.getName());
                            ObjectUtils.setValueForField(entity, entityField.getName(), dtoFieldValue);
                        }
                    }
                }
            } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                throw new MappingFieldException("Error occurs while copy DTO to Entity", e);
            }
        }
    }
}
