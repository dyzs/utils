package com.dyzs.review.designpattern.test;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * @author maidou, created on 2018/3/16.
 */

public class EncryptTest {

    public static final String DATA = "helloworld";

    public static void main(String[] args) throws Exception {

        Map<String, Object> map = EncryptUtils.genRSAKey();
        RSAPrivateKey rsaPrivateKey = EncryptUtils.getPrivateKey(map);
        RSAPublicKey rsaPublicKey = EncryptUtils.getPublicKey(map);

        byte[] resultBytes = EncryptUtils.encryptRSA(DATA.getBytes(), rsaPublicKey);


        System.out.println("RSA Encrypt : " + EncryptUtils.bytesToHexString(resultBytes));

        byte[] plainBytes = EncryptUtils.decryptRSA(resultBytes, rsaPrivateKey);

        System.out.println("RES Plain : " + new String(plainBytes));


    }
}
