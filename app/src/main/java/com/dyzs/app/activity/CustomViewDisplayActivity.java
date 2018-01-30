package com.dyzs.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.dyzs.app.R;
import com.dyzs.app.base.BaseActivity;
import com.dyzs.app.fragment.ChasingLoadingFragment;
import com.dyzs.app.fragment.ColorProgressFragment;
import com.dyzs.app.fragment.CornerImageFragment;
import com.dyzs.app.fragment.DottedLineFragment;
import com.dyzs.app.fragment.FragmentFactory;
import com.dyzs.app.fragment.SolarEclipseFragment;
import com.dyzs.app.fragment.CompassServantFragment;
import com.dyzs.app.fragment.YinJiMultiPlayerFragment;

import butterknife.BindView;

/**
 * Created by maidou on 2018/1/8.
 */

public class CustomViewDisplayActivity extends BaseActivity{
    String viewName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_custion_view_display);
        initView();
    }


    @Override
    public void initView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        viewName = getIntent().getStringExtra("viewName");
        FragmentFactory.Type type = FragmentFactory.Type.SOLAR_ECLIPSE;
        switch (viewName) {
            case "SolarEclipse":
                type = FragmentFactory.Type.SOLAR_ECLIPSE;
                break;
            case "ChasingLoading":
                type = FragmentFactory.Type.CHASING_LOADING;
                break;
            case "CompassServant":
                type = FragmentFactory.Type.COMPASS_SERVANT;
                break;
            case "YinJiMultiPlayer":
                type = FragmentFactory.Type.MULTI_PLAYER;
                break;
            case "DottedLine":
                type = FragmentFactory.Type.DOTTED_LINE;
                break;
            case "ColorProgress":
                type = FragmentFactory.Type.COLOR_PROGRESS;
                break;
            case "CornerImage":
                type = FragmentFactory.Type.CORNER_IMAGE;
                break;
        }
        transaction.add(R.id.container, FragmentFactory.createFragment(type), viewName);
        transaction.commit();
    }

    @Override
    public void initData() {

    }
}
