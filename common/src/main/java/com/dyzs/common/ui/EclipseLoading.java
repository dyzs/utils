package com.dyzs.common.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * @author dyzs
 * Created on 2018/1/5.
 */

public class EclipseLoading extends View{
    private Paint mPaint;
    private float mWidth, mHeight;
    private float mArcStartAngle = -90;
    private float mArcSweepAngle = 0f;
    private int mProgress = 27;

    public EclipseLoading(Context context) {
        this(context, null);
    }

    public EclipseLoading(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public EclipseLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint();
        mPaint.setStrokeWidth(5f);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.DKGRAY);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.drawCircle(mWidth/2, mHeight/2, 20f, mPaint);
        /*Path path = new Path();
        path.addCircle(mWidth/2, mHeight/2, mHeight/2 - 10f, Path.Direction.CW);
        canvas.drawPath(path, mPaint);*/

        RectF rectF = new RectF(50, 50, 200, 200);
        mArcSweepAngle = mProgress * 360f / 100;
        canvas.drawArc(rectF, mArcStartAngle, mArcSweepAngle, true, mPaint);
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 100) {
            progress = 100;
        }
        this.mProgress = progress;
        postInvalidate();
    }

    public int getProgress() {
        return this.mProgress;
    }

    public void drawAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, getProgress());
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mProgress = (int) value;
                postInvalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }
}
