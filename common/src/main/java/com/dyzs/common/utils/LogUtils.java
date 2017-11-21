package com.dyzs.common.utils;

import android.util.Log;

/**
 * Created by NKlaus on 2017/11/18.
 */

public class LogUtils{
    private static final String TAG = LogUtils.class.getSimpleName();
    private static boolean isDebugging = true;
    public static void setDebug(boolean b) {
        isDebugging = b;
    }

    public static void v(String tag, String msg) {
        if (isDebugging) {
            Log.v(tag == null ? TAG : tag, msg == null ? "" : msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebugging) {
            Log.d(tag == null ? TAG : tag, msg == null ? "" : msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebugging) {
            Log.i(tag == null ? TAG : tag, msg == null ? "" : msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebugging) {
            Log.w(tag == null ? TAG : tag, msg == null ? "" : msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebugging) {
            Log.e(tag == null ? TAG : tag, msg == null ? "" : msg);
        }
    }
}
