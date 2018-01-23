package com.dyzs.app.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dyzs.app.R;
import com.dyzs.app.base.BaseActivity;

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
        findViewById(R.id.chasing_loading).setOnClickListener(this);
        findViewById(R.id.multi_player).setOnClickListener(this);
        findViewById(R.id.dotted_line).setOnClickListener(this);
        findViewById(R.id.compass_servant).setOnClickListener(this);
        findViewById(R.id.color_progress).setOnClickListener(this);
        findViewById(R.id.corner_image).setOnClickListener(this);

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
            case R.id.chasing_loading:
                intent = new Intent(this, CustomViewDisplayActivity.class);
                intent.putExtra("viewName", "ChasingLoading");
                break;
            case R.id.compass_servant:
                intent = new Intent(this, CustomViewDisplayActivity.class);
                intent.putExtra("viewName", "CompassServant");
                break;
            case R.id.multi_player:
                intent = new Intent(this, CustomViewDisplayActivity.class);
                intent.putExtra("viewName", "YinJiMultiPlayer");
                break;
            case R.id.dotted_line:
                intent = new Intent(this, CustomViewDisplayActivity.class);
                intent.putExtra("viewName", "DottedLine");
                break;
            case R.id.color_progress:
                intent = new Intent(this, CustomViewDisplayActivity.class);
                intent.putExtra("viewName", "ColorProgress");
                break;
            case R.id.corner_image:
                intent = new Intent(this, CustomViewDisplayActivity.class);
                intent.putExtra("viewName", "CornerImage");
                break;
        }
        startActivity(intent);
    }
}
