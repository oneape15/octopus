package com.oneape.octopus.commons.value;

import org.apache.commons.lang3.StringUtils;

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

    public static void main(String[] args) {
        String tmp = "2.14";
        System.out.println(DataUtils.isInteger(tmp));
    }
}
