package com.dyzs.common.ui.rvhelper;

import android.support.v7.widget.RecyclerView;

/**
 * ================================================
 * Created by dyzs on 2018/7/1.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 * Description.
 * ================================================
 */

public abstract class BaseItemTouchHelperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperCallbackListener {

    protected OnStartDragListener mStartDragListener;
    protected OnStartSwipeListener mStartSwipeListener;

    protected void setDragListener(OnStartDragListener listener) {
        mStartDragListener = listener;
    }

    protected void setSwipeListener(OnStartSwipeListener listener) {
        mStartSwipeListener = listener;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    if (mStartDragListener!=null)mStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });*/

        /*holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mStartSwipeListener.onStartSwipe(holder);
                }
                return false;
            }
        });*/
    }
}
