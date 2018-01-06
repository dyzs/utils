package com.dyzs.common.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author dyzs
 * Created on 2018/1/5.
 */

public class EclipseLoading extends View{
    private Paint mPaint;
    private float mWidth, mHeight;
    private float l, t, r, b;
    private float mCirclePadding = 10f;
    private float mCircleRadius = 10f;
    private float[] mCirclePoint = new float[2];
    private float mStartAngle = -90;
    private float mSweepAngle = 0f;
    private int mProgress = 100;

    private STEP mStep = STEP.STEP_1ST;
    private ValueAnimator mAnimator;
    private Path mPath;
    private RectF mRectF;
    private boolean interrupt = false;
    public EclipseLoading(Context context) {
        this(context, null);
    }

    public EclipseLoading(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public EclipseLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
        startAnimation();
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint();
        mPaint.setStrokeWidth(5f);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.DKGRAY);
        mPath = new Path();
        mStep = STEP.STEP_1ST;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mCirclePoint[0] = mWidth / 2;
        mCirclePoint[1] = mHeight / 2;
        if (mWidth >= mHeight) {
            mCirclePadding = mHeight * 0.05f;
            mCircleRadius = mHeight / 2 - mCirclePadding;
        } else {
            mCirclePadding = mWidth * 0.05f;
            mCircleRadius = mWidth / 2 - mCirclePadding;
        }
        l = mWidth / 2 - mCircleRadius;
        t = mHeight / 2 - mCircleRadius;
        r = mWidth / 2 + mCircleRadius;
        b = mHeight / 2 + mCircleRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*mRectF = new RectF(50, 50, 200, 200);
        canvas.drawArc(mRectF, mStartAngle, mSweepAngle, true, mPaint);*/
        // mPath.addCircle(mWidth/2, mHeight/2, mHeight/2 - 10f, Path.Direction.CW);
        // mRectF = new RectF(mWidth/2 - mHeight/2 + 50f, 50f, mWidth/2 + mHeight/2-50f, mHeight-50f);
        // mPath.addRoundRect(mRectF, mSweepAngle, mHeight/2, Path.Direction.CW);
        mPath.reset();
        mRectF = new RectF(l, t, r, b);
        if (mStep == STEP.STEP_1ST) {
            mSweepAngle = mProgress * 360f / 100;
            mPaint.setColor(Color.DKGRAY);
            mPaint.setStyle(Paint.Style.STROKE);
            mPath.addArc(mRectF, mStartAngle, -mSweepAngle);
            canvas.drawPath(mPath, mPaint);
        }
        if (mStep == STEP.STEP_2ND) {
            mSweepAngle = (100 - mProgress) * 360f / 100;
            mPaint.setColor(Color.DKGRAY);
            mPaint.setStyle(Paint.Style.STROKE);
            mPath.addArc(mRectF, mStartAngle, mSweepAngle);
            canvas.drawPath(mPath, mPaint);
        }
        if (mStep == STEP.STEP_3RD) {
            mPaint.setColor(Color.YELLOW);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(
                    mCirclePoint[0],
                    mCirclePoint[1],
                    mCircleRadius,
                    mPaint);
            mPaint.setColor(Color.WHITE);
            float offset = mCircleRadius * 2 / 100 * mProgress;
            canvas.drawCircle(
                    mCirclePoint[0] - offset,
                    mCirclePoint[1],
                    mCircleRadius,
                    mPaint);
        }
        if (mStep == STEP.STEP_4TH) {
            mPaint.setColor(Color.YELLOW);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(
                    mCirclePoint[0],
                    mCirclePoint[1],
                    mCircleRadius,
                    mPaint);
            mPaint.setColor(Color.WHITE);
            float offset = mCircleRadius * 2 / 100 * mProgress;
            canvas.drawCircle(
                    mCirclePoint[0] + mCircleRadius * 2 - offset,
                    mCirclePoint[1],
                    mCircleRadius,
                    mPaint);
        }
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

    public void startAnimation() {
        if (interrupt)return;
        mAnimator = ValueAnimator.ofFloat(0, getProgress());
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(new LinearInterpolator());
        // mAnimator.setRepeatCount(-1);
        // mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mProgress = (int) value;
                postInvalidate();
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mStep == STEP.STEP_1ST) {
                    mStep = STEP.STEP_2ND;
                } else if (mStep == STEP.STEP_2ND) {
                    mStep = STEP.STEP_3RD;
                } else if (mStep == STEP.STEP_3RD) {
                    mStep = STEP.STEP_4TH;
                } else if (mStep == STEP.STEP_4TH) {
                    mStep = STEP.STEP_1ST;
                }
                startAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    public void setInterruptAnimation(boolean interrupt) {
        this.interrupt = interrupt;
    }

    private enum STEP{
        STEP_1ST, STEP_2ND, STEP_3RD, STEP_4TH
    }
}
