package com.dyzs.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.dyzs.common.utils.LogUtils;

/**
 * Created by maidou on 2017/12/13.
 */

public class ScreenUnLockMonitorService extends Service{
    private static final String TAG = ScreenUnLockMonitorService.class.getSimpleName();
    private ScreenUnLockReceiver screenUnLockReceiver;
    private MyBinder binder;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new MyBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i(TAG, "on start command");
        registerScreenActionReceiver();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void registerScreenActionReceiver(){
        screenUnLockReceiver = new ScreenUnLockReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(screenUnLockReceiver, filter);
    }

    public class ScreenUnLockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_ON:
                    LogUtils.i(TAG, "ACTION_SCREEN_ON...");
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    LogUtils.i(TAG, "ACTION_SCREEN_OFF...");
                    break;
                case Intent.ACTION_USER_PRESENT:
                    LogUtils.i(TAG, "ACTION_USER_PRESENT...");
                    break;
            }
        }
    }

    public class MyBinder extends Binder {
        public ScreenUnLockMonitorService getService() {
            return ScreenUnLockMonitorService.this;
        }

        public String getScreenAction(Intent intent) {
            return intent.getAction();
        }
    }

    public static class MyConn implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.i(TAG, "my conn...service connected");
            MyBinder binder = (MyBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.i(TAG, "my conn...service dis connected");
        }
    }
}
