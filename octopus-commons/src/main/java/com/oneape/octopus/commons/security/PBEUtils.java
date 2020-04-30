package com.oneape.octopus.commons.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;

/**
 * PBE（Password Based Encryption 基于口令加密）算法结合了消息摘要算法和对称加密算法的优点。
 * 但是口令还是有可能被穷举出来，所以一般通过加盐salt的方式进行加密。PBE算法只是对已有算法进行了包装，通常有JDK和BC 两种实现。
 * Created by oneape<oneape15@163.com>
 * Created 2020-04-30 16:48.
 * Modify:
 */
@Slf4j
public final class PBEUtils {
    // 固定的盐值
    private final static byte[] salt     = "octopus!".getBytes();
    // 这个是加密用的口令
    private final static String password = "octopus_is_very_gooooood";

    private static Key              key;
    private static PBEParameterSpec parameterSpec;
    private static Cipher           cipher;

    static {
        try {
            //2.2 把密码转换成秘钥
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHMD5andDES");
            key = factory.generateSecret(pbeKeySpec);
            parameterSpec = new PBEParameterSpec(salt, 100);//100是你选择迭代的次数
            cipher = Cipher.getInstance("PBEWITHMD5andDES");
        } catch (Exception e) {
            log.error("生成PBE秘钥失败", e);
        }
    }

    /**
     * 加密
     *
     * @param str 明文字符串
     * @return String 十六进制加密字符串
     */
    public static String encrypt(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("加密字符串不能为空");
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] result = cipher.doFinal(str.getBytes());
            return ByteArrayToStringUtils.bytes2String(result);
        } catch (Exception e) {
            log.error("PBE加密字符串失败", e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param str String 十六进制字符串
     * @return String 明文字符串
     */
    public static String decrypt(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("解密字符串不能为空");
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            byte[] tmp = ByteArrayToStringUtils.String2bytes(str);
            byte[] result = cipher.doFinal(tmp);

            return new String(result);
        } catch (Exception e) {
            log.error("PBE解密失败", e);
        }
        return null;
    }
}
