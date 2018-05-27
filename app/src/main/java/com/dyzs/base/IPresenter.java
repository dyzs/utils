package com.dyzs.base;

import android.app.Activity;

/**
 * ================================================
 * Created by dyzs on 2018/5/27.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */
public interface IPresenter {

    void onStart();

    /**
     * 在框架中 {@link Activity#onDestroy()} 时默认调用 {@link IPresenter#onDestroy()}
     */
    void onDestroy();

}
