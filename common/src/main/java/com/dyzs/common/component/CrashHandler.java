package com.dyzs.common.component;

import android.content.Context;

import com.dyzs.common.utils.LogUtils;
import com.dyzs.common.utils.ToastUtils;

/**
 * @author dyzs, created on 2018/3/25.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = CrashHandler.class.getSimpleName();

    private CrashHandler() {

    }

    private static CrashHandler instance = null;

    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    public void init(Context ctx){  //初始化，把当前对象设置成UncaughtExceptionHandler处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        LogUtils.d(TAG, "uncaughtException, thread: " + thread
                + " name: " + thread.getName() + " id: " + thread.getId() + "exception: "
                + throwable);
        String threadName = thread.getName();
        if ("sub1".equals(threadName)){
            LogUtils.d(TAG, "");
        }
    }
}
