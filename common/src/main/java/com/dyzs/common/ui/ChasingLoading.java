package com.dyzs.common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by maidou on 2018/1/8.
 */

public class ChasingLoading extends View{
    private Context mCtx;
    private float mViewWidth, mViewHeight;

    private Paint mDark, mFlame, mMaster;
    private float mDarkRadian, mFlameRadian, mMasterRadian;
    private float mDfmWidth, mDfmSpacing;

    private Path mDfmPath;
    private Region[] regions;
    public ChasingLoading(Context context) {
        this(context, null);
    }

    public ChasingLoading(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ChasingLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mCtx = context;
        this.mDfmWidth = 10f;
        this.mDfmSpacing = 5f;
        this.mDarkRadian = 360f / 100 * 35;
        this.mFlameRadian = 360f / 100 * 30;
        this.mMasterRadian = 360f / 100 * 25;

        mDark = new Paint();
        mDark.setAntiAlias(true);
        mDark.setStyle(Paint.Style.FILL);
        mDark.setStrokeWidth(mDfmWidth);
        mDark.setStrokeCap(Paint.Cap.ROUND);
        mDark.setColor(Color.RED);

        mFlame = new Paint();
        mFlame.setAntiAlias(true);
        mFlame.setStyle(Paint.Style.FILL);
        mFlame.setStrokeWidth(mDfmWidth);
        mFlame.setStrokeCap(Paint.Cap.ROUND);
        mFlame.setColor(Color.CYAN);

        mMaster = new Paint();
        mMaster.setAntiAlias(true);
        mMaster.setStyle(Paint.Style.FILL);
        mMaster.setStrokeWidth(mDfmWidth);
        mMaster.setStrokeCap(Paint.Cap.ROUND);
        mMaster.setColor(Color.CYAN);

        mDfmPath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measure(-1, -1);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();

        float l, t, r, b, radius;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDarkFlameMaster(canvas);
    }

    private void drawDarkFlameMaster(Canvas canvas) {

    }

    private void startDarkFlameMaster() {

    }
}
