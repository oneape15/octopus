package com.oneape.octopus.commons.security;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * base64 tools
 * Created by oneape<oneape15@163.com>
 * Created 2020-02-27 10:02.
 * Modify:
 */
public class Base64Utils {
    private static final Charset utf8 = StandardCharsets.UTF_8;

    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * Encrypt the byte array as a Base64 byte array.
     *
     * @param before byte[]
     * @return String
     */
    public static String toBase64(byte[] before) {
        byte[] after = encoder.encode(before);
        return new String(after, utf8);
    }

    /**
     * Decode the cipherText.
     *
     * @param cipher String
     * @return byte[]
     */
    public static byte[] fromBase64(String cipher) {
        if (StringUtils.isBlank(cipher)) {
            return null;
        }
        byte[] bits = cipher.getBytes(utf8);

        return decoder.decode(bits);
    }

    /**
     * Decode the cipherText.
     *
     * @param cipher String
     * @return byte[]
     */
    public static String decodeCipherText(String cipher) {
        if (StringUtils.isBlank(cipher)) {
            return null;
        }
        byte[] bits = cipher.getBytes(utf8);

        return new String(decoder.decode(bits), utf8);
    }
}
