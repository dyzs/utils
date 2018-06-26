package com.dyzs.app.presenter;

import android.os.SystemClock;

import com.dyzs.app.view.IBarcodeScanView;
import com.dyzs.base.BasePresenter;

/**
 * ================================================
 * Created by dyzs on 2018/6/26.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */

public class BarcodeScanPresenter extends BasePresenter {
    private IBarcodeScanView iView;
    public BarcodeScanPresenter(IBarcodeScanView iView) {
        super(iView);
        this.iView = iView;
    }

    public void initDelayCloseThrobber() {
        new Thread(() -> {
            SystemClock.sleep(2000);
            sendMainHandlerMessage(1002, "dismissTh");
            /*getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    iView.dismissThrobber();
                }
            });*/
        }).start();
    }
}
