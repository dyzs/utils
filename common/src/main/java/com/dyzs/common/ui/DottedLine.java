package com.dyzs.common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.dyzs.common.R;

/**
 * Created by maidou on 2018/1/2.
 */

public class DottedLine extends View{
    private Context mCtx;
    private float mDashGap = 10f;
    private float mDashWidth = 10f;
    private int mType = 0;// 0 vertical, 1 horizontal
    private Paint mPaint;
    private float mWidth, mHeight;
    public DottedLine(Context context) {
        this(context, null);
    }

    public DottedLine(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DottedLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCtx = context;
        initialized(attrs, defStyleAttr);
    }

    private void initialized(AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(ContextCompat.getColor(mCtx, R.color.oxygen_grey));

        PathEffect effects = new DashPathEffect(new float[] { 0, 3, 10, 0}, 1);
        mPaint.setPathEffect(effects);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mType) {
            case 0:
                drawDashVertical(canvas);
                break;
            case 1:
                drawDashHorizontal(canvas);
                break;
        }
    }

    private void drawDashVertical(Canvas canvas) {
        mPaint.setStrokeWidth(mWidth);
        Path path = new Path();
        path.moveTo(mWidth / 2, 0);
        path.lineTo(mWidth / 2, mHeight);
        canvas.drawPath(path, mPaint);
    }

    private void drawDashHorizontal(Canvas canvas) {
        mPaint.setStrokeWidth(mHeight);
        Path path = new Path();
        path.moveTo(0, mHeight / 2);
        path.lineTo(mWidth, mHeight / 2);
        canvas.drawPath(path, mPaint);
    }

    public void setType(int type) {
        this.mType = type;
        invalidate();
    }
}
