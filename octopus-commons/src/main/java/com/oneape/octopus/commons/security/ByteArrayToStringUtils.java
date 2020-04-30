package com.oneape.octopus.commons.security;

import org.apache.commons.lang3.StringUtils;

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
    public static String bytes2String(byte[] byteArray) {
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

    /**
     * 十六进制字符串转换成byte数组
     *
     * @param hexString String
     * @return byte[]
     */
    public static byte[] String2bytes(String hexString) {
        if (StringUtils.isEmpty(hexString))
            throw new IllegalArgumentException("this hexString must not be empty");

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        //因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
        for (int i = 0; i < byteArray.length; i++) {
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }
}
