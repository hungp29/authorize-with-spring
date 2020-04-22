package org.example.authorize.utils;

import org.example.authorize.utils.constants.Constants;
import org.springframework.util.StringUtils;

/**
 * Common Utils.
 */
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
}
