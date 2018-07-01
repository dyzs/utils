package com.dyzs.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.dyzs.app.R;
import com.dyzs.app.UtilsApplication;
import com.dyzs.app.presenter.CustomViewPresenter;
import com.dyzs.app.view.ICustomView;
import com.dyzs.base.BaseActivity;
import com.dyzs.common.utils.ToastUtilsVer2;
import com.dyzs.common.utils.ColorUtils;
import com.squareup.leakcanary.RefWatcher;

import butterknife.OnClick;

/**
 * ================================================
 * Created by dyzs on 2017/11/20.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */

public class CustomViewActivity extends BaseActivity<CustomViewPresenter> implements ICustomView {

    private CustomViewPresenter mPresenter = new CustomViewPresenter(this);
    @Override
    public int initLayoutView(Bundle savedInstanceState) {
        return R.layout.act_custion_view;
    }

    @Override
    public void initView() {}

    @Override
    public void initData() {}

    @Override
    public CustomViewPresenter initPresenter() {
        return mPresenter;
    }

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
                mPresenter.go2DottedLineView();
                return;
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


    @Override
    public void go2DottedLineView() {
        new ToastUtilsVer2.Builder(this)
                .setText("拉拉。。。")
                .setTextColor(ColorUtils.randomColor())
                .setGravity(Gravity.CENTER, 0, 0)
                .setDuration(Toast.LENGTH_SHORT)
                .build()
                .show();

        /*Intent intent = new Intent(this, CustomViewDisplayActivity.class);
        intent.putExtra("viewName", "DottedLine");
        startActivity(intent);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = UtilsApplication.getRefWatcher(this);
        refWatcher.watch(this);

    }
}
