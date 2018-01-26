package com.dyzs.common.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.dyzs.common.R;

import java.util.ArrayList;

/**
 * @author dyzs
 * Created on 2018/1/8.
 * InterPolator:
 * LinearInterPolator   以常量速率改变
 * DecelerateInterpolator   在动画开始的地方快然后慢
 * AccelerateInterpolator   在动画开始的地方速率改变比较慢，然后开始加速
 * AccelerateDecelerateInterpolator     在动画开始与介绍的地方速率改变比较慢，在中间的时候加速
 * AnticipateInterpolator   开始的时候向后然后向前甩
 * AnticipateOvershootInterpolator      开始的时候向后然后向前甩一定值后返回最后的值
 * BounceInterpolator   动画结束的时候弹起
 * CycleInterpolator    动画循环播放特定的次数，速率改变沿着正弦曲线
 * OvershootInterpolator    向前甩一定值后再回到原来位置
 * @notice this view already moved to ChasingLoading repository @url{link=https://github.com/dyzs/ChasingLoading}
 */

public class ChasingLoading extends View{
    private Context mCtx;
    private float mWidth, mHeight;
    private Paint mDfmPaint;
    private ArrayList<Paint> mDfmPaints;
    private ArrayList<Float> mDfmAngles;
    private float mDfmAngle;
    private float mDfmWidth, mDfmSpacing, mPadding;
    private float mDarkStartAngle, mFlameStartAngle, mMasterStartAngle;
    private int mDarkSpeedRate, mFlameSpeedRate, mMasterSpeedRate;
    private ArrayList<Float> mStartAngleValues;
    private Path mDfmPath;
    private ArrayList<RectF> mRectFs;
    private boolean mChasing;
    private ValueAnimator mAnimator;
    private static final int[] COLORS = {Color.RED, Color.CYAN, Color.BLACK};
    private static final int[] SYS_ATTRS = {android.R.attr.padding};
    public ChasingLoading(Context context) {
        this(context, null);
    }

    public ChasingLoading(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ChasingLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public ChasingLoading(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mCtx = context;
        init(context, attrs);
        startDarkFlameMaster();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, SYS_ATTRS);
        mPadding = ta.getDimension(0, 5f);
        ta.recycle();
        ta = context.obtainStyledAttributes(attrs, R.styleable.ChasingLoading);
        mDfmWidth = ta.getDimension(R.styleable.ChasingLoading_clDfmWidth, 10f);
        mDfmSpacing = ta.getDimension(R.styleable.ChasingLoading_clDfmSpacing, 10f);
        mDarkSpeedRate = ta.getInteger(R.styleable.ChasingLoading_clDsRate, 4);
        mFlameSpeedRate = ta.getInteger(R.styleable.ChasingLoading_clFsRate, 5);
        mMasterSpeedRate = ta.getInteger(R.styleable.ChasingLoading_clMsRate, 6);
        mDarkStartAngle =  ta.getInteger(R.styleable.ChasingLoading_clDsAngle, 0);
        mFlameStartAngle =  ta.getInteger(R.styleable.ChasingLoading_clFsAngle, 135);
        mMasterStartAngle =  ta.getInteger(R.styleable.ChasingLoading_clMsAngle, 260);
        mChasing = ta.getBoolean(R.styleable.ChasingLoading_clChasing, true);
        ta.recycle();

        mStartAngleValues = new ArrayList<>();
        mStartAngleValues.add(mDarkStartAngle);
        mStartAngleValues.add(mFlameStartAngle);
        mStartAngleValues.add(mMasterStartAngle);

        mDfmPaints = new ArrayList<>();
        mDfmAngles = new ArrayList();
        for (int i = 0; i < mStartAngleValues.size(); i++) {
            // add arc paint 添加画笔数组
            mDfmPaint = new Paint();
            mDfmPaint.setAntiAlias(true);
            mDfmPaint.setStyle(Paint.Style.STROKE);
            mDfmPaint.setStrokeWidth(mDfmWidth);
            mDfmPaint.setStrokeCap(Paint.Cap.ROUND);
            mDfmPaint.setColor(COLORS[i]);
            mDfmPaints.add(mDfmPaint);

            /* add arc radian 计算圆弧的角度 */
            mDfmAngle = 360f / 100 * Math.abs(30 - 5 * i) % 360;
            mDfmAngles.add(mDfmAngle);
        }

        mDfmPath = new Path();
        mRectFs = new ArrayList<>();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        float l, t, r, b, radius;
        if (mWidth >= mHeight) {
            radius = mHeight / 2 - mPadding;
        } else {
            radius = mWidth / 2 - mPadding;
        }
        RectF rectF;
        for (int i = 0 ; i < 3; i++) {
            l = mWidth / 2 - radius + i * (mDfmSpacing + mDfmWidth);
            t = mHeight / 2 - radius + i * (mDfmSpacing + mDfmWidth);
            r = mWidth / 2 + radius - i * (mDfmSpacing + mDfmWidth);
            b = mHeight / 2 + radius - i * (mDfmSpacing + mDfmWidth);
            rectF = new RectF(l, t, r, b);
            mRectFs.add(rectF);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDarkFlameMaster(canvas);
    }

    private void drawDarkFlameMaster(Canvas canvas) {
        if (mRectFs == null)return;
        for (int i = 0; i < mRectFs.size(); i++) {
            mDfmPath.reset();
            mDfmPath.addArc(mRectFs.get(i), mStartAngleValues.get(i), mDfmAngles.get(i));
            canvas.drawPath(mDfmPath, mDfmPaints.get(i));
        }
    }

    private void startDarkFlameMaster() {
        if (!mChasing)return;
        mAnimator = ValueAnimator.ofFloat(360);
        mAnimator.setDuration(5000);
        mAnimator.setRepeatCount(-1);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mStartAngleValues.clear();
                mStartAngleValues.add(mDarkStartAngle + value * mDarkSpeedRate % 360);
                mStartAngleValues.add(mFlameStartAngle + value * mFlameSpeedRate % 360);
                mStartAngleValues.add(mMasterStartAngle + value * mMasterSpeedRate % 360);
                postInvalidate();
            }
        });
        mAnimator.start();
    }

    public void setChasing(boolean b) {
        mChasing = b;
        if (mChasing) {
            startDarkFlameMaster();
            return;
        }
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }
}
