package org.example.authorize.component.generator.id;

import org.example.authorize.entity.common.IdPrefixValue;
import org.example.authorize.exception.MaxLengthIdException;
import org.example.authorize.utils.ObjectUtils;
import org.example.authorize.utils.SpringContext;
import org.example.authorize.utils.constants.Constants;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * String Identifier Generator.
 */
public class StringIdentifierGenerator implements IdentifierGenerator {

    /**
     * Generate id value, if the field value to generation id is determined, the id will be generated base on this value,
     * otherwise the id value will be generated random.
     *
     * @param session session instance
     * @param object  object entity
     * @return return id generated
     * @throws HibernateException
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session,
                                 Object object) throws HibernateException {
        String prefix = Constants.EMPTY_STRING;
        String valueToGenerate = Constants.EMPTY_STRING;
        int maxLength = 0;

        @SuppressWarnings("unchecked")
        Generator<String> generator = SpringContext.getBean(Generator.class, String.class);

        // Get annotation Id prefix value from class
        IdPrefixValue idPrefixValue = ObjectUtils.getAnnotation(object, IdPrefixValue.class);

        // Checking annotation IdPrefixValue in Id field of object
        Field idField = ObjectUtils.getFieldHasAnnotation(object, Id.class, true);
        if (ObjectUtils.hasAnnotation(idField, IdPrefixValue.class)) {
            idPrefixValue = idField.getAnnotation(IdPrefixValue.class);
        }

        if (null != idPrefixValue) {
            prefix = idPrefixValue.value();
            valueToGenerate = getValueToGenerateId(object, idPrefixValue.field());
            maxLength = idPrefixValue.maxLengthId();
        }

        String idValue = prefix + (StringUtils.isEmpty(valueToGenerate)
                ? generator.generate()
                : generator.generate(valueToGenerate));

        if (idValue.length() > maxLength) {
            throw new MaxLengthIdException("Length of Id field of " + object.getClass().getName() +
                    " entity exceeds " + maxLength + " characters: " + idValue);
        }

        return prefix + (StringUtils.isEmpty(valueToGenerate) ? generator.generate() : generator.generate(valueToGenerate));
    }

    /**
     * Get value to generate Id.
     *
     * @param object    object
     * @param fieldName field name of object
     * @return value of field
     */
    private String getValueToGenerateId(Object object, String fieldName) {
        if (null != object && !StringUtils.isEmpty(fieldName)) {
            return ObjectUtils.getValueOfFieldByGetMethod(object, fieldName, String.class);
        }
        return Constants.EMPTY_STRING;
    }

}
