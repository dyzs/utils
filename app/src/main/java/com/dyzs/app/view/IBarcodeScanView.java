package com.dyzs.app.view;

import com.dyzs.base.IBaseView;

/**
 * ================================================
 * Created by dyzs on 2018/6/26.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */

public interface IBarcodeScanView extends IBaseView {

    void dismissThrobber();

    void getGoodsInfo(String barcode);
}
