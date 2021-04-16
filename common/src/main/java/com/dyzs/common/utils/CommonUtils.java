package com.dyzs.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.io.IOException;

/**
 * ================================================
 * Created by dyzs on 2018/6/26.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */
public class CommonUtils {
    /**
     * TODO 判断网络状态是否可用
     *
     * @return true: 网络可用 ; false: 网络不可用
     */
    public static boolean hasInternet(Context context) {
        ConnectivityManager m = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (m == null) {
            LogUtils.d("NetWorkState", "Unavailable");
            return false;
        } else {
            NetworkInfo[] info = m.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        LogUtils.d("NetWorkState", "Available");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void createFileDir(File file) {
        try {
            if (file.getParentFile().exists()) {
                file.createNewFile();
            } else {
                file.getParentFile().mkdirs();
                createFileDir(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
