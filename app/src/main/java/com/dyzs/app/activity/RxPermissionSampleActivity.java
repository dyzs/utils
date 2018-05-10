package com.dyzs.app.activity;

import android.Manifest;

import com.dyzs.app.R;
import com.dyzs.app.base.BaseActivity;
import com.dyzs.common.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.OnClick;

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
        // rxPermissions.request(Manifest.permission.CAMERA);
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {

                    } else {

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
