package com.dyzs.common.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dyzs.common.R;

/**
 * @author akuma
 * Created on 2016/8/13.
 * 
 */
public class BiscuitsToast extends Toast{
    private static BiscuitsToast instance = null;
    private static View view;
    private static TextView textView;
    private static Context mCtx;
    private static LinearLayout ll_parent;
    private static ProgressBar pb_progress;
    private static ImageView iv_load_done;

    public static final int STATE_LOADING = 1;
    public static final int STATE_DONE = 2;

    public static final int LOCATE_TOP = 3;
    public static final int LOCATE_CENTER = 4;
    public static final int LOCATE_BOTTOM = 5;
    public static final int LOCATE_CUSTOM = 6;
    public static BiscuitsToast getInstance(Context ctx) {
        if (instance == null) {
            synchronized (BiscuitsToast.class) {
                if (instance == null) {
                    instance = new BiscuitsToast(ctx);
                }
            }
        }
        return instance;
    }

    public BiscuitsToast(Context context) {
        super(context);
        mCtx = context;
        initView(context);
    }
    private void initView (Context ctx) {
        view = LayoutInflater.from(ctx).inflate(R.layout.layout_biscuits_toast, null);
        textView = (TextView) view.findViewById(R.id.tv_text);
        ll_parent = (LinearLayout) view.findViewById(R.id.ll_parent);
        pb_progress = (ProgressBar) view.findViewById(R.id.pb_progress);
        iv_load_done = (ImageView) view.findViewById(R.id.iv_load_done);
        this.setView(view);
    }

    @Override
    public void setText(int resId) {
        textView.setText(mCtx.getResources().getString(resId));
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void makeText(String text){
        instance.setText(text);
        instance.show();
    }

    /**
     * wait to do it,
     * used drawable util and generate, that can be set color, stroke, radius
     */
    public static void setBackgroundColor () {
        Drawable drawable = instance.ll_parent.getBackground();
        instance.ll_parent.setBackgroundDrawable(drawable);
    }



    public static void makeTextWithState(int state, String text) {
        instance.setText(text);
        switch (state) {
            case STATE_LOADING:
                iv_load_done.setVisibility(View.GONE);
                pb_progress.setVisibility(View.VISIBLE);
                break;
            case STATE_DONE:
                iv_load_done.setVisibility(View.VISIBLE);
                pb_progress.setVisibility(View.GONE);
                break;
        }
        instance.show();
    }

    public static void makeTextWithState(int state, int resId) {
        instance.setText(resId);
        switch (state) {
            case STATE_LOADING:
                iv_load_done.setVisibility(View.GONE);
                pb_progress.setVisibility(View.VISIBLE);
                break;
            case STATE_DONE:
                iv_load_done.setVisibility(View.VISIBLE);
                pb_progress.setVisibility(View.GONE);
                break;
        }
        instance.show();
    }

    public static void setLocate(int locate, int xOffset, int yOffset) {
        switch (locate) {
            case LOCATE_TOP:
                instance.setGravity(Gravity.TOP, xOffset, yOffset);
                break;
            case LOCATE_CENTER:
                instance.setGravity(Gravity.CENTER, xOffset, yOffset);
                break;
            case LOCATE_BOTTOM:
                instance.setGravity(Gravity.BOTTOM, xOffset, yOffset);
                break;
        }
    }

}
