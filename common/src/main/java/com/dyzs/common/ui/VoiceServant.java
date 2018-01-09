package com.dyzs.common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.dyzs.common.R;

/**
 * @author dyzs
 * Created on 2018/1/9.
 * a voice servant, voice sensor listener
 * decibel(dB) use range 0~150 or 0~120
 */

public class VoiceServant extends View{
    private float mWidth, mHeight, mPadding, mSpacing;
    private float[] mCircleCenter = new float[2];
    private float mRadius, mPointerRadius, mTickRadius, mOxygenRadius;
    private float mDegree, mAPieceOfDegree, mPointerDegree, mMinDegree, mMaxDegree;
    private RectF mPointerRectF, mTickRectF, mOxygenRectF;
    private float mCircleWidth, mTickLength, mTickWidth, mOxygenWidth;
    private Paint mDarkPaint, mFlamePaint, mMasterPaint;
    private float mStartAngle;
    private int dBType = 119;

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
    }

    private void init(Context context) {
        mPadding = 10f;
        mSpacing = 15f;
        mDegree = 280f;
        mAPieceOfDegree = mDegree / dBType;
        mStartAngle = (360f - mDegree) / 2 + 90f;
        mPointerDegree = 125f; mMinDegree = 60f; mMaxDegree = 180f; // def degree value
        mCircleWidth = 20f;
        mTickLength = 70f;
        mTickWidth = 3f;
        mOxygenWidth = 10f;

        mDarkPaint = new Paint();
        mDarkPaint.setAntiAlias(true);
        mDarkPaint.setStyle(Paint.Style.STROKE);
        mDarkPaint.setStrokeWidth(mCircleWidth);
        mDarkPaint.setColor(ContextCompat.getColor(context, R.color.stone_grey));

        mFlamePaint = new Paint();
        mFlamePaint.setAntiAlias(true);
        mFlamePaint.setStyle(Paint.Style.STROKE);
        mFlamePaint.setStrokeWidth(mTickWidth);
        mFlamePaint.setColor(ContextCompat.getColor(context, R.color.oxygen_green));

        mMasterPaint = new Paint();
        mMasterPaint.setAntiAlias(true);
        mMasterPaint.setStyle(Paint.Style.STROKE);
        mMasterPaint.setStrokeWidth(mOxygenWidth);
        mMasterPaint.setColor(ContextCompat.getColor(context, R.color.girl_pink));
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
        for (int i = 0; i <= dBType; i++) {
            canvas.save();
            canvas.rotate(90 + mAPieceOfDegree * i, mCircleCenter[0], mCircleCenter[1]);
            canvas.drawLine(mCircleCenter[0], mTickRectF.top - mTickLength / 2, mCircleCenter[0], mTickRectF.top + mTickLength / 2, mFlamePaint);
            canvas.restore();
        }
        /*Path path = new Path();
        path.addArc(mTickRectF, mStartAngle, mDegree);
        canvas.drawPath(path, mFlamePaint);*/

        /* draw colorful gradient */
        Path path = new Path();
        path.addArc(mOxygenRectF, 0, mDegree);
        canvas.drawPath(path, mMasterPaint);
    }
}
