package com.dyzs.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.dyzs.app.R;

/**
 * @author dyzs
 * Created on 2016/4/30.
 * sample progress widget, including set progress color or total color
 * used API Rect and Path draw line
 */
public class ColorProgress extends View{
    private Paint mPaint;
    private int mTotalColor = Color.rgb(255,255,255);
    private int mTotalProgress = 100;
    private int mProgress = 20;
    private int mProgressColor = Color.BLACK;
    private float mWidth;
    private float mHeight;
    private float mDrawLeft, mDrawTop, mDrawRight, mDrawBottom;

    private Rect mProgressRect;
    private Path mPath;

    private float mCurrentWidth, mCurrentHeight;

    public ColorProgress(Context context) {
        this(context, null);
    }

    public ColorProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ColorProgress);
        mTotalColor = typedArray.getColor(R.styleable.ColorProgress_cpTotalColor, mTotalColor);
        mProgressColor = typedArray.getColor(R.styleable.ColorProgress_cpProgressColor, mProgressColor);
        mProgress = typedArray.getInteger(R.styleable.ColorProgress_cpProgress, mProgress);
        mTotalProgress = typedArray.getInteger(R.styleable.ColorProgress_cpTotalProgress, mTotalProgress);
        typedArray.recycle();

        if (mTotalProgress <= 0) {
            mTotalProgress = 100;
        }
        if (mProgress > mTotalProgress) {
            mProgress = mTotalProgress;
        }

        mDrawLeft = 0f;
        mDrawTop = 0f;
        mDrawRight = 0f;
        mDrawBottom = 0f;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mHeight);
        mPaint.setColor(mTotalColor);

        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(mWidth, 0);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        myDraw(canvas);
        super.onDraw(canvas);
    }

    private void myDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1f);
        paint.setColor(mProgressColor);

        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(mCurrentWidth * mProgress, 0);
        path.lineTo(mCurrentWidth * mProgress, mHeight);
        path.lineTo(0, mHeight);
        path.close();
        canvas.drawPath(path, paint);

    }

    public synchronized void setProgress(int progress) {
        if (progress > mTotalProgress) {
            progress = mTotalProgress;
        }
        this.mProgress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth() * 1.0f;
        mHeight = getMeasuredHeight() * 1.0f;
        mCurrentWidth = mWidth / mTotalProgress;
    }
}
