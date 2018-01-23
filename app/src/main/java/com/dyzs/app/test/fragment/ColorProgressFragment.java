package com.dyzs.app.test.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyzs.app.R;
import com.dyzs.app.ui.ColorProgress;

/**
 * @author dyzs
 * Created on 2018/1/17.
 */

public class ColorProgressFragment extends Fragment implements View.OnClickListener {
    private static final int SUCCESS = 1;
    private ColorProgress progress;
    private TextView click;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_color_progress, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        click = (TextView) view.findViewById(R.id.click);
        click.setOnClickListener(this);
        progress = (ColorProgress) view.findViewById(R.id.progress);
    }

    @Override
    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 100; i ++) {
                    i += 5;
                    SystemClock.sleep(50);
                    Message msg = Message.obtain();
                    msg.what = SUCCESS;
                    msg.obj = i;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case SUCCESS:
                    progress.setProgress((int) message.obj);
                    break;
            }
            return false;
        }
    });
}
