package com.dyzs.common.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.dyzs.common.R;

/**
 * @author dyzs
 * Created on 2018/1/9.
 * a voice servant, voice sensor listener
 * decibel(dB) use range 0~150 or 0~120
 */

public class VoiceServant extends View{
    private static final String TAG = VoiceServant.class.getSimpleName();
    private Context mCtx;// god of the universal
    private float mWidth, mHeight;
    private float mPadding;// the strength of the triangle point
    private float mSpacing;// the abyss between tick mark and outer circle
    private float[] mCircleCenter = new float[2];// center of the universal
    private float mRadius, mPointerRadius, mTickRadius, mOxygenRadius;
    private float mGalaxyDegree, mAPieceOfDegree, mPointerDegree, mMinDegree, mMaxDegree;
    private RectF mPointerRectF, mTickRectF, mOxygenRectF;
    private float mCircleWidth;// outer circle width
    private float mTickLength;// tick mark pointer length
    private float mTickWidth;// tick mark pointer width
    private float mOxygenWidth;// color gradient width
    private float mMoriSummerWidth;// pointer width
    private Paint mDarkPaint;// outer circle paint
    private Paint mFlamePaint;// tick mark paint
    private Paint mMasterPaint;// color gradient paint
    private Paint mMoriSummerPaint;// pointer paint
    private float mStartAngle;
    private int mDecibel = 119;// decibel
    private int[] mGradientColors;
    // add servant trick
    public VoiceServant(Context context) {
        this(context, null);
    }

    public VoiceServant(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public VoiceServant(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public VoiceServant(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        startPointerAnim();
    }

    private void init(Context context) {
        mCtx = context;
        mPadding = 10f;
        mSpacing = 15f;
        mGalaxyDegree = 280f;
        mAPieceOfDegree = mGalaxyDegree / mDecibel;
        mStartAngle = (360f - mGalaxyDegree) / 2 + 90f;
        mPointerDegree = 125f; mMinDegree = 60f; mMaxDegree = 180f; // def degree value
        mCircleWidth = 20f;
        mTickLength = 80f;
        mTickWidth = 3f;
        mOxygenWidth = 30f;
        mMoriSummerWidth = 10f;

        mDarkPaint = new Paint();
        mDarkPaint.setAntiAlias(true);
        mDarkPaint.setStyle(Paint.Style.STROKE);
        mDarkPaint.setStrokeWidth(mCircleWidth);
        mDarkPaint.setColor(ContextCompat.getColor(context, R.color.blair_grey));

        mFlamePaint = new Paint();
        mFlamePaint.setAntiAlias(true);
        mFlamePaint.setStyle(Paint.Style.STROKE);
        mFlamePaint.setStrokeWidth(mTickWidth);
        mFlamePaint.setColor(ContextCompat.getColor(context, R.color.emma_grey));

        mMasterPaint = new Paint();
        mMasterPaint.setAntiAlias(true);
        mMasterPaint.setStyle(Paint.Style.STROKE);
        mMasterPaint.setStrokeWidth(mOxygenWidth);
        mMasterPaint.setColor(ContextCompat.getColor(context, R.color.girl_pink));

        mMoriSummerPaint = new Paint();
        mMoriSummerPaint.setAntiAlias(true);
        mMoriSummerPaint.setStyle(Paint.Style.STROKE);
        mMoriSummerPaint.setStrokeWidth(mMoriSummerWidth);
        mMoriSummerPaint.setColor(ContextCompat.getColor(context, R.color.alice_blue));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();mHeight = getMeasuredHeight();
        if (mWidth >= mHeight) {
            mRadius = mHeight / 2 - mPadding;
        } else {
            mRadius = mWidth / 2 - mPadding;
        }
        mCircleCenter[0] = mWidth / 2; mCircleCenter[1] = mHeight / 2;

        mPointerRadius = mRadius - mCircleWidth / 2;
        float l, t, r, b;
        l = mCircleCenter[0] - mPointerRadius;
        t = mCircleCenter[1] - mPointerRadius;
        r = mCircleCenter[0] + mPointerRadius;
        b = mCircleCenter[1] + mPointerRadius;
        mPointerRectF = new RectF(l, t, r, b);

        mTickRadius = mRadius - mCircleWidth - mTickLength / 2 - mSpacing;
        l = mCircleCenter[0] - mTickRadius;
        t = mCircleCenter[1] - mTickRadius;
        r = mCircleCenter[0] + mTickRadius;
        b = mCircleCenter[1] + mTickRadius;
        mTickRectF = new RectF(l, t, r, b);

        mOxygenRadius = mRadius - mCircleWidth - mTickLength - mOxygenWidth / 2 - mSpacing * 2;
        l = mCircleCenter[0] - mOxygenRadius;
        t = mCircleCenter[1] - mOxygenRadius;
        r = mCircleCenter[0] + mOxygenRadius;
        b = mCircleCenter[1] + mOxygenRadius;
        mOxygenRectF = new RectF(l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDarkFlameMaster(canvas);
    }

    private void drawDarkFlameMaster(Canvas canvas) {
        /* draw circle */
        canvas.drawCircle(mCircleCenter[0], mCircleCenter[1], mPointerRadius, mDarkPaint);

        /* draw tick mark, triangle mark and color pointer */
        int dBPointer = (int) (mPointerDegree * 119 / mGalaxyDegree);
        for (int i = 0; i <= mDecibel; i++) {
            canvas.save();
            float rotateDegree;
            rotateDegree = mStartAngle + 90 + mAPieceOfDegree * i;
            canvas.rotate(rotateDegree, mCircleCenter[0], mCircleCenter[1]);
            if (i < dBPointer) {
                mFlamePaint.setColor(ContextCompat.getColor(mCtx, R.color.blair_grey));
                canvas.drawLine(
                        mCircleCenter[0],
                        mTickRectF.top - mTickLength / 2,
                        mCircleCenter[0],
                        mTickRectF.top + mTickLength / 2,
                        mFlamePaint);
                if (i == dBPointer - 1) {
                    mMoriSummerPaint.setColor(getPointerColor(i));
                    canvas.drawLine(
                            mCircleCenter[0],
                            mPadding,
                            mCircleCenter[0],
                            mTickRectF.top + mTickLength / 2,
                            mMoriSummerPaint);
                }
            } else {
                mFlamePaint.setColor(ContextCompat.getColor(mCtx, R.color.emma_grey));
                canvas.drawLine(
                        mCircleCenter[0],
                        mTickRectF.top - mTickLength / 2,
                        mCircleCenter[0],
                        mTickRectF.top + mTickLength / 2,
                        mFlamePaint);
            }
            canvas.restore();
        }

        /* draw colorful gradient */
        SweepGradient sweepGradient = new SweepGradient(
                mCircleCenter[0],
                mCircleCenter[1],
                ContextCompat.getColor(mCtx, R.color.white),
                ContextCompat.getColor(mCtx, R.color.oxygen_green));
        mMasterPaint.setShader(sweepGradient);
        Path path = new Path();
        canvas.save();
        canvas.rotate(mStartAngle, mCircleCenter[0], mCircleCenter[1]);
        path.addArc(mOxygenRectF, 0, mGalaxyDegree);
        canvas.drawPath(path, mMasterPaint);
        canvas.restore();
    }

    public void setPointerDecibel(int value) {
        if (mDecibel == 119) {
            value = value % 119;
            System.out.println("======= value:" + value);
            float degree = mGalaxyDegree * value / 119;
            this.mPointerDegree = degree;
            startPointerAnim();
            // invalidate();
        }
    }

    public int getPointerColor(int dBPointer) {
        int sc = ContextCompat.getColor(mCtx, R.color.white);
        int ec = ContextCompat.getColor(mCtx, R.color.oxygen_green);
        int rS = Color.red(sc);
        int gS = Color.green(sc);
        int bS = Color.blue(sc);
        int rE = Color.red(ec);
        int gE = Color.green(ec);
        int bE = Color.blue(ec);
        int r = (int) (rS + (rE - rS) * 1f / (mDecibel + 1) * dBPointer);
        int g = (int) (gS + (gE - gS) * 1f / (mDecibel + 1) * dBPointer);
        int b = (int) (bS + (bE - bS) * 1f / (mDecibel + 1) * dBPointer);
        return Color.argb(255, r, g, b);
    }

    private ValueAnimator mAnimator;
    private float mLastValue = 0f;
    private float mCurrentValue = 100f;
    private boolean b = false;
    private void startPointerAnim() {
        mAnimator = ValueAnimator.ofFloat(mLastValue, mPointerDegree);
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPointerDegree = value;
                postInvalidate();
            }
        });
        mAnimator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
