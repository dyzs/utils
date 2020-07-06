package com.dyzs.dbtest.activity;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dyzs.db.impl.RoomLoginImpl;
import com.dyzs.db.interfaces.RoomLoginInterface;
import com.dyzs.db.room.entities.LoginInfo;
import com.dyzs.dbtest.R;

public class MainActivity extends AppCompatActivity {
    TextView tv_hello_world;
    private String loginId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_hello_world = findViewById(R.id.tv_hello_world);
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoomLoginInterface loginInterface = new RoomLoginImpl();
                LoginInfo loginInfo = loginInterface.queryLoginInfo(MainActivity.this, "100");
                if (loginInfo == null) {
                    loginInfo = new LoginInfo();
                    loginInfo.loginId = "100";
                    loginInfo.recordTime = System.currentTimeMillis();
                    loginInterface.insertLoginInfo(MainActivity.this, loginInfo);
                }
                loginId = loginInfo.loginId;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_hello_world.setText(loginId);
                    }
                });
            }
        }).start();
    }
}
