package com.oneape.octopus.commons.value;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DataUtils {
    /**
     * Determines whether the string is Integer
     *
     * @param str String
     * @return boolean true - number string， false - not number string.
     */
    public static boolean isInteger(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        for (char ch : str.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    public static Long toLong(String val) {
        return toLong(val, null);
    }

    public static Long toLong(String val, Long defaultVal) {
        try {
            return Long.parseLong(val);
        } catch (Exception e) {
            log.error("String2Long error, value: {}", val);
        }
        return defaultVal;
    }

    public static Integer toInt(String val) {
        return toInt(val, null);
    }

    public static Integer toInt(String val, Integer defaultVal) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            log.error("String2Integer error. value: {}", val);
        }
        return defaultVal;
    }
}
