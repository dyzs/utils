package com.dyzs.app.presenter;

import com.dyzs.app.view.IMainView;
import com.dyzs.base.BasePresenter;
import com.dyzs.base.IBaseView;

/**
 * @author maidou, created on 2018/2/28.
 */

public class MainPresenter extends BasePresenter {
    private IMainView iView;
    public MainPresenter(IBaseView baseView) {
        super(baseView);
        iView = (IMainView) baseView;
    }


    public void go2CustomView() {
        iView.go2CustomView();
    }

    public void go2RetrofitSample() {
        iView.go2RetrofitSample();
    }


    public void showMagicRUFDialog() {

    }


    public void go2SampleHotfix() {
        iView.go2SampleHotfix();
    }

    public void go2RxPermission() {
        iView.go2RxPermission();
    }

    public void go2AndroidPermission() {
        iView.go2AndroidPermissionRequest();
    }
}
