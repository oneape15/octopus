package com.oneape.octopus.commons.security;

/**
 * byte数组转为字符串
 * Created by oneape<oneape15@163.com>
 * Created 2020-02-27 10:05.
 * Modify:
 */
public class ByteArrayToStringUtils {
    /**
     * 下面这个函数用于将字节数组换成成字符串
     *
     * @param byteArray byte[]
     * @return String
     */
    public static String byteArrayToString(byte[] byteArray) {
        StringBuilder builder = new StringBuilder();
        String temp;
        for (byte b : byteArray) {
            temp = Integer.toHexString(b & 0xFF);
            if (temp.length() == 1) {
                builder.append("0");
            }
            builder.append(temp);
        }
        return builder.toString();
    }
}
