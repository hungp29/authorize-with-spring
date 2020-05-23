package org.example.authorize.component.httpdefault.fieldconverter;

/**
 * Default Field Converter.
 */
public class DefaultFieldConverter implements FieldConverter<Object, Object> {

    @Override
    public Object convertFieldEntityToDTO(Object entityField) {
        return entityField;
    }

    @Override
    public Object convertFieldDTOToEntity(Object dtoField) {
        return dtoField;
    }
}
