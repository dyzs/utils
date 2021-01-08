//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ww.libtest;


//import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class MD5Util {
    private static final int HEX_VALUE_COUNT = 16;

    public MD5Util() {
    }

    public static String md5(String value) {
        if (value != null && !"".equals(value)) {
            return getMD5(value.getBytes());
        }
        return "";
    }

    public static String getMD5(byte[] bytes) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] str = new char[32];

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] tmp = md.digest();
            int k = 0;

            for (int i = 0; i < 16; ++i) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return new String(str);
    }

    public static String getMD5(String value, String encode) {
        String result = "";

        try {
            result = getMD5(value.getBytes(encode));
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

        return result;
    }
}
