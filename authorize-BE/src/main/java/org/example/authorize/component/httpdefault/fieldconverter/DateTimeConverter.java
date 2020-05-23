package org.example.authorize.component.httpdefault.fieldconverter;

import org.example.authorize.utils.CommonUtils;

import java.time.LocalDateTime;

/**
 * Convert LocalDateTime to milliseconds.
 */
public class DateTimeConverter implements FieldConverter<LocalDateTime, Long> {

    @Override
    public Long convertFieldEntityToDTO(LocalDateTime entityField) {
        if (null != entityField) {
            return CommonUtils.convertLocalDateTimeToMilli(entityField);
        }
        return 0L;
    }

    @Override
    public LocalDateTime convertFieldDTOToEntity(Long dtoField) {
        if (null != dtoField) {
            return CommonUtils.convertMilliToLocalDateTime(dtoField);
        }
        return null;
    }
}
