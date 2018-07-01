package com.dyzs.common.ui.rvhelper;

import android.support.v7.widget.RecyclerView;

/**
 * ================================================
 * Created by dyzs on 2018/6/26.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 * Description:
 *      Listener for manual initiation of a swipe.
 * ================================================
 */

public interface OnStartSwipeListener {
    /**
     * Called when a view is requesting a start of a swipe.
     * @param viewHolder The holder of the view to swipe.
     */
    void onStartSwipe(RecyclerView.ViewHolder viewHolder);
}
