package com.dyzs.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.dyzs.app.R;
import com.dyzs.common.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by NKlaus on 2017/11/18.
 */
public class BaseActivity extends AppCompatActivity {
    private Unbinder mUnBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(initContentView());

        mUnBinder = ButterKnife.bind(this);

        initView();

        initData();
    }

    @LayoutRes
    public int initContentView() {
        return R.layout.layout_wait_to_replace;
    }

    public void initView() {

    }

    public void initData() {

    }

    @Override
    protected void onDestroy() {
        mUnBinder.unbind();
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
