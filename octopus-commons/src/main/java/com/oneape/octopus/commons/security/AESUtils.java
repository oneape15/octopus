package com.oneape.octopus.commons.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.SecureRandom;

/**
 * AES使用的最多的对称加密算法，AES的优势之一是至今尚未被破解。AES通常用于移动通信系统加密以及基于SSH协议的软件.
 * Created by oneape<oneape15@163.com>
 * Created 2020-04-30 15:39.
 * Modify:
 */
@Slf4j
public final class AESUtils {
    private final static String  password = "octopus_is_data_center";
    private final static Charset charset  = Charset.forName("UTF-8");

    private static Key    key;
    private static Cipher cipher;

    static {
        try {

            //1.生成KEY
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom sr = new SecureRandom(password.getBytes(charset));
            keyGenerator.init(sr);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] byteKey = secretKey.getEncoded();
            log.info("byteKey: {}", ByteArrayToStringUtils.bytes2String(byteKey));

            //2.转换KEY
            key = new SecretKeySpec(byteKey, "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (Exception e) {
            log.error("AES算法实例化失败", e);
        }
    }

    /**
     * 加密
     *
     * @param str String
     * @return String 十六进制字符串
     */
    public static String encode(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("加密字符串不能为空");
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(str.getBytes(charset));

            return ByteArrayToStringUtils.bytes2String(result);
        } catch (Exception e) {
            log.error("加密字符串异常", e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param str String 十六进制字符串
     * @return String
     */
    public static String decode(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("解密字符串不能为空");
        }
        try {
            //4.解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] tmp = ByteArrayToStringUtils.String2bytes(str);
            byte[] result = cipher.doFinal(tmp);
            return new String(result);
        } catch (Exception e) {
            log.error("解密操作失败", e);
        }
        return null;
    }

}
