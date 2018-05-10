package com.dyzs.base;

import android.os.Handler;
import android.os.Message;

/**
 * Created by maidou on 2018/1/27.
 */

public class BasePresenter {
    private IBaseView iBaseView;
    private static final int MESSAGE_GOT_ERROR = -110;
    public BasePresenter(IBaseView baseView) {
        this.iBaseView = baseView;
    }

    public BasePresenter(IBaseView baseView, String handlerThreadName) {
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






}
