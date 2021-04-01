package com.oneape.octopus.commons.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;

/**
 * PBE（Password Based Encryption）
 * Created by oneape<oneape15@163.com>
 * Created 2020-04-30 16:48.
 * Modify:
 */
@Slf4j
public final class PBEUtils {
    // Fixed salt value.
    private final static byte[] salt = "octopus!".getBytes();
    // This is the password for encryption.
    private final static String password = "octopus_is_very_gooooood";
    // Number of iterations
    private final static Integer iterTime = 50;

    private final static String secretKey = "PBEWITHMD5andDES";

    private final static Key key;
    private final static PBEParameterSpec parameterSpec;
    private final static Cipher cipher;

    static {
        try {
            // Convert the password to a secret key.
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKey);
            key = factory.generateSecret(pbeKeySpec);
            parameterSpec = new PBEParameterSpec(salt, iterTime);
            cipher = Cipher.getInstance(secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * encrypt
     *
     * @param str Plaintext string
     * @return String A hexadecimal string
     */
    public static String encrypt(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("The encryption string is blank.");
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] result = cipher.doFinal(str.getBytes());
            return ByteArrayToStringUtils.bytes2String(result);
        } catch (Exception e) {
            log.error("PBE encryption string failed.", e);
        }
        return null;
    }

    /**
     * decrypt
     *
     * @param str String A hexadecimal string
     * @return String Plaintext string
     */
    public static String decrypt(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("The decryption string is blank.");
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            byte[] tmp = ByteArrayToStringUtils.String2bytes(str);
            byte[] result = cipher.doFinal(tmp);

            return new String(result);
        } catch (Exception e) {
            log.error("PBE decryption string failed.", e);
        }
        return null;
    }
}
