package org.example.authorize.utils.generator.id;

import org.example.authorize.entity.common.IdGenerator;
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

        @SuppressWarnings("unchecked")
        Generator<String> generator = SpringContext.getBean(Generator.class, String.class);

        Field idField = ObjectUtils.getFieldHasAnnotation(object, Id.class);
        if (null != idField) {
            if (ObjectUtils.hasAnnotation(idField, IdGenerator.class)) {
                IdGenerator idGenerator = idField.getAnnotation(IdGenerator.class);

                String prefixValue = idGenerator.value();
                String fieldGenerate = idGenerator.field();

                prefix = !StringUtils.isEmpty(prefixValue) ? prefixValue : Constants.EMPTY_STRING;

                if (!StringUtils.isEmpty(fieldGenerate)) {
                    valueToGenerate = ObjectUtils.getValueOfFieldByGetMethod(object, fieldGenerate, String.class);
                }
            }
        }

        return prefix + (StringUtils.isEmpty(valueToGenerate) ? generator.generate() : generator.generate(valueToGenerate));
    }

}
