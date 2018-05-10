package com.dyzs.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyzs.app.R;
import com.dyzs.base.BaseFragment;
import com.dyzs.app.presenter.CompassServantPresenter;
import com.dyzs.app.view.ICompassServantView;
import com.dyzs.common.ui.CompassServant;

/**
 * @author dyzs
 * Created on 2018/1/8.
 */

public class CompassServantFragment extends BaseFragment implements CompassServant.ServantListener
        , ICompassServantView{
    CompassServant compass_servant;
    private CompassServantPresenter presenter = new CompassServantPresenter(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.initHandlerThread();
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


    @Override
    public void onDestroy() {
        presenter.getHandlerThread().quit();
        super.onDestroy();
    }

    @Override
    public void startTension() {
        presenter.startTension();
    }

    @Override
    public void setCSPointerDecibel(int decibel) {
        compass_servant.setPointerDecibel(decibel);
    }
}
