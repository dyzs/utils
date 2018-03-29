package com.dyzs.app.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyzs.app.R;
import com.dyzs.app.base.BaseFragment;
import com.dyzs.common.ui.ChasingLoading;

/**
 * Created by maidou on 2018/1/8.
 */

public class ChasingLoadingFragment extends BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_chasing_loading, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ChasingLoading chasing_loading = (ChasingLoading) view.findViewById(R.id.chasing_loading);
    }
}
