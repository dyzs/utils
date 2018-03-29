package com.dyzs.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.dyzs.common.utils.ToastUtils;

/**
 * @author dyzs, created on 2018/3/29.
 */

public class BaseFragment extends Fragment {

    public void showToast(String text) {
        ToastUtils.makeText(getActivity(), text);
    }

    public void showToast(int resId) {
        ToastUtils.makeText(getActivity(), resId);
    }
}
