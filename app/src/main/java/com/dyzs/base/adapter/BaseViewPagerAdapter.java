package com.dyzs.base.adapter;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * ================================================
 * Created by dyzs on 2018/5/1.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 */

public abstract class BaseViewPagerAdapter<T> extends PagerAdapter {
    protected final Context mContext;
    protected final LayoutInflater mLayoutInflater;
    protected ArrayList<T> mList;
    protected SparseArrayCompat<Object> mCaches;

    public BaseViewPagerAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList = new ArrayList<>();
        this.mCaches = new SparseArrayCompat<>();
    }


    public void setDataList(ArrayList<T> arrayList) {
        this.mList.clear();
        this.mList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void addDataList(ArrayList<T> arrayList) {
        this.mList.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {return (this.mList != null) ? this.mList.size() : 0;}

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {return view == object;}

    protected Object getCacheItem(int position) {
        Object object = mCaches.get(position);
        if (object == null) {
            object = loadCacheItem(position);
            mCaches.put(position, object);
        }
        return object;
    }

    protected abstract Object loadCacheItem(int position);

}
