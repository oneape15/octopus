package com.oneape.octopus.commons.value;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DataUtils {
    /**
     * 判断字符串是否为Integer
     *
     * @param str String
     * @return boolean true - 是数字字符串， false - 不是
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

    public static Long toLong(String obj, Long defaultVal) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
            log.debug("String2Long 失败, value: {}", obj);
            return defaultVal;
        }
    }
}
