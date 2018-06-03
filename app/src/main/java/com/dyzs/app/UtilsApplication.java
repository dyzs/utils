package com.dyzs.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.dyzs.common.component.CrashHandler;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.commonsdk.UMConfigure;

/**
 * ================================================
 * Created by dyzs on 2018/3/2.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */

public class UtilsApplication extends MultiDexApplication {
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {

        super.onCreate();

        /**
         * 初始化 umeng
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);

        /**
         * 设置日志加密
         * 参数：boolean 默认为false（不加密）
         */
        UMConfigure.setEncryptEnabled(true);

        //在 Application 里面设置我们的异常处理器为 UncaughtExceptionHandler 处理器
        //CrashHandler.getInstance().init(getApplicationContext());

        refWatcher = setupLeakCanary();

    }

    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        UtilsApplication leakApplication = (UtilsApplication) context.getApplicationContext();
        return leakApplication.refWatcher;
    }
}
