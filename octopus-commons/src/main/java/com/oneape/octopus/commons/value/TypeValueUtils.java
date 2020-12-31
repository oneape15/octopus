package com.oneape.octopus.commons.value;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-31 10:53.
 * Modify:
 */
@Slf4j
public class TypeValueUtils {
    public static Boolean str2Bool(String val, Boolean defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }

        try {
            return Boolean.parseBoolean(val);
        } catch (Exception e) {
            log.warn("String :'{}' to Boolean error!", val);
        }
        return defaultVal;
    }

    public static Integer str2int(String val, Integer defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }

        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            log.warn("String:'{}' to Integer error!", val);
        }
        return defaultVal;
    }

    public static String int2str(Integer val, String defaultVal) {
        if (val == null) {
            return defaultVal;
        }
        return String.valueOf(val);
    }

    public static Long str2long(String val, Long defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }

        try {
            return Long.parseLong(val);
        } catch (Exception e) {
            log.warn("String:'{}' to Long error!", val);
        }
        return defaultVal;
    }

    public static Long getOrDefault(Long val, Long defaultVal) {
        return val == null ? defaultVal : val;
    }

    public static Float str2float(String val, Float defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }

        try {
            return Float.parseFloat(val);
        } catch (Exception e) {
            log.warn("String:'{}' to Float error!", val);
        }
        return defaultVal;
    }

    public static Double str2double(String val, Double defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }

        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            log.warn("String:'{}' to Double error!", val);
        }
        return defaultVal;
    }

    public static BigDecimal str2decimal(String val, BigDecimal defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }

        try {
            return new BigDecimal(val);
        } catch (Exception e) {
            log.warn("String:'{}' to BigDecimal error!", val);
        }
        return defaultVal;
    }

    /**
     * check string is number string.
     *
     * @param str String
     * @return boolean true - number， false - not number string
     */
    public static boolean isNumber(String str) {
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
}
