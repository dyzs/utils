package com.dyzs.common.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by dyzs on 2019/7/11.
 */
public class StretchViewChildViewPager extends ViewPager implements NestedScrollingChild2 {
    private static final String TAG = StretchViewChildViewPager.class.getSimpleName();
    private int mTouchSlop;

    public StretchViewChildViewPager(@NonNull Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public StretchViewChildViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean startNestedScroll(int i, int i1) {
        return false;
    }

    @Override
    public void stopNestedScroll(int i) {

    }

    @Override
    public boolean hasNestedScrollingParent(int i) {
        return false;
    }

    @Override
    public boolean dispatchNestedScroll(int i, int i1, int i2, int i3, @Nullable int[] ints, int i4) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int i, int i1, @Nullable int[] ints, @Nullable int[] ints1, int i2) {
        return false;
    }
}
