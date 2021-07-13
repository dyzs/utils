package com.dyzs.calllogwirter;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tv_content;
    TextView tv_content_v2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_content = findViewById(R.id.tv_content);
        tv_content_v2 = findViewById(R.id.tv_content_v2);

        findViewById(R.id.tv_get_call_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeCallLog();
            }
        });
    }

    private void writeCallLog() {
        acceptAnswerCall();

        String[] str = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALL_LOG,
        };
        /*if (CommonUtils.lacksPermissions(this, str)) {
            return;
        }*/

        ThreadPoolUtils.enqueue(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    String columns = PhoneManager.getSysColumns(MainActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_content_v2.setText(columns);
                        }
                    });
                    List<CallRecord> listCallRecord = PhoneManager.getCallRecords(MainActivity.this);
                    ArrayList<String> list = new ArrayList<>();
                    for (CallRecord record : listCallRecord) {
                        list.add(record.toStringV2());
                        stringBuilder.append(record.toStringV2()).append("\n\t");
                    }
                    CommonUtils.writeLog(list, "call_log_" + CommonUtils.getTimeForFileName() + "_");
                    Log.i("TAGGGGG", "完成");
                    final String log = stringBuilder.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_content.setText(log);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_content.setText(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void acceptAnswerCall() {
        String[] str = new String[]{
                Manifest.permission.ANSWER_PHONE_CALLS,
                Manifest.permission.MODIFY_PHONE_STATE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.LOCATION_HARDWARE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        if (CommonUtils.lacksPermissions(this, str)) {
            ActivityCompat.requestPermissions(this, str, 10086);
        }
    }
}