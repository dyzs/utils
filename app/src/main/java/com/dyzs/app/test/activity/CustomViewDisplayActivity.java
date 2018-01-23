package com.dyzs.app.test.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.dyzs.app.R;
import com.dyzs.app.base.BaseActivity;
import com.dyzs.app.test.fragment.ChasingLoadingFragment;
import com.dyzs.app.test.fragment.ColorProgressFragment;
import com.dyzs.app.test.fragment.CornerImageFragment;
import com.dyzs.app.test.fragment.DottedLineFragment;
import com.dyzs.app.test.fragment.SolarEclipseFragment;
import com.dyzs.app.test.fragment.CompassServantFragment;
import com.dyzs.app.test.fragment.YinJiMultiPlayerFragment;

/**
 * Created by maidou on 2018/1/8.
 */

public class CustomViewDisplayActivity extends BaseActivity{
    FrameLayout container;
    String viewName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_custion_view_display);
        initView();
    }


    @Override
    public void initView() {
        container = (FrameLayout) findViewById(R.id.container);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        viewName = getIntent().getStringExtra("viewName");
        switch (viewName) {
            case "SolarEclipse":
                transaction.add(R.id.container, new SolarEclipseFragment(), viewName);
                break;
            case "ChasingLoading":
                transaction.add(R.id.container, new ChasingLoadingFragment(), viewName);
                break;
            case "CompassServant":
                transaction.add(R.id.container, new CompassServantFragment(), viewName);
                break;
            case "YinJiMultiPlayer":
                transaction.add(R.id.container, new YinJiMultiPlayerFragment(), viewName);
                break;
            case "DottedLine":
                transaction.add(R.id.container, new DottedLineFragment(), viewName);
                break;
            case "ColorProgress":
                transaction.add(R.id.container, new ColorProgressFragment(), viewName);
                break;
            case "CornerImage":
                transaction.add(R.id.container, new CornerImageFragment(), viewName);
                break;
        }
        transaction.commit();
    }

    @Override
    public void initData() {

    }
}
