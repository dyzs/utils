package com.dyzs.base;

import android.os.Handler;
import android.os.Message;

import com.dyzs.common.utils.LogUtils;

/**
 * ================================================
 * Created by dyzs on 2018/1/27.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */

public class BasePresenter<V extends IBaseView> implements IPresenter {
    private static String TAG = "BasePresenter";
    private V iBaseView;
    private static final int MESSAGE_GOT_ERROR = -110;
    public BasePresenter(V baseView) {
        this.iBaseView = baseView;
    }

    public BasePresenter(V baseView, String handlerThreadName) {
        this.iBaseView = baseView;

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            processMainHandleMessage(msg);
            return false;
        }
    });


    protected void sendMainHandlerMessage(int what, Object obj) {
        Message msg= mHandler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }

    /**
     * @param msg
     */
    protected void processMainHandleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_GOT_ERROR:
        }
    }


    @Override
    public void onStart() {
        LogUtils.i(TAG, "basePresenter call onStart");
    }

    @Override
    public void onDestroy() {
        this.iBaseView = null;
    }
}
