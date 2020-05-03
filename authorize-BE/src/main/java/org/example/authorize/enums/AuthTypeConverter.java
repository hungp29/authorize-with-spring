package org.example.authorize.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

/**
 * Converter for enum AuthType.
 */
@Converter(autoApply = true)
public class AuthTypeConverter implements AttributeConverter<AuthType, String> {

    @Override
    public String convertToDatabaseColumn(AuthType attribute) {
        return null != attribute ? attribute.getCode() : null;
    }

    @Override
    public AuthType convertToEntityAttribute(String dbData) {
        if (null == dbData) {
            return null;
        }
        return Stream.of(AuthType.values()).
                filter(authType -> authType.getCode().equals(dbData)).
                findFirst().
                orElseThrow(IllegalArgumentException::new);
    }
}
