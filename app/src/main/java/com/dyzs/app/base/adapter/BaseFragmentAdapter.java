package com.dyzs.app.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * Created by dyzs on 2018/5/1.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitles;
    private List<Fragment> mFragments;
    public BaseFragmentAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
        mFragments = new ArrayList<>();
    }

    public void setFragments(List<Fragment> fragments) {
        mFragments.clear();
        mFragments.addAll(fragments);
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
