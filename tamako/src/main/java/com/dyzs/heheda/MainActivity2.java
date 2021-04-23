package com.dyzs.heheda;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.dyzs.heheda.manager.CallWorkManager;

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
                boolean lacksPermissions = TamakoUtils.checkLacks(view.getContext());
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
                callWorkManager.startRecordLog();
                callWorkManager.resetCallStateListenImpl(true);
                callWorkManager.resetParam(count, phone);
                callWorkManager.startCallTask();
            }
        });

        findViewById(R.id.tv_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean lacksPermissions = TamakoUtils.checkLacks(view.getContext());
                if (lacksPermissions) {
                    Toast.makeText(view.getContext(), "权限缺失", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(et_phone.getText())) {
                    Toast.makeText(view.getContext(), "缺少手机号", Toast.LENGTH_LONG).show();
                    return;
                }
                callWorkManager.resetCallStateListenImpl(false);
                callWorkManager.endCall();
                callWorkManager.stopCallTask();
            }
        });
        boolean lacksPermissions = TamakoUtils.checkLacks(this);
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

    @Override
    public void sendCKToken() {
        
    }
}