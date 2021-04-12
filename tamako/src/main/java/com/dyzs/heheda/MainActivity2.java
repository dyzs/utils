package com.dyzs.heheda;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class MainActivity2 extends AppCompatActivity implements CallWorkManager.ICallback {
    static final String TAG = "MainActivity";
    AppCompatEditText et_phone;
    AppCompatEditText et_count;
    private CallWorkManager callWorkManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callWorkManager = new CallWorkManager(this).setICallback(this);
        et_phone = findViewById(R.id.et_phone);
        et_count = findViewById(R.id.et_count);
        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean lacksPermissions = checkLacks();
                if (lacksPermissions) {
                    Toast.makeText(view.getContext(), "权限缺失", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(et_phone.getText())) {
                    Toast.makeText(view.getContext(), "缺少手机号", Toast.LENGTH_LONG).show();
                    return;
                }
                String phone = et_phone.getText().toString();
                int count = Integer.parseInt(et_count.getText().toString());
                callWorkManager.resetParam(count, phone);
                callWorkManager.startCallTask();
            }
        });

        findViewById(R.id.tv_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean lacksPermissions = checkLacks();
                if (lacksPermissions) {
                    Toast.makeText(view.getContext(), "权限缺失", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(et_phone.getText())) {
                    Toast.makeText(view.getContext(), "缺少手机号", Toast.LENGTH_LONG).show();
                    return;
                }
                callWorkManager.endCall();
                callWorkManager.stopCallTask();
            }
        });
        boolean lacksPermissions = checkLacks();
        if (lacksPermissions) {
            Toast.makeText(this, "权限缺失", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkLacks() {
        return lacksPermissions(this, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_PHONE_STATE);
    }

    public static boolean lacksPermissions(Context mContexts, String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(mContexts, permission)) {
                return true;
            }
        }
        return false;
    }

    private static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    @Override
    public void sendCKToken() {
        
    }
}