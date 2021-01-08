package com.example.aes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.bouncycastle.util.encoders.Base64;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密工具类
 * Created by shuzheng on 2017/2/5.
 */
public class AESUtil {

    public static String logger = AESUtil.class.getSimpleName();

    public static final String KEY_ALGORITHM = "AES";

    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";

    private static final String encodeRules = "zheng";

    public static final String PASSWORD = "weWay!123456";

    public static final String key = "weWay!123456weWay!123456weWay!12";

    public static byte[] initkey() throws Exception {
        return new byte[]{0x77, 0x65, 0x57, 0x61, 0x79, 0x21, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36
                , 0x77, 0x65, 0x57, 0x61, 0x79, 0x21, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36
                , 0x77, 0x65, 0x57, 0x61, 0x79, 0x21, 0x31, 0x32
        };
    }


    public static String AESDecode(String content) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(encodeRules.getBytes());
            keygen.init(128, random);
            SecretKey original_key = keygen.generateKey();
            byte[] raw = original_key.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] byte_content =  Base64.decode(content);

            byte[] byte_decode = cipher.doFinal(byte_content);
            return new String(byte_decode, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {

            //e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        //如果有错就返加nulll
        return null;
    }

    public static Key toKey(byte[] key) throws Exception {

        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }

    private static byte[] encrypt(byte[] data) {
        try {
            Key k = toKey(initkey());

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = null;
            cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES256 解密
     *
     * @param data
     * @return
     */
    public static byte[] decrypt(byte[] data) {
        try {
            Key k = toKey(initkey());
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, k);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES256 加密 BASE64
     * @param content
     * @return
     */
    public static String encryptBase64(String content) {
        //TODO
        if (content == null || "".endsWith(content)) {
            return null;
        }
        byte[] result = encrypt(content.getBytes());
        //TODO
        return Base64.toBase64String(result);
        //return new BASE64Encoder().encode(result);
        //return Base64.getEncoder().encodeToString(result);
        //return Base64();
    }

    /**
     * AES256 解密
     *
     * @param cryptograph
     * @return
     */
    public static String decryptBase64(String cryptograph) {
        if (cryptograph == null) {
            return null;
        }
        byte[] decode = Base64.decode(cryptograph);
        byte[] decrypt = decrypt(decode);
        if (decrypt == null)return null;
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    public static String createEncryptStrV2(String parameterJsonStr){
        JSONObject parameter = JSON.parseObject(parameterJsonStr);
        JSONObject jsonObject = new JSONObject();
        try {
            parameter.put("appKey", "10001");
            parameter.put("appSecret", "appSecretOrAuthCode");
            parameter.put("packageName", "com.knocknock.android");
            String nonceStr = getNonceStr();
            jsonObject.put("phone", "18503083618");
            jsonObject.put("nonceStr", nonceStr);
            jsonObject.put("sign", getSign(parameter, nonceStr));
            jsonObject.put("params", parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AESUtil.encryptBase64(jsonObject.toString());
    }

    public static String createEncryptStr(JSONObject parameter){
        JSONObject jsonObject = new JSONObject();
        try {
            parameter.put("appKey", "10001");
            parameter.put("appSecret", "appSecretOrAuthCode");
            parameter.put("packageName", "com.knocknock.android");
            String nonceStr = getNonceStr();
            jsonObject.put("phone", "18503083618");
            jsonObject.put("nonceStr", nonceStr);
            jsonObject.put("sign", getSign(parameter, nonceStr));
            jsonObject.put("params", parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AESUtil.encryptBase64(jsonObject.toString());
    }

    /*public static String createNerGet(JsonObject parameter){
        JsonObject jsonObject = new JsonObject();
        try {
            parameter.addProperty("appKey", "10001");
            parameter.addProperty("appSecret", "appSecretOrAuthCode");
//            if(!TextUtils.isEmpty(BaseApplication.userId)&&!parameter.has("userId"))  parameter.addProperty("userId", BaseApplication.userId);
            String nonceStr = getNonceStr();
            jsonObject.addProperty("nonceStr", nonceStr);
            jsonObject.addProperty("sign", getSign(parameter, nonceStr));
            jsonObject.add("params", parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AESUtil.encryptBase64(jsonObject.toString());
    }*/

    public static String getNonceStr() {
        return UUID.randomUUID().toString().replace("-", "").substring(2, 8);
    }

    public static String getSign(JSONObject jsonObject, String secret) {
        Map<String, String> map = json2Map(jsonObject);
        StringBuilder sb = new StringBuilder();
        List<String> paramNames = new ArrayList<String>(jsonObject.size());
        paramNames.addAll(map.keySet());
        sb.append(secret);
        Collections.sort(paramNames);
        for (String paramName : paramNames) {
            sb.append(paramName).append("=").append(map.get(paramName));
            sb.append("&");
        }
        String returnStr = sb.length() > 1 ? sb.substring(0, sb.length() - 1) : "";
        returnStr = MD5Util.md5(returnStr);
        return returnStr;
    }

    public static Map<String, String> json2Map(JSONObject jsonObj) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        return gson.fromJson(jsonObj.toString(), mapType);
    }

}
