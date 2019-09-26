package com.dyzs.app.activity;

import android.os.Bundle;
import android.view.View;

import com.dyzs.app.R;
import com.dyzs.app.presenter.HardcodeFilterPresenter;
import com.dyzs.app.view.IHardcodeFilterView;
import com.dyzs.base.BaseActivity;

import butterknife.OnClick;

public class HardcodeFilterActivity extends BaseActivity<HardcodeFilterPresenter> implements IHardcodeFilterView {

    HardcodeFilterPresenter mPresenter = new HardcodeFilterPresenter(this);

    @Override
    public int initLayoutView(Bundle savedInstanceState) {
        return R.layout.act_hardcode_filter;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.tv_start})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start:
                mPresenter.operateStartFilter();
                break;
        }
    }
}
