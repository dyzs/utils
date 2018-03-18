package com.dyzs.common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * @author maidou, created on 2018/3/16.
 */

public class LineChartViewWrapper extends HorizontalScrollView {
    String TAG = getClass().getSimpleName();

    public LineChartViewWrapper(Context context) {
        super(context);
    }

    public LineChartViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartViewWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println(TAG + "onDraw");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println(TAG + "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        System.out.println(TAG + "onLayout");
    }


}
