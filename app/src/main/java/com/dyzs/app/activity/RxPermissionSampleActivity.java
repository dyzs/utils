package com.dyzs.app.activity;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dyzs.app.R;
import com.dyzs.app.base.BaseActivity;
import com.dyzs.common.utils.ToastUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;

import butterknife.OnClick;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import rx.functions.Func1;

/**
 * Created by maidou on 2017/11/20.
 */

public class RxPermissionSampleActivity extends BaseActivity {
    private static String TAG = RxPermissionSampleActivity.class.getSimpleName();

    @Override
    public int initContentView() {
        return R.layout.act_rx_permission_sample;
    }

    @Override
    public void initView() {

        /*RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);
        rxPermissions.request(Manifest.permission.CAMERA);
        rxPermissions.ensureEach(Manifest.permission.CAMERA);*/

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.rx_permission_camera)
    public void permissionCameraClick() {
        ToastUtils.makeText(this, "hi");
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA);
        /*rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {

                    } else {

                    }
                });*/
    }
}
