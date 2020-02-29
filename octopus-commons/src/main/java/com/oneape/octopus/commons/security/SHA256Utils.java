package com.oneape.octopus.commons.security;

import java.security.MessageDigest;

/**
 * sha256加密工具类
 * Created by oneape<oneape15@163.com>
 * Created 2020-02-27 10:08.
 * Modify:
 */
public class SHA256Utils {
    public static final String KEY_SHA_256 = "SHA-256";

    /**
     * sha256算法
     *
     * @param before String
     * @return String
     */
    public static String sha256(String before) {
        MessageDigest messageDigest;
        String after = "";
        try {
            messageDigest = MessageDigest.getInstance(KEY_SHA_256);
            messageDigest.update(before.getBytes());
            after = ByteArrayToStringUtils.byteArrayToString(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return after;
    }
}
