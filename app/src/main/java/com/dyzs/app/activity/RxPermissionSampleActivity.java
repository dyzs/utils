package com.dyzs.app.activity;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dyzs.app.R;
import com.dyzs.base.BaseActivity;
import com.dyzs.common.utils.ToastUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;

import butterknife.OnClick;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * ================================================
 * Created by dyzs on 2017/11/20.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */
public class RxPermissionSampleActivity extends BaseActivity {
    private static String TAG = RxPermissionSampleActivity.class.getSimpleName();

    @Override
    public int initLayoutView(Bundle savedInstanceState) {
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
        RxPermissions rxPermissions = new RxPermissions(this);
        // rxPermissions.request(Manifest.permission.CAMERA);
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        ToastUtils.makeText(this, "access");
                    } else {
                        ToastUtils.makeText(this, "permission denied");
                    }
                });

        /*rxPermissions
                .requestEachCombined(Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        // All permissions are granted !
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // At least one denied permission without ask never again
                    } else {
                        // At least one denied permission with ask never again
                        // Need to go to the settings
                    }
        });*/
    }
}
