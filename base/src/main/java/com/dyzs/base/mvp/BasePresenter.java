package com.dyzs.base.mvp;

import android.os.Handler;
import android.os.Message;

/**
 * Created by maidou on 2018/1/27.
 */

public class BasePresenter {
    IBaseView iBaseView;
    public BasePresenter(IBaseView baseView) {
        this.iBaseView = baseView;
    }

    public BasePresenter(IBaseView baseView, String handlerThreadName) {
        this.iBaseView = baseView;

    }

    private Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            processMainHandleMessage(msg);
        }
    };

    protected void sendMainHandlerMessage(int what, Object obj){
        Message msg= mHandler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }

    protected void processMainHandleMessage(Message msg){

    }






}
