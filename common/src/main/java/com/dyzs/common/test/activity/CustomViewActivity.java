package com.dyzs.common.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dyzs.common.R;
import com.dyzs.common.base.BaseActivity;

/**
 * Created by maidou on 2017/11/20.
 */

public class CustomViewActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_custion_view);
        initView();
    }

    @Override
    public void initView() {
        findViewById(R.id.solar_eclipse_loading).setOnClickListener(this);
        findViewById(R.id.multi_player).setOnClickListener(this);
        findViewById(R.id.dotted_line).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.solar_eclipse_loading:
                intent = new Intent(this, CustomViewDisplayActivity.class);
                intent.putExtra("viewName", "SolarEclipse");
                break;
            case R.id.multi_player:
                intent = new Intent(this, CustomViewDisplayActivity.class);
                intent.putExtra("viewName", "YinJiMultiPlayer");
                break;
            case R.id.dotted_line:
                intent = new Intent(this, CustomViewDisplayActivity.class);
                intent.putExtra("viewName", "DottedLine");
                break;
        }
        startActivity(intent);
    }
}
