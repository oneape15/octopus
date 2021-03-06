package com.oneape.octopus.commons.security;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-02-27 10:37.
 * Modify:
 */
public class RSAUtilsTest {

    @Test
    public void decryptTest() throws Exception {
        Map<String, Object> map = RSAUtils.initKey();
        String priKey = RSAUtils.getPrivateKey(map);
        String pubKey = RSAUtils.getPublicKey(map);
        System.out.println("公钥: " + pubKey);
        System.out.println("密钥: " + priKey);
        String tmp = "hello world";
        byte[] secrtStr = RSAUtils.encryptByPublicKey(tmp.getBytes(), Base64Utils.fromBase64(pubKey));
        byte[] mwArr = RSAUtils.decryptByPrivateKey(secrtStr, Base64Utils.fromBase64(priKey));

        assertTrue(StringUtils.equals(tmp, new String(mwArr)), "RSA加密算法异常");
    }
}
