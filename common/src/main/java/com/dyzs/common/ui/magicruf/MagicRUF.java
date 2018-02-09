package com.dyzs.common.ui.magicruf;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author dyzs, created on 2018/1/31.
 *
 * PathMeasure{@link PathMeasure} learning
 * 前言：常规的绘制图形，如 drawArc，drawCircle，drawPath等, 但是无法绘出很棒的特效。
 * 而实用 PathMeasure 就能帮你轻易做到，
 * 其中的重点：{@link PathMeasure#getSegment(float, float, android.graphics.Path, boolean)};
 *
 */

public class MagicRUF extends View {
    private static final int STATE_1ST = 0, STATE_2ND = 1, STATE_3RD = 2, STATE_4TH = 3;
    private int mState = STATE_1ST;

    private float mWidth, mHeight;
    private float mPadding;
    private float mSpacingBtwOuterAndInner; // 内圆和外圆的间距
    private float mLineWidth; // 表示当前的画笔的宽度
    private float mRadius; // 当前去除 padding 的半径
    private float mRadiusOuter; // outer circle 的实际半径

    private float mRadiusInner; // 内圆的实际半径
    private float[] mCenterPoint;
    private Path mPathOuterCircle, mPathInnerCircle, mPathTriangle, mPathTriangle2;
    private PathMeasure mPathMeasure;
    private Path mReactPath; // 用于截取 path measure 的 path 路径
    private Paint mPaint;

    private ValueAnimator mAnimator;
    public MagicRUF(Context context) {
        this(context, null);
    }

    public MagicRUF(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MagicRUF(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        startAnimation();
    }

    public void init() {
        mLineWidth = 10f;
        mPadding = 10f;
        if (mLineWidth > mPadding) {
            mPadding = mLineWidth;
        }
        mSpacingBtwOuterAndInner = 10f;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setColor(Color.CYAN);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
        mPaint.setShadowLayer(20, 0, 0, Color.WHITE);
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

        mRadiusOuter = mRadius - mLineWidth / 2;
        mRadiusInner = mRadius - mLineWidth - mSpacingBtwOuterAndInner - mLineWidth / 2;

        /*初始化 rect, path 路径*/
        initPath();
    }

    private void initPath() {
        mPathMeasure = new PathMeasure();
        mReactPath = new Path();
        RectF rectF;
        float l, t, r, b;
        // step1: 初始化两个圆的 path 路径
        l = mCenterPoint[0] - mRadiusOuter;
        t = mCenterPoint[1] - mRadiusOuter;
        r = mCenterPoint[0] + mRadiusOuter;
        b = mCenterPoint[1] + mRadiusOuter;
        rectF = new RectF(l ,t, r, b);
        mPathOuterCircle = new Path();
        mPathOuterCircle.addArc(rectF, 0, -359.9f);

        l = mCenterPoint[0] - mRadiusInner;
        t = mCenterPoint[1] - mRadiusInner;
        r = mCenterPoint[0] + mRadiusInner;
        b = mCenterPoint[1] + mRadiusInner;
        rectF = new RectF(l ,t, r, b);
        mPathInnerCircle = new Path();
        mPathInnerCircle.addArc(rectF, 60, -359.9f);

        // step2: 设置 triangle 的 path 路径
        mPathTriangle = new Path();
        mPathMeasure.setPath(mPathInnerCircle, false);// 设置当前的 path 路径，保证 triangle 可以有起始位置
        float[] pos = new float[2];
        mPathMeasure.getPosTan(0, pos, null);// 获取初始位置的坐标，表示获取当前内圆的起始点
        mPathTriangle.moveTo(pos[0], pos[1]);
        System.out.println("init pos : " + pos[0] + "  " + pos[1]);

        mPathMeasure.getPosTan(1f / 3f * mPathMeasure.getLength(), pos, null);// 获取当前内圆的 path 路径在 1/3 处的点的坐标
        mPathTriangle.lineTo(pos[0], pos[1]);
        System.out.println("one-third pos : " + pos[0] + "  " + pos[1]);

        mPathMeasure.getPosTan(2f / 3f * mPathMeasure.getLength(), pos, null);
        mPathTriangle.lineTo(pos[0], pos[1]);
        System.out.println("two-third pos : " + pos[0] + "  " + pos[1]);
        mPathTriangle.close();

        // step3: 设置 triangle2 的 path 路径
        mPathMeasure.getPosTan(2f / 3f * mPathMeasure.getLength(), pos, null);
        mPathTriangle2 = new Path();
        Matrix matrix = new Matrix();
        matrix.postRotate(-180, mCenterPoint[0], mCenterPoint[1]);// 设置矩阵绕圆心点旋转
        mPathTriangle.transform(matrix, mPathTriangle2);
        // TODO: 2018/2/9 postRotate and transform
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mReactPath.reset();
        // 使用 path measure 代替当前 path 路径, 绘制想要的图形
        canvas.save();

        switch (mState) {
            case STATE_1ST:
                mReactPath.reset();
                mPathMeasure.setPath(mPathOuterCircle, false);
                mPathMeasure.getSegment(0, perTimePathValue * mPathMeasure.getLength(), mReactPath, true);
                canvas.drawPath(mReactPath, mPaint);

                mReactPath.reset();
                mPathMeasure.setPath(mPathInnerCircle, false);
                mPathMeasure.getSegment(0, perTimePathValue * mPathMeasure.getLength(), mReactPath, true);
                canvas.drawPath(mReactPath, mPaint);
                break;
            case STATE_2ND:
                canvas.drawPath(mPathOuterCircle, mPaint);
                canvas.drawPath(mPathInnerCircle, mPaint);

                mReactPath.reset();
                mPathMeasure.setPath(mPathTriangle, false);
                float stopD = perTimePathValue * mPathMeasure.getLength();
                float startD = stopD - (0.5f - Math.abs(0.5f - perTimePathValue)) * 200;
                mPathMeasure.getSegment(startD, stopD, mReactPath, true);
                canvas.drawPath(mReactPath, mPaint);

                mReactPath.reset();
                mPathMeasure.setPath(mPathTriangle2, false);
                mPathMeasure.getSegment(startD, stopD, mReactPath, true);
                canvas.drawPath(mReactPath, mPaint);
                break;
            case STATE_3RD:
                canvas.drawPath(mPathOuterCircle, mPaint);
                canvas.drawPath(mPathInnerCircle, mPaint);

                mReactPath.reset();
                mPathMeasure.setPath(mPathTriangle, false);
                mPathMeasure.getSegment(0, perTimePathValue * mPathMeasure.getLength(), mReactPath, true);
                canvas.drawPath(mReactPath, mPaint);

                mReactPath.reset();
                mPathMeasure.setPath(mPathTriangle2, false);
                mPathMeasure.getSegment(0, perTimePathValue * mPathMeasure.getLength(), mReactPath, true);
                canvas.drawPath(mReactPath, mPaint);
                break;
        }

        canvas.restore();


    }


    private float perTimePathValue = 0;
    private void startAnimation() {
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                perTimePathValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                switch (mState) {
                    case STATE_1ST:
                        mState = STATE_2ND;
                        break;
                    case STATE_2ND:
                        mState = STATE_3RD;
                        break;
                    case STATE_3RD:
                        mState = STATE_1ST;
                        break;
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
}
