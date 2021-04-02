package com.dyzs.heheda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallWorkService extends Service implements PhoneStateListener.StateImpl {
    private static final String TAG = "CallWrokService";

    private TelephonyManager mTelephonyManager;
    private PhoneStateListener myPhoneStateListener;

    private HandlerThread mHandlerThread;
    private static String mHTName = "HandlerThread";
    private Handler mHandler;
    private Handler mCallPhoneHandler;
    private Handler mEndCallHandler;
    private boolean mTimeDown = true;
    private static final int START_CALL = 0x110;
    private static final int END_CALL = 0x111;
    private static final int TIME_TICK = 0x112;
    private static final int UPLOAD_TOKEN = 0x112;
    private String mCallPhone;
    private int mTotalCount = 0;
    private int currCallTimes = 0;
    private MyBinder myBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        initHandlerThread();
        initPhoneTelephoneManager();
        myBinder = new MyBinder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        mCallPhone = intent.getStringExtra("phone");
        mTotalCount = intent.getIntExtra("countOfTimes", 0);
        currCallTimes = 0;
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind:" + intent.getIntExtra("countOfTimes", 0));
        mCallPhone = intent.getStringExtra("phone");
        mTotalCount = intent.getIntExtra("countOfTimes", 0);
        currCallTimes = 0;
        if (myBinder != null) {
            return myBinder;
        }
        return null;
    }

    private void initPhoneTelephoneManager() {
        mTelephonyManager = (TelephonyManager) getSystemService(Activity.TELEPHONY_SERVICE);
        myPhoneStateListener = new PhoneStateListener();
        myPhoneStateListener.setStateImpl(this);
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    @Override
    public void onCallRinging(String phoneNumber) {
        Date date = new Date(System.currentTimeMillis());
        Log.i(TAG, "电话响铃:" + simpleDateFormat.format(date));
    }

    @Override
    public void onCallOffHook(String phoneNumber) {
        Date date = new Date(System.currentTimeMillis());
        Log.i(TAG, "电话接通:" + simpleDateFormat.format(date));
    }

    @Override
    public void onCallIdle(String phoneNumber) {
        Date date = new Date(System.currentTimeMillis());
        Log.i(TAG, "电话空闲:" + simpleDateFormat.format(date));
        startCallTask();
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH-MM-dd hh:mm:ss", Locale.CHINA);

    private void initHandlerThread() {
        mCallPhoneHandler = new Handler();
        mEndCallHandler = new Handler();
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandlerThread = new HandlerThread(mHTName, Process.THREAD_PRIORITY_DEFAULT);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case START_CALL:
                        callPhone();
                        break;
                    case END_CALL:
                        endCall();
                        break;
                    case TIME_TICK:
                        tickTime++;
                        if (tickTime > 10) {
                            mHandler.sendEmptyMessage(END_CALL);
                            return;
                        }
                        mHandler.sendEmptyMessageDelayed(TIME_TICK, 1000);
                        break;
                }
            }
        };
    }


    private void callPhone() {
        try {
            mCallPhoneHandler.post(new Runnable() {
                @Override
                public void run() {
                    /* main UI */
                    int state = 0;
                    if (mTelephonyManager != null) {
                        state = mTelephonyManager.getCallState();
                    }
                    if (state == 0) {
                        actionCall();
                        startTickTime();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "call phone error:" + e.getMessage());
        }
    }

    private void endCall() {
        mEndCallHandler.post(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                try {
                    Date date = new Date(System.currentTimeMillis());
                    Log.i(TAG, "end call time:" + simpleDateFormat.format(date));
                    TelecomManager tm = (TelecomManager) getApplication().getSystemService(Activity.TELECOM_SERVICE);
                    tm.endCall();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "endCallError:" + e.getMessage());
                }
            }
        });
    }

    public void startCallTask() {
        currCallTimes++;
        if (currCallTimes > mTotalCount) {
            stopCallTask();


            return;
        }
        tickTime = 0;
        mHandler.removeMessages(TIME_TICK);
        mHandler.removeMessages(END_CALL);
        mHandler.sendEmptyMessageDelayed(START_CALL, 1000);
    }

    public void stopCallTask() {
        currCallTimes = 0;
        mHandler.removeMessages(TIME_TICK);
        mHandler.removeMessages(END_CALL);
        mHandler.removeMessages(START_CALL);
    }

    int tickTime = 0;
    public void startTickTime() {
        tickTime = 0;
        mHandler.sendEmptyMessage(TIME_TICK);
    }

    public void actionCall() {
        if (mCallPhone == null)return;
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + mCallPhone);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        mHandlerThread.quitSafely();
        super.onDestroy();
    }

    public static class MyBinder extends Binder {
        private CallWorkService callService;
        public MyBinder(Service service) {
            this.callService = (CallWorkService) service;
        }
        public CallWorkService getService() {
            return callService;
        }

        public void onTaskStar(int count, String phone) {
            callService.mTotalCount = count;
            callService.mCallPhone = phone;
            callService.startCallTask();
        }

        public void onTaskStop() {
            callService.endCall();
            callService.stopCallTask();
        }
    }
}
