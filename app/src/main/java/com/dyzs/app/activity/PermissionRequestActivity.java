package com.dyzs.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dyzs.app.R;
import com.dyzs.base.BaseActivity;

import butterknife.OnClick;

public class PermissionRequestActivity extends BaseActivity {


    @Override
    public int initLayoutView(Bundle savedInstanceState) {
        return R.layout.act_permission_request;
    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void initData() {
        super.initData();

    }

    @OnClick({R.id.tv_read_phone_contacts})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_read_phone_contacts:
                handleReadPhoneContactsPermission();
                break;
        }
    }

    private void handleReadPhoneContactsPermission() {
        boolean b = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},10086);
        } else {
            // realReadContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10086:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // realReadContacts();
                } else {
                    Toast.makeText(this, "获取联系人权限失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
