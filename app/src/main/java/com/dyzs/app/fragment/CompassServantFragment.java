package com.dyzs.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyzs.app.R;
import com.dyzs.common.ui.CompassServant;

/**
 * @author dyzs
 * Created on 2018/1/8.
 */

public class CompassServantFragment extends Fragment implements CompassServant.ServantListener{
    CompassServant compass_servant;
    private HandlerThread mHandlerThread;
    private String mHtName = "compass_servant";
    private Handler mLooper;
    private Handler mUIHandler;
    private static int MESSAGE = 0x110;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHandlerThread();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_compass_servant, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compass_servant = (CompassServant) view.findViewById(R.id.compass_servant);
        compass_servant.setServantListener(this);
        compass_servant.setPointerDecibel(118);
    }

    private void initHandlerThread() {
        mUIHandler = new Handler();
        mHandlerThread = new HandlerThread(mHtName, Process.THREAD_PRIORITY_DEFAULT);
        mHandlerThread.start();
        mLooper = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MESSAGE && i > 0) {
                    doWithMainUI();
                    i--;
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
                    Double d = Math.random() * 89;
                    int iRandom = d.intValue() + 30;
                    compass_servant.setPointerDecibel(iRandom);
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

    @Override
    public void startTension() {
        mLooper.sendEmptyMessage(MESSAGE);
    }
}
