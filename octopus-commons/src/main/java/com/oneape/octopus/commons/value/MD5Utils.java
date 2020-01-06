package com.oneape.octopus.commons.value;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5Utils {

    // 盐值, 用于混淆md5
    private static final String slat = "*I_think*tomorrow_Will_be_better*";

    public static String getMD5(String str) {
        return getMD5(str, false);
    }

    public static String getMD5(String str, boolean needSlat) {
        String base = str;
        if (needSlat) {
            base += slat;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(base.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                result.append(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00).substring(6));
            }
            return result.toString().toUpperCase();
        } catch (Exception e) {
            //
        }
        return "";
    }

    /**
     * 对拉冬系统用户进行特殊md5加密
     * --> md5( ( md5(password + salt) ).toLowerCase()  + username ).toUpperCase()
     *
     * @param username String
     * @param password String
     * @param salt     String
     * @return String
     */
    public static String saltUserPassword(String username, String password, String salt) {
        String md5_1;
        if (StringUtils.isBlank(salt)) {
            md5_1 = getMD5(password);
        } else {
            md5_1 = getMD5(password + salt);
        }
        return getMD5(md5_1.toLowerCase() + username);
    }
}
