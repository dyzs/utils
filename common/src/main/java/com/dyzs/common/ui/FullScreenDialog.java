package com.dyzs.common.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.dyzs.common.R;

/**
 * Created by maidou on 2017/11/17.
 */

public class FullScreenDialog extends Dialog{
    private boolean interruptKeyEvent = true;
    public FullScreenDialog(Context context) {
        super(context, R.style.FullScreenDialog);
        setCanceledOnTouchOutside(true);
        interruptKeyEvent = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width= WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }


    public void showMatchWidth() {
        showMatchWidth(0, 0);
    }

    public void showMatchWidth(int paddingLeft, int paddingRight) {
        if (paddingLeft < 0) {
            paddingLeft = 25;
        }
        if (paddingRight < 0) {
            paddingRight = 25;
        }
        show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingLeft, getContext().getResources().getDisplayMetrics()),
                0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingRight, getContext().getResources().getDisplayMetrics()),
                0);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (interruptKeyEvent) {
            if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) && event.getRepeatCount() == 0) {
                if (isShowing()) {
                    // System.out.println("can you feel powerless?");
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 拦截 back 和 home 的点击事件
     * @param interrupt
     */
    public void setInterruptKeyEvent(boolean interrupt) {
        this.interruptKeyEvent = interrupt;
    }
}
