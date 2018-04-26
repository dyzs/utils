package com.dyzs.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.view.View;
import android.widget.TextView;

import com.dyzs.app.R;
import com.dyzs.app.base.BaseActivity;
import com.dyzs.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by maidou on 2017/11/20.
 */

public class CustomViewActivity extends BaseActivity {

    @Override
    public int initContentView() {
        return R.layout.act_custion_view;
    }

    @Override
    public void initView() {}

    @Override
    public void initData() {}


    @OnClick({R.id.solar_eclipse_loading, R.id.chasing_loading, R.id.compass_servant, R.id.multi_player,
            R.id.dotted_line, R.id.color_progress, R.id.corner_image})
    public void go2SolarEclipseLoading(TextView textView) {
        Intent intent = null;
        switch (textView.getId()) {
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

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
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
