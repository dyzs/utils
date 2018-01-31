package com.dyzs.common.ui.magicruf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author dyzs, created on 2018/1/31.
 */

public class MagicRUF extends View {
    private float mWidth, mHeight;
    private float mPadding, mRadius;
    private float[] mCenterPoint;

    private Path mPathOuter, mPathInner, mPathTriangle, mPathTriangle2;
    private PathMeasure mPathMeasure;
    private Paint mPaint;
    private float mLineWidth;
    public MagicRUF(Context context) {
        this(context, null);
    }

    public MagicRUF(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MagicRUF(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mLineWidth = 10f;
        mPadding = 10f;
        if (mLineWidth > mPadding) {
            mPadding = mLineWidth;
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setColor(Color.CYAN);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = w;mHeight = h;
        if (mWidth >= mHeight) {
            mRadius = mHeight / 2 - mPadding;
        } else {
            mRadius = mWidth / 2 - mPadding;
        }
        mCenterPoint = new float[]{mWidth / 2, mHeight / 2};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rectF = new RectF();
        rectF.set(mCenterPoint[0] - mRadius, mPadding, mCenterPoint[0] + mRadius, mCenterPoint[1] + mRadius);
        mPathOuter = new Path();
        mPathOuter.addArc(rectF, -90, 360);

        canvas.drawPath(mPathOuter, mPaint);

        canvas.save();



        canvas.restore();


    }
}
