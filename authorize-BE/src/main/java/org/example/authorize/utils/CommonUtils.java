package org.example.authorize.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.authorize.utils.constants.Constants;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

    /**
     * Convert LocalDateTime to milliseconds.
     *
     * @param dateTime date time value
     * @return milliseconds
     */
    public static long convertLocalDateTimeToMilli(LocalDateTime dateTime) {
        if (null != dateTime) {
            return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return 0L;
    }

    /**
     * Convert milliseconds to LocalDateTime.
     *
     * @param millis milliseconds
     * @return LocalDateTime instance
     */
    public static LocalDateTime convertMilliToLocalDateTime(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
