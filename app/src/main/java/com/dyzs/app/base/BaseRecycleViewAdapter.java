package com.dyzs.app.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by NKlaus on 2017/11/19.
 */

public abstract class BaseRecycleViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected final Context mContext;
    protected final LayoutInflater mLayoutInflater;
    protected ArrayList<T> mList;
    protected ItemClickListener<T> onItemClickListener;
    protected ItemLongClickListener<T> onItemLongClickListener;

    public BaseRecycleViewAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList = new ArrayList<>();
    }

    public interface ItemClickListener<T> {
        void onItemClick(T t, int position);
    }

    public interface ItemLongClickListener<T> {
        void onItemLongClick(T t, int position);
    }

    public void setOnItemClickListener(ItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(ItemLongClickListener<T> listener) {
        this.onItemLongClickListener = listener;
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
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        final T t = mList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(t, holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null)
                    onItemLongClickListener.onItemLongClick(t, holder.getAdapterPosition());
                return false;
            }
        });

        /* do your own logic */
        onBindViewHolder(t, holder, position);
    }

    protected abstract void onBindViewHolder(T t, VH holder, int position);

    @Override
    public int getItemCount() {
        return (this.mList != null) ? this.mList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
