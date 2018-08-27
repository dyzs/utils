package com.dyzs.common.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by dyzs on 2018/8/27.
 *
 */

public class SignatureUtils {


    /**
     * 方法用途: 对所有传入参数按照字段名的Unicode码从小到大排序（字典序），并且生成url参数串<br>
     * @param nodeKey key
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static String createMultiNodesSignature(Map<String, String> paraMap, String nodeKey) {
        String buff = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<>(paraMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, (o1, o2) -> (o1.getKey()).compareTo(o2.getKey()));
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (!TextUtils.isEmpty(item.getKey()) && !TextUtils.isEmpty(item.getValue())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    val = URLEncoder.encode(val, "UTF-8");
                    buf.append(key).append("=").append(val);
                    buf.append("&");
                }
            }
            buf.append("key").append("=").append(nodeKey);
            buff = buf.toString();
            if (!buff.isEmpty()) {
                buff = buff.substring(0, buff.length() - 1);
            }
            // step2: md5运算
            buff = encodeToMD5(buff);
            // step3: 转大写, 得到 signValue
            buff = buff.toUpperCase();
            // step4: 截取第 8 到第 24 位
            buff = buff.substring(7, 24);
        } catch (Exception e) {
            return buff;
        }
        return buff;
    }

    public static Map<String, String> json2Map(JSONObject jsonObj) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        return gson.fromJson(jsonObj.toString(), mapType);
    }


    /**
     * MD5
     */
    public static String encodeToMD5(String string) {
        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
