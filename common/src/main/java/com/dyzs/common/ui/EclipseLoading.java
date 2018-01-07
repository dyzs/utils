package com.dyzs.common.ui;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.dyzs.common.R;

/**
 * @author dyzs
 * Created on 2018/1/5.
 * 18.01.07 12.50 : modify animation interpolator
 * 18.01.07 13.18 : add color sunrise and sunset
 */

public class EclipseLoading extends View{
    private Context mCtx;
    private Paint mPaint;
    private float mWidth, mHeight;
    private float l, t, r, b;
    private float mCirclePadding = 10f;
    private float mCircleRadius = 10f;
    private float[] mCirclePoint = new float[2];
    private float mStartAngle = -90;
    private float mSweepAngle = 0f;
    private int mProgress = 100;
    private ValueAnimator mAnimator;
    private Path mPath;
    private RectF mRectF;
    private boolean interrupt = false;
    private float mPaintStrokeWidth = 5f;
    private int mArcColor;
    private int mSunColor;
    private int mSunriseColor;
    private int mSunsetColor;

    public static final int STEP_1ST = 1;
    public static final int STEP_2ND = 2;
    public static final int STEP_3RD = 3;
    public static final int STEP_4TH = 4;
    private int mStep = STEP_1ST;
    public EclipseLoading(Context context) {
        this(context, null);
    }

    public EclipseLoading(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public EclipseLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCtx = context;
        init(context, attrs, defStyleAttr);
        startAnimation();
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EclipseLoading);
        mPaintStrokeWidth = ta.getDimension(R.styleable.EclipseLoading_elStrokeWidth, 5f);
        mArcColor = ta.getColor(R.styleable.EclipseLoading_elArcColor, ContextCompat.getColor(context, R.color.oxygen_yellow));
        mSunColor = ta.getColor(R.styleable.EclipseLoading_elSunColor, ContextCompat.getColor(context, R.color.oxygen_yellow));
        ta.recycle();

        mPaint = new Paint();
        mPaint.setStrokeWidth(mPaintStrokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mArcColor);
        mPath = new Path();
        mStep = STEP_1ST;
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
        mRectF = new RectF(l, t, r, b);
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
        if (mStep == STEP_1ST) {
            mSweepAngle = mProgress * 360f / 100;
            mPaint.setColor(mArcColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPath.addArc(mRectF, mStartAngle, -mSweepAngle);
            canvas.drawPath(mPath, mPaint);
        }
        if (mStep == STEP_2ND) {
            mSweepAngle = (100 - mProgress) * 360f / 100;
            mPaint.setColor(mArcColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPath.addArc(mRectF, mStartAngle, mSweepAngle);
            canvas.drawPath(mPath, mPaint);
        }
        if (mStep == STEP_3RD) {
            mPaint.setColor(mSunColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(
                    mCirclePoint[0],
                    mCirclePoint[1],
                    mCircleRadius,
                    mPaint);
            mPaint.setColor(mSunriseColor);
            float offset = mCircleRadius * 2 / 100 * mProgress;
            canvas.drawCircle(
                    mCirclePoint[0] - offset,
                    mCirclePoint[1],
                    mCircleRadius,
                    mPaint);
        }
        if (mStep == STEP_4TH) {
            mPaint.setColor(mSunColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(
                    mCirclePoint[0],
                    mCirclePoint[1],
                    mCircleRadius,
                    mPaint);
            mPaint.setColor(mSunsetColor);//ContextCompat.getColor(mCtx, R.color.white));
            float offset = mCircleRadius * 2 / 100 * mProgress;
            canvas.drawCircle(
                    mCirclePoint[0] + mCircleRadius * 2 - offset,
                    mCirclePoint[1],
                    mCircleRadius,
                    mPaint);
        }
    }

    public void startAnimation() {
        if (interrupt)return;
        mAnimator = ValueAnimator.ofFloat(0, getProgress());
        mAnimator.setDuration(getDuration());
        mAnimator.setInterpolator(getInterpolator());
        // mAnimator.setRepeatCount(-1);
        // mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mProgress = (int) value;
                setSunriseAndSunsetReverse(mProgress);
                postInvalidate();
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mStep == STEP_1ST) {
                    mStep = STEP_2ND;
                } else if (mStep == STEP_2ND) {
                    mStep = STEP_3RD;
                } else if (mStep == STEP_3RD) {
                    mStep = STEP_4TH;
                } else if (mStep == STEP_4TH) {
                    mStep = STEP_1ST;
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

    private int getProgress() {
        return this.mProgress;
    }

    private long getDuration() {
        long duration = -1L;
        switch (mStep) {
            case STEP_1ST:
            case STEP_2ND:
                duration = 400;
                break;
            case STEP_3RD:
            case STEP_4TH:
                duration = 2000;
                break;
        }
        return duration;
    }

    private LinearInterpolator linearInterpolator;
    private DecelerateInterpolator decelerateInterpolator;
    private TimeInterpolator getInterpolator() {
        TimeInterpolator interpolator;
        switch (mStep) {
            case STEP_1ST:
            case STEP_2ND:
                if (decelerateInterpolator == null) {
                    decelerateInterpolator = new DecelerateInterpolator();
                }
                interpolator = decelerateInterpolator;
                break;
            case STEP_3RD:
            case STEP_4TH:
                if (linearInterpolator == null) {
                    linearInterpolator = new LinearInterpolator();
                }
                interpolator = linearInterpolator;
                break;
            default:
                interpolator = new LinearInterpolator();
                break;
        }
        return interpolator;
    }


    private void setSunriseAndSunsetReverse(int value) {
        switch (mStep) {
            case STEP_1ST:
            case STEP_2ND:
                setBackgroundColor(ContextCompat.getColor(mCtx, R.color.black));
                break;
            case STEP_3RD:
                mSunriseColor = getSunriseColor(value);
                setBackgroundColor(mSunriseColor);
                break;
            case STEP_4TH:
                mSunsetColor = getSunsetColor(value);
                setBackgroundColor(mSunsetColor);
                break;
        }
    }

    private int getSunriseColor(int value) {
        int color = (int) (value * 255 * 1.0f / 100);
        return Color.rgb(color, color, color);
    }

    private int getSunsetColor(int value) {
        int color = (int) ((100 - value) * 255 * 1.0f / 100);
        return Color.rgb(color, color, color);
    }
}
