package com.dyzs.app.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by NKlaus on 2017/11/19.
 */

public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mCtx;
    private ArrayList<T> mList;
    public BaseRecycleViewAdapter(Context context, ArrayList<T> arrayList) {
        this.mCtx = context;
        this.mList = arrayList;
    }

    public void setDataList(ArrayList<T> arrayList) {
        this.mList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position) ;

    @Override
    public int getItemCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
