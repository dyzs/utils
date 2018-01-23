package com.dyzs.common.test.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;

import com.dyzs.common.R;
import com.dyzs.common.utils.LogUtils;

/**
 * Created by NKlaus on 2017/11/18.
 */

public class CBMonitorService extends Service{
    private static final String TAG = CBMonitorService.class.getSimpleName();
    private ClipboardManager mClipboardManager;
    private HandlerThread mHandlerThread;
    private static String mHTName = "CBMonitorServiceHandlerThread";
    private Handler mHandler;
    private Handler mMainUIHandler;
    private boolean mTimeDown = true;
    private static int TIME_DOWNING = 0x110;
    @Override
    public void onCreate() {
        super.onCreate();

        mMainUIHandler = new Handler();

        initHandleThread();

        // mHandler.sendEmptyMessage(TIME_DOWNING);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initClipBoard();
        initScreenService();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mClipboardManager.addPrimaryClipChangedListener(null);
        super.onDestroy();
    }

    private void initClipBoard() {
        mClipboardManager = (ClipboardManager) getSystemService(Service.CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                if (mClipboardManager.hasPrimaryClip() && mClipboardManager.getPrimaryClip().getItemCount() > 0) {
                    CharSequence copyText = mClipboardManager.getPrimaryClip().getItemAt(0).getText();
                    if (copyText != null) {
                        /* send a notification message */
                        LogUtils.i(TAG, "copied text: " + copyText);
                        sendThisNotification();
                        /*Intent intent = new Intent();
                        intent.setAction("com.dyzs.common.action.TEST");
                        intent.setPackage(CBMonitorService.this.getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/
                    }
                }
            }
        });
    }

    private void initHandleThread() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandlerThread = new HandlerThread(mHTName, Process.THREAD_PRIORITY_DEFAULT);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                myTimeLooper();
                if (mTimeDown) {
                    mHandler.sendEmptyMessageDelayed(TIME_DOWNING, 2500);
                }
            }
        };
    }

    private int i = 0;
    private void myTimeLooper() {
        try {
            mMainUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    /* main UI */
                    System.out.println(TAG + " update ui.....");
                    i ++;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.getMessage());
        }
    }

    private void sendThisNotification() {
        Intent intent = new Intent();
        intent.setAction("com.dyzs.common.action.TEST");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage(CBMonitorService.this.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(CBMonitorService.this, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(CBMonitorService.this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle("familiar");
        builder.setContentText("content text");
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setAutoCancel(true);
        Notification notification = builder.build();

        NotificationManager nm = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        nm.notify(i, notification);
    }

    private void initScreenService() {
        Intent intent = new Intent(CBMonitorService.this, ScreenUnLockMonitorService.class);
        // CBMonitorService.this.bindService(intent, new ScreenUnLockMonitorService.MyConn(), Context.BIND_AUTO_CREATE);
        CBMonitorService.this.startService(intent);
    }
}
