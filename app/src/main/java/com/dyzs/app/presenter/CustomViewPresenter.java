package com.dyzs.app.presenter;

import com.dyzs.app.view.ICustomView;
import com.dyzs.base.BasePresenter;

/**
 * ================================================
 * Created by dyzs on 2018/5/27.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */
public class CustomViewPresenter extends BasePresenter<ICustomView> {

    private ICustomView iView;
    public CustomViewPresenter(ICustomView baseView) {
        super(baseView);
        iView = baseView;
    }

    public void go2DottedLineView() {
        iView.go2DottedLineView();
    }


}
