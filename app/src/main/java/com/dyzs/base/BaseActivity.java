package com.dyzs.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.dyzs.app.R;
import com.dyzs.common.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * ================================================
 * Created by dyzs on 2017/11/18.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 * Todo：待添加 toolbar 初始化操作, 以及适配 view
 */
public class BaseActivity<P extends IPresenter> extends AppCompatActivity {
    private Unbinder mUnBinder;

    protected P mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            int layoutResId = initLayoutView(savedInstanceState);
            if (layoutResId != 0) {
                setContentView(layoutResId);
                // bind butter knife
                mUnBinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.mPresenter = initPresenter();

        initView();

        initData();
    }

    public P initPresenter() {
        return null;
    }

    /**
     * 初始化 xml 布局文件
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * @return the def layout {@link R.layout#layout_wait_to_replace}
     */
    @LayoutRes
    public int initLayoutView(Bundle savedInstanceState) {
        return R.layout.layout_wait_to_replace;
    }

    public void initView() {

    }

    public void initData() {

    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null && mUnBinder != Unbinder.EMPTY) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        super.onDestroy();
    }

    public void showToast(String text) {
        ToastUtils.makeText(this, text);
    }

    public void showToast(int resId) {
        ToastUtils.makeText(this, resId);
    }
/*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (isFastDoubleClick()) {
                return false;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private static long lastClickTime = -1L;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 1000) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }
    */
}
