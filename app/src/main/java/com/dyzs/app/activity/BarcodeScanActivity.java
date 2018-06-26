package com.dyzs.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dyzs.app.R;
import com.dyzs.app.presenter.BarcodeScanPresenter;
import com.dyzs.app.view.IBarcodeScanView;
import com.dyzs.base.BaseActivity;
import com.dyzs.common.utils.CommonUtils;
import com.dyzs.common.utils.ToastUtils;
import com.dyzs.zxing.activity.CaptureFragment;
import com.dyzs.zxing.activity.CodeUtils;



/**
 * ================================================
 * Created by dyzs on 2018/6/26.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */

public class BarcodeScanActivity extends BaseActivity<BarcodeScanPresenter> implements IBarcodeScanView, CodeUtils.AnalyzeCallback {
    private static final int REQUEST_PERMISSION_CAMERA = 1201;
    private BarcodeScanPresenter presenter = new BarcodeScanPresenter(this);
    private RelativeLayout rl_loading;
    private CaptureFragment captureFragment;
    private TextView tv_net_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_barcode_scan);
        initToolbarView();
        initScannerOnCreate();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkPermission(Manifest.permission.CAMERA)) {
                presenter.initDelayCloseThrobber();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
            }
        } else {
            presenter.initDelayCloseThrobber();
        }
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.initDelayCloseThrobber();
            } else {
                ToastUtils.makeText(this,"权限被禁止，无法打开相机");
            }
        }
    }

    private void initToolbarView() {
        // initToolbar(R.id.toolbar);
        // setToolbarTitle(R.id.toolbar_title, getResources().getString(R.string.title_barcode_scan_add_goods));
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        tv_net_error = (TextView) findViewById(R.id.tv_net_error);
        if (CommonUtils.hasInternet(this)) {
            tv_net_error.setVisibility(View.GONE);
        }
    }

    private void initScannerOnCreate() {
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        captureFragment.setFragmentArgs(R.layout.layout_my_scanner);
        captureFragment.setAnalyzeCallback(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_my_container, captureFragment)
                .commit();
    }

    @Override
    public void dismissThrobber() {
        rl_loading.setVisibility(View.GONE);
    }

    @Override
    public void getGoodsInfo(String barcode) {
        // get goods info
        Intent resultIntent = new Intent();
        resultIntent.putExtra("barcode",barcode);
        BarcodeScanActivity.this.setResult(RESULT_OK, resultIntent);
        BarcodeScanActivity.this.finish();
    }

    @Override
    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
        bundle.putString(CodeUtils.RESULT_STRING, result);
        resultIntent.putExtras(bundle);
    }

    @Override
    public void onAnalyzeFailed() {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
        bundle.putString(CodeUtils.RESULT_STRING, "");
        resultIntent.putExtras(bundle);
    }
}
