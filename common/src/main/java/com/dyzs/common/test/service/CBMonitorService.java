package com.dyzs.common.test.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * Created by NKlaus on 2017/11/18.
 */

public class CBMonitorService extends Service{
    private ClipboardManager mClipboardManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mClipboardManager = (ClipboardManager) getSystemService(Service.CLIPBOARD_SERVICE);

        System.out.println("CBMonitorService......");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep(1000);
                    System.out.println("CBMonitorService...Thread...");
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
