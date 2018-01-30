package com.dyzs.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dyzs.app.R;
import com.dyzs.common.ui.ColorProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author dyzs
 * Created on 2018/1/17.
 */

public class ColorProgressFragment extends Fragment{
    private static final int SUCCESS = 1;
    @BindView(R.id.progress)
    public ColorProgress progress;
    @BindView(R.id.click)
    public TextView click;

    private Unbinder unbinder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ButterKnife.bind(this); // activity use this
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_color_progress, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.click)
    public void click2Run() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
