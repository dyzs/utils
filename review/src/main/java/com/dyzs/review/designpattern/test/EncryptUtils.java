package com.dyzs.review.designpattern.test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * @author maidou, created on 2018/3/16.
 *
 *
 *
 *
 */

public class EncryptUtils {


    private static final String PUBLIC_KEY = "public_key";
    private static final String PRIVATE_KEY = "private_key";

    /**
     * 生成 RSA 密钥
     * RSA 算法中的密钥长度是非常长的，介于 512 - 65536 之间（JDK 中默认长度是1024），但是必须是64 的倍数。
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genRSAKey() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> map = new HashMap<>();
        map.put(PUBLIC_KEY, rsaPublicKey);
        map.put(PRIVATE_KEY, rsaPrivateKey);

        return map;

    }


    /**
     * 利用公钥进行加密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptRSA(byte[] data, RSAPublicKey key) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] resultBytes = cipher.doFinal(data);
        return resultBytes;
    }

    /**
     * 利用私钥解密
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptRSA(byte[] src, RSAPrivateKey key) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainBytes = cipher.doFinal(src);
        return plainBytes;
    }

    public static RSAPrivateKey getPrivateKey(Map<String, Object> map) {
        return (RSAPrivateKey) map.get(PRIVATE_KEY);
    }

    public static RSAPublicKey getPublicKey(Map<String, Object> map) {
        return (RSAPublicKey) map.get(PUBLIC_KEY);
    }


    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
