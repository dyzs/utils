package com.dyzs.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by NKlaus on 2017/11/25.
 */

public class AppUtils {
    private static Context mCcontext;
    private static TelephonyManager mTelephonyManager;
    public static void init(Context ctx) {
        mCcontext = ctx;
        mTelephonyManager = (TelephonyManager) mCcontext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static String getVersionName() {
        String versionName = "";
        try {
            PackageManager pm = mCcontext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mCcontext.getPackageName(), 0);
            versionName = pi.versionName == null ? "" : pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return versionName;
        }
    }

    public static String getDeviceId() {
        String deviceId = null;
        try {
            deviceId = mTelephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return deviceId;
        }
    }

    public static String getDeviceInfo() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("SdkVersion = " + Build.VERSION.SDK_INT);
            sb.append(",ReleaseVersion = " + Build.VERSION.RELEASE);
            sb.append(",Mode = " + Build.MODEL);
            sb.append(",Manufacturer = " + Build.MANUFACTURER);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return sb.toString();
        }
    }
}
