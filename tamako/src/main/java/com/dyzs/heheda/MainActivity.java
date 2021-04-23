package com.dyzs.heheda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

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

import com.dyzs.heheda.calllog.CallRecord;
import com.dyzs.heheda.calllog.PhoneManager;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";
    AppCompatEditText et_phone;
    AppCompatEditText et_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_phone = findViewById(R.id.et_phone);
        et_count = findViewById(R.id.et_count);
        startService();
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
                try {
                    mMyBinder.resetParam(count, phone);
                    mMyBinder.startCallTask();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
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
                try {
                    mMyBinder.endCallTask();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                // execShellCmd("input text '123456'");
            }
        });
        boolean lacksPermissions = TamakoUtils.checkLacks(this);
        if (lacksPermissions) {
            Toast.makeText(this, "权限缺失", Toast.LENGTH_LONG).show();
        }
        // writeCallLog();
    }

    private boolean allowUnBind = false;

    private ICallService mMyBinder;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "my conn...service connected");
            mMyBinder = ICallService.Stub.asInterface(iBinder);
            allowUnBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "my conn...service disconnected");
            mMyBinder = null;
            allowUnBind = false;
        }
    };

    @Override
    protected void onDestroy() {
        stopService();
        super.onDestroy();
    }

    private void startService() {
        Intent intent = new Intent(this, CallWorkService.class);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    private void stopService() {
        if (!allowUnBind)return;
        unbindService(mConn);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void execShellCmd(String cmd) {
        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    private void writeCallLog() {
        ThreadPoolUtils.enqueue(new Runnable() {
            @Override
            public void run() {
                List<CallRecord> listCallRecord = PhoneManager.getCallRecords(MainActivity.this);
                ArrayList<String> list = new ArrayList<>();
                for (CallRecord record : listCallRecord) {
                    list.add("Date:["+TamakoUtils.getCurrentTime(record.getDate())+"], Phone:["+record.getPhone()+"]");
                }
                TamakoUtils.writeLog(list, "call_log_04_23______");
            }
        });

    }

}