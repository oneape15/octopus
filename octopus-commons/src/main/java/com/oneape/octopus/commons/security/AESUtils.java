package com.oneape.octopus.commons.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;

/**
 * AES is the most widely used symmetric encryption algorithm,
 * and one of the advantages of AES is that it has not yet been cracked.
 * AES is commonly used for mobile communication system encryption and software based on SSH protocol.
 * Created by oneape<oneape15@163.com>
 * Created 2020-04-30 15:39.
 * Modify:
 */
@Slf4j
public final class AESUtils {
    private final static String password = "octopus_is_data_center";
    private final static Charset charset = StandardCharsets.UTF_8;

    private final static Key key;
    private final static Cipher cipher;

    static {
        try {
            //1. gen key.
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom sr = new SecureRandom(password.getBytes(charset));
            keyGenerator.init(sr);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] byteKey = secretKey.getEncoded();

            //2. change key.
            key = new SecretKeySpec(byteKey, "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * encrypt
     *
     * @param str String
     * @return String  A hexadecimal string.
     */
    public static String encrypt(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("Encryption string is blank.");
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(str.getBytes(charset));

            return ByteArrayToStringUtils.bytes2String(result);
        } catch (Exception e) {
            log.error("AES encrypt fail.", e);
        }
        return null;
    }

    /**
     * decrypt
     *
     * @param str String  A hexadecimal string
     * @return String
     */
    public static String decrypt(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("decryption string is blank.");
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] tmp = ByteArrayToStringUtils.String2bytes(str);
            byte[] result = cipher.doFinal(tmp);
            return new String(result);
        } catch (Exception e) {
            log.error("AES decrypt fail.", e);
        }
        return null;
    }

}
