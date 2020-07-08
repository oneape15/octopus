package com.oneape.octopus.commons.value;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

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
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }
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
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            log.error("String2Integer error. value: {}", val);
        }
        return defaultVal;
    }

    public static Boolean toBool(String val) {
        return toBool(val, null);
    }

    public static Boolean toBool(String val, Boolean defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }

        try {
            return Boolean.parseBoolean(val);
        } catch (Exception e) {
            log.warn("String2Boolean error. value: {}", val);
        }
        return defaultVal;
    }

    public static Float toFloat(String val) {
        return toFloat(val, null);
    }

    public static Float toFloat(String val, Float defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }

        try {
            return Float.parseFloat(val);
        } catch (Exception e) {
            log.warn("String2Float error. value: {}", val);
        }
        return defaultVal;
    }

    public static Double toDouble(String val) {
        return toDouble(val, null);
    }

    public static Double toDouble(String val, Double defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }

        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            log.warn("String2Double error. value: {}", val);
        }
        return defaultVal;
    }

    public static BigDecimal toDecimal(String val) {
        return toDecimal(val, null);
    }

    public static BigDecimal toDecimal(String val, BigDecimal defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }

        try {
            return new BigDecimal(val);
        } catch (Exception e) {
            log.warn("String2Decimal error. value: {}", val);
        }
        return defaultVal;
    }
}
