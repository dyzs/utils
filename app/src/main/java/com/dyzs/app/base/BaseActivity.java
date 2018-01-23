package com.dyzs.app.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by NKlaus on 2017/11/18.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    public abstract void initView();

    public abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
