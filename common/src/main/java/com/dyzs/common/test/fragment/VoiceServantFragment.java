package com.dyzs.common.test.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyzs.common.R;
import com.dyzs.common.ui.VoiceServant;

import java.util.Random;

/**
 * Created by maidou on 2018/1/8.
 */

public class VoiceServantFragment extends Fragment{
    VoiceServant voice_servant;
    private HandlerThread mHandlerThread;
    private String mHtName = "voice_servant";
    private Handler mLooper;
    private Handler mUIHandler;
    private static int TIME_DOWNING = 0x110;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHandlerThread();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_voice_servant, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        voice_servant = (VoiceServant) view.findViewById(R.id.voice_servant);
        voice_servant.setPointerDecibel(123);
        mLooper.sendEmptyMessage(TIME_DOWNING);
    }

    private void initHandlerThread() {
        mUIHandler = new Handler();
        mHandlerThread = new HandlerThread(mHtName, Process.THREAD_PRIORITY_DEFAULT);
        mHandlerThread.start();
        mLooper = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == TIME_DOWNING && i > 0) {
                    doWithMainUI();
                    i--;
                    mLooper.sendEmptyMessageDelayed(TIME_DOWNING, 500);
                }
            }
        };
    }
    private int i = 1000;
    private void doWithMainUI() {
        try {
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    int iRandom = (int) (Math.random() * 360 % 360);
                    voice_servant.setPointerDecibel(iRandom);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        mHandlerThread.quit();
        super.onDestroy();
    }
}
