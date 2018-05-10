package com.dyzs.app.presenter;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

import com.dyzs.app.view.ICompassServantView;
import com.dyzs.base.BasePresenter;
import com.dyzs.base.IBaseView;

/**
 * @author akuma
 * Created on 2018/1/27.
 */

public class CompassServantPresenter extends BasePresenter{
    private HandlerThread mHandlerThread;
    private String mHtName = "compass_servant";
    private Handler mLooper;
    private Handler mUIHandler;
    private static int MESSAGE = 0x110;
    private ICompassServantView iView;
    public CompassServantPresenter(IBaseView baseView) {
        super(baseView);
        iView = (ICompassServantView) baseView;
    }

    public void initHandlerThread() {
        mUIHandler = new Handler();
        mHandlerThread = new HandlerThread(mHtName, Process.THREAD_PRIORITY_DEFAULT);
        mHandlerThread.start();
        mLooper = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MESSAGE && i > 0) {
                    doWithMainUI();
                    i--;
                }
            }
        };
    }

    private int i = 1000;
    private void doWithMainUI() {
        try {
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    Double d = Math.random() * 89;
                    int iRandom = d.intValue() + 30;
                    iView.setCSPointerDecibel(iRandom);
                }
            });
        } catch (Exception e) {

        }
    }

    public HandlerThread getHandlerThread() {
        return mHandlerThread;
    }

    public void startTension() {
        mLooper.sendEmptyMessage(MESSAGE);
    }
}
