package com.dyzs.heheda.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.dyzs.heheda.PhoneStateListener;
import com.dyzs.heheda.TamakoUtils;
import com.dyzs.heheda.ThreadPoolUtils;
import com.dyzs.heheda.runnable.WriteLogRunnable;

import java.lang.ref.WeakReference;
import java.util.List;

public class CallWorkManager implements PhoneStateListener.StateImpl {
    private static final String TAG = "CallWorkManager";
    public static final String ACTION_SEND_TOKEN = "SendCkToken";

    private TelephonyManager mTelephonyManager;
    private PhoneStateListener myPhoneStateListener;

    private HandlerThread mHandlerThread;
    private static String mHTName = "HandlerThread";
    private Handler mHandler;
    private Handler mUIOperatorHandler;
    private Handler mEndCallHandler;
    private boolean mTimeDown = true;
    private static final int START_CALL = 0x110;
    private static final int END_CALL = 0x111;
    private static final int TIME_TICK = 0x112;
    private static final int SEND_TOKEN = 0x113;
    private String mCallPhone;
    private int mTotalCount = 0;
    private int mCurrCallTimes = 0;
    private int mTickTime = 0;
    private int mTotalTickTimes = 10;
    private long mSendTokenDelayTime = 500;

    private WeakReference<Activity> mActivity;
    private ICallback iCallback;

    public CallWorkManager(Activity activity) {
        Log.i(TAG, "onCreate");
        mActivity = new WeakReference<>(activity);
        initHandlerThread();
        initPhoneTelephoneManager();
    }

    public CallWorkManager setICallback(ICallback iCallback) {
        this.iCallback = iCallback;
        return this;
    }

    private void initPhoneTelephoneManager() {
        mTelephonyManager = (TelephonyManager) mActivity.get().getSystemService(Activity.TELEPHONY_SERVICE);
        myPhoneStateListener = new PhoneStateListener();
        myPhoneStateListener.setStateImpl(this);
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    @Override
    public void onCallRinging(String phoneNumber) {
        Log.i(TAG, "电话响铃:" + TamakoUtils.getCurrentTime());
        LogManager.getInstance().addLog(new LogManager.MyLog(null, "电话响铃:["+TamakoUtils.getCurrentTime()+"]"));
    }

    @Override
    public void onCallOffHook(String phoneNumber) {
        Log.i(TAG, "电话接通:" + TamakoUtils.getCurrentTime());
        LogManager.getInstance().addLog(new LogManager.MyLog(null, "接通时间:["+TamakoUtils.getCurrentTime()+"]"));
    }

    @Override
    public void onCallIdle(String phoneNumber) {
        Log.i(TAG, "电话空闲:" + TamakoUtils.getCurrentTime());
        startCallTask();
    }

    private void initHandlerThread() {
        mUIOperatorHandler = new Handler();
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
                        sendCKToken();
                        callPhone();
                        break;
                    case END_CALL:
                        endCall();
                        break;
                    case TIME_TICK:
                        mTickTime++;
                        if (mTickTime >= mTotalTickTimes) {
                            mHandler.sendEmptyMessage(END_CALL);
                            return;
                        }
                        mHandler.sendEmptyMessageDelayed(TIME_TICK, 1000);
                        break;
                }
            }
        };
    }

    public void sendCKToken() {
        mUIOperatorHandler.removeCallbacks(mSendCKToken);
        mUIOperatorHandler.post(mSendCKToken);
    }

    private Runnable mSendCKToken = new Runnable() {
        @Override
        public void run() {
            if (iCallback != null)iCallback.sendCKToken();
        }
    };

    private void writeLogBeforeCall() {
        List<String> logList = LogManager.getInstance().getWriteListAndClearCache();
        ThreadPoolUtils.getThreadPoolExecutorForWriteLog(100).execute(new WriteLogRunnable(logList, LogManager.getInstance().getLogName()));
    }

    private void callPhone() {
        try {
            writeLogBeforeCall();
            mUIOperatorHandler.removeCallbacks(mCallPhoneRunnable);
            mUIOperatorHandler.post(mCallPhoneRunnable);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "call phone error:" + e.getMessage());
        }
    }

    private Runnable mCallPhoneRunnable = new Runnable() {
        @Override
        public void run() {
            /* main UI */
            Log.i(TAG, "send call phone action");
            int state = 0;
            if (mTelephonyManager != null) {
                state = mTelephonyManager.getCallState();
            }
            if (state == 0) {
                LogManager.getInstance().addLog(new LogManager.MyLog(null, "拨号时间:["+TamakoUtils.getCurrentTime()+"]"));
                actionCall();
                startTickTime();
            }
        }
    };

    private Runnable mHangUpRunnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.P)
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            try {
                Log.i(TAG, "hang up time:" + TamakoUtils.getCurrentTime());
                LogManager.getInstance().addLog(new LogManager.MyLog(null, "挂断时间:["+TamakoUtils.getCurrentTime()+"]"));
                TelecomManager tm = (TelecomManager) mActivity.get().getSystemService(Activity.TELECOM_SERVICE);
                tm.endCall();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "endCallError:" + e.getMessage());
            }
        }
    };

    public void startRecordLog() {
        String logName = "SendUdp_" + TamakoUtils.getTimeForFileName();
        LogManager.getInstance().preparedForStart(logName);
        LogManager.getInstance().addLog(new LogManager.MyLog(null, "===================开始日志==================="));
    }

    public void endRecordLog() {
        LogManager.getInstance().addLog(new LogManager.MyLog(null, "===================结束日志==================="));
        List<String> logList = LogManager.getInstance().getWriteListAndClearCache();
        ThreadPoolUtils.getThreadPoolExecutorForWriteLog(100).execute(new WriteLogRunnable(logList, LogManager.getInstance().getLogName()));
    }

    public void resetCallStateListenImpl(boolean reset) {
        if (reset) {
            myPhoneStateListener.setStateImpl(this);
        } else {
            myPhoneStateListener.setStateImpl(null);
        }
    }

    public void endCall() {
        mUIOperatorHandler.removeCallbacks(mHangUpRunnable);
        mUIOperatorHandler.post(mHangUpRunnable);
    }

    public void startCallTask() {
        mCurrCallTimes++;
        if (mCurrCallTimes > mTotalCount) {
            stopCallTask();
            return;
        }
        mTickTime = 0;
        mHandler.removeMessages(SEND_TOKEN);
        mHandler.removeMessages(TIME_TICK);
        mHandler.removeMessages(END_CALL);
        mHandler.removeMessages(START_CALL);
        mHandler.sendEmptyMessage(SEND_TOKEN);
    }

    public void stopCallTask() {
        mCurrCallTimes = 0;
        mHandler.removeMessages(SEND_TOKEN);
        mHandler.removeMessages(TIME_TICK);
        mHandler.removeMessages(END_CALL);
        mHandler.removeMessages(START_CALL);
        endRecordLog();
    }

    public void startTickTime() {
        mTickTime = 0;
        mHandler.sendEmptyMessage(TIME_TICK);
    }

    public void actionCall() {
        if (mCallPhone == null)return;
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + mCallPhone);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(data);
        mActivity.get().startActivity(intent);
    }

    public void onDestroy() {
        mHandlerThread.quitSafely();
    }

    public void resetParam(int callTimes, String callPhone) {
        this.mTotalCount = callTimes;
        this.mCallPhone = callPhone;
    }

    public void resetParam(int callTimes, String callPhone, int totalTickTime) {
        this.mTotalCount = callTimes;
        this.mCallPhone = callPhone;
        this.mTotalTickTimes = totalTickTime;
    }

    public interface ICallback {
        void sendCKToken();
    }
}
