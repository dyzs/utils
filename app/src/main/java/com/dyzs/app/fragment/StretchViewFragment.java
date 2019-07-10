package com.dyzs.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dyzs.app.R;
import com.dyzs.common.ui.StretchView2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author dyzs
 * Created on 2019/7/8.
 */

public class StretchViewFragment extends Fragment{
    private static final int SUCCESS = 1;

    private Unbinder unbinder;


    @BindView(R.id.stretchView) StretchView2 mStretchView2;
    @BindView(R.id.view_cover) View mViewCoverPart;
    @BindView(R.id.part_pic) RelativeLayout mViewPartPic;
    @BindView(R.id.toolbar) Toolbar mToolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ButterKnife.bind(this); // activity use this
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_stretch_view, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStretchView2.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStretchView2.invokeCoverView(mViewCoverPart);
                mStretchView2.invokePartPicView(mViewPartPic, mToolbarLayout.getMeasuredHeight());
            }
        }, 50);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
