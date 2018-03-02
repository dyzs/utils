package com.dyzs.app;

import com.dyzs.base.BaseApplication;
import com.umeng.commonsdk.UMConfigure;

/**
 * @author maidou, created on 2018/3/2.
 */

public class UtilsApplication extends BaseApplication {
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
    }
}
