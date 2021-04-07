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
        if (StringUtils.isBlank(str)) return false;

        for (char ch : str.toCharArray()) {
            if (!Character.isDigit(ch)) return false;
        }
        return true;
    }

    /**
     * Convert String to Long
     *
     * @param val String
     * @return Long
     */
    public static Long str2Long(String val) {
        return str2Long(val, null);
    }

    /**
     * Convert String to Long with default value.
     *
     * @param val        String
     * @param defaultVal Long
     * @return Long
     */
    public static Long str2Long(String val, Long defaultVal) {
        if (StringUtils.isBlank(val)) return defaultVal;

        try {
            return Long.parseLong(val);
        } catch (Exception e) {
            log.error("String:'{}' to Long error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert String to Integer
     *
     * @param val String
     * @return Integer
     */
    public static Integer str2Integer(String val) {
        return str2Integer(val, null);
    }

    /**
     * Convert String to Integer with default value.
     *
     * @param val        String
     * @param defaultVal Integer
     * @return Integer
     */
    public static Integer str2Integer(String val, Integer defaultVal) {
        if (StringUtils.isBlank(val)) return defaultVal;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            log.error("String:'{}' to Integer error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert Object to Integer.
     *
     * @param val String
     * @return Integer
     */
    public static Integer obj2Integer(Object val) {
        return obj2Integer(val, null);
    }

    /**
     * Convert Object to Integer with default value.
     *
     * @param val        String
     * @param defaultVal Integer
     * @return Integer
     */
    public static Integer obj2Integer(Object val, Integer defaultVal) {
        if (val == null) return defaultVal;
        try {
            return Integer.parseInt(String.valueOf(val));
        } catch (Exception e) {
            log.warn("Object:'{}' to Integer error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert Object to Integer.
     *
     * @param val String
     * @return Integer
     */
    public static String obj2String(Object val) {
        return obj2String(val, null);
    }

    /**
     * Convert Object to Integer with default value.
     *
     * @param val        String
     * @param defaultVal Integer
     * @return Integer
     */
    public static String obj2String(Object val, String defaultVal) {
        if (val == null) return defaultVal;
        try {
            return String.valueOf(val);
        } catch (Exception e) {
            log.warn("Object:'{}' to String error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert Integer to string with default value.
     *
     * @param val        Integer
     * @param defaultVal String
     * @return String
     */
    public static String int2String(Integer val, String defaultVal) {
        if (val == null) {
            return defaultVal;
        }
        return String.valueOf(val);
    }

    /**
     * Convert String to Boolean.
     *
     * @param val String
     * @return Integer
     */
    public static Boolean str2Boolean(String val) {
        return str2Boolean(val, null);
    }

    /**
     * Convert String to Boolean with default value.
     *
     * @param val        String
     * @param defaultVal Boolean
     * @return Boolean
     */
    public static Boolean str2Boolean(String val, Boolean defaultVal) {
        if (StringUtils.isBlank(val)) return defaultVal;

        try {
            return Boolean.parseBoolean(val);
        } catch (Exception e) {
            log.warn("String:'{}' to Boolean error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert Object to Boolean.
     *
     * @param val String
     * @return Boolean
     */
    public static Boolean obj2Boolean(Object val) {
        return obj2Boolean(val, null);
    }

    /**
     * Convert Object to Boolean with default value.
     *
     * @param val        String
     * @param defaultVal Boolean
     * @return Boolean
     */
    public static Boolean obj2Boolean(Object val, Boolean defaultVal) {
        if (val == null) return defaultVal;
        try {
            return Boolean.parseBoolean(String.valueOf(val));
        } catch (Exception e) {
            log.warn("Object:'{}' to Boolean error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert Boolean to string with default value.
     *
     * @param val        Boolean
     * @param defaultVal String
     * @return String
     */
    public static String bool2String(Boolean val, String defaultVal) {
        if (val == null) {
            return defaultVal;
        }
        return val ? "1" : "0";
    }

    /**
     * Convert Object to Long.
     *
     * @param val String
     * @return Long
     */
    public static Long obj2Long(Object val) {
        return obj2Long(val, null);
    }

    /**
     * Convert Object to Long with default value.
     *
     * @param val        String
     * @param defaultVal Long
     * @return Long
     */
    public static Long obj2Long(Object val, Long defaultVal) {
        if (val == null) return defaultVal;

        try {
            return Long.parseLong(String.valueOf(val));
        } catch (Exception e) {
            log.warn("Object:'{}' to Long error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert String to Float.
     *
     * @param val String
     * @return Float
     */
    public static Float str2Float(String val) {
        return str2Float(val, null);
    }

    /**
     * Convert String to Float with default value.
     *
     * @param val        String
     * @param defaultVal Float
     * @return Float
     */
    public static Float str2Float(String val, Float defaultVal) {
        if (StringUtils.isBlank(val)) return defaultVal;

        try {
            return Float.parseFloat(val);
        } catch (Exception e) {
            log.warn("String:'{}' to Float error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert Object to Float.
     *
     * @param val String
     * @return Float
     */
    public static Float obj2Float(Object val) {
        return obj2Float(val, null);
    }

    /**
     * Convert Object to Float with default value.
     *
     * @param val        String
     * @param defaultVal Float
     * @return Float
     */
    public static Float obj2Float(Object val, Float defaultVal) {
        if (val == null) return defaultVal;
        try {
            return Float.parseFloat(String.valueOf(val));
        } catch (Exception e) {
            log.warn("Object:'{}' to Float error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert String to Double.
     *
     * @param val String
     * @return Double
     */
    public static Double str2Double(String val) {
        return str2Double(val, null);
    }

    /**
     * Convert String to Double with default value.
     *
     * @param val        String
     * @param defaultVal Double
     * @return Double
     */
    public static Double str2Double(String val, Double defaultVal) {
        if (StringUtils.isBlank(val)) return defaultVal;

        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            log.warn("String:'{}' to Double error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert Object to Double.
     *
     * @param val String
     * @return Double
     */
    public static Double obj2Double(Object val) {
        return obj2Double(val, null);
    }

    /**
     * Convert Object to Double with default value.
     *
     * @param val        String
     * @param defaultVal Double
     * @return Double
     */
    public static Double obj2Double(Object val, Double defaultVal) {
        if (val == null) return defaultVal;
        try {
            return Double.parseDouble(String.valueOf(val));
        } catch (Exception e) {
            log.warn("Object:'{}' to Double error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert String to BigDecimal.
     *
     * @param val String
     * @return BigDecimal
     */
    public static BigDecimal str2Decimal(String val) {
        return str2Decimal(val, null);
    }

    /**
     * Convert String to BigDecimal with default value.
     *
     * @param val        String
     * @param defaultVal BigDecimal
     * @return BigDecimal
     */
    public static BigDecimal str2Decimal(String val, BigDecimal defaultVal) {
        if (StringUtils.isBlank(val)) return defaultVal;

        try {
            return new BigDecimal(val);
        } catch (Exception e) {
            log.warn("String:'{}' to BigDecimal error!", val);
        }
        return defaultVal;
    }

    /**
     * Convert Object to BigDecimal.
     *
     * @param val String
     * @return BigDecimal
     */
    public static BigDecimal obj2Decimal(Object val) {
        return obj2Decimal(val, null);
    }

    /**
     * Convert Object to BigDecimal with default value.
     *
     * @param val        String
     * @param defaultVal BigDecimal
     * @return BigDecimal
     */
    public static BigDecimal obj2Decimal(Object val, BigDecimal defaultVal) {
        if (val == null) return defaultVal;
        try {
            return new BigDecimal(String.valueOf(val));
        } catch (Exception e) {
            log.warn("Object:'{}' to BigDecimal error!", val);
        }
        return defaultVal;
    }

    public static Long getOrDefault(Long val, Long defaultVal) {
        return val == null ? defaultVal : val;
    }

}
