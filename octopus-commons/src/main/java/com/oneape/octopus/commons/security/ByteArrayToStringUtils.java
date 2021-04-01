package com.oneape.octopus.commons.security;

import org.apache.commons.lang3.StringUtils;

/**
 * Byte array to String.
 * Created by oneape<oneape15@163.com>
 * Created 2020-02-27 10:05.
 * Modify:
 */
public class ByteArrayToStringUtils {
    /**
     * The byte array is replaced with a string.
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
     * A hexadecimal string is converted to a byte array.
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
        // Since it is hexadecimal, it takes up only four bits at most,
        // and the conversion to bytes requires two hexadecimal characters, with the higher order coming first.
        for (int i = 0; i < byteArray.length; i++) {
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }
}
