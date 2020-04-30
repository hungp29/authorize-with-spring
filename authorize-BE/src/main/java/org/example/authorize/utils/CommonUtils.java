package org.example.authorize.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.authorize.utils.constants.Constants;
import org.springframework.util.StringUtils;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * Common Utils.
 */
@Slf4j
public class CommonUtils {

    /**
     * Prevent new instance.
     */
    private CommonUtils() {
    }

    /**
     * Get first string value not empty.
     *
     * @param values list values
     * @return return first non empty value or return empty if all values are empty
     */
    public static String getFistValueNotEmpty(String... values) {
        for (String value : values) {
            if (!StringUtils.isEmpty(value)) {
                return value;
            }
        }
        return Constants.EMPTY_STRING;
    }

    /**
     * Convert String to byte array.
     *
     * @param value string value
     * @return return byte array
     */
    public static byte[] stringToByteArray(String value) {
        if (!StringUtils.isEmpty(value)) {
            return value.getBytes(StandardCharsets.UTF_8);
        }
        return null;
    }
}
