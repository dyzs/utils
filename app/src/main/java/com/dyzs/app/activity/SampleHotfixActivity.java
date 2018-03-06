package com.dyzs.app.activity;

import android.os.Environment;

import com.dyzs.app.R;
import com.dyzs.app.base.BaseActivity;
import com.dyzs.common.utils.FixDexUtils;
import com.dyzs.common.utils.ToastUtils;

import butterknife.OnClick;

/**
 * @author maidou, created on 2018/3/6.
 */

public class SampleHotfixActivity extends BaseActivity {

    @Override
    public int initContentView() {
        return R.layout.act_sample_hotfix;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.test_hotfix)
    public void testHotfix () {
        int i = 10;
        int j = 0;
        ToastUtils.makeText(this, i / j + "----Hello hotfix");
    }

    @OnClick(R.id.fix_bug)
    public void fixBug() {
        FixDexUtils.loadFixedDex(this, Environment.getExternalStorageDirectory());
    }


}
