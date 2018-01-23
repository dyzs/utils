package com.dyzs.common.test.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.dyzs.common.R;
import com.dyzs.common.base.BaseActivity;
import com.dyzs.common.test.fragment.ChasingLoadingFragment;
import com.dyzs.common.test.fragment.ColorProgressFragment;
import com.dyzs.common.test.fragment.CornerImageFragment;
import com.dyzs.common.test.fragment.DottedLineFragment;
import com.dyzs.common.test.fragment.SolarEclipseFragment;
import com.dyzs.common.test.fragment.CompassServantFragment;
import com.dyzs.common.test.fragment.YinJiMultiPlayerFragment;

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
