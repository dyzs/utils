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

import java.util.ArrayList;

/**
 * Created by maidou on 2018/1/8.
 */

public class ChasingLoading extends View{
    private Context mCtx;
    private float mWidth, mHeight;

    private Paint mDark, mFlame, mMaster;
    private ArrayList<Paint> mDfmPaints;
    private ArrayList<Float> mDfmRadians;
    private float mDarkRadian, mFlameRadian, mMasterRadian;
    private float mDfmWidth, mDfmSpacing, mPadding;
    private float mStartAngle = -90;

    private Path mDfmPath;
    private ArrayList<RectF> rectFs;
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
        mDfmRadians = new ArrayList();
        mDfmRadians.add(mDarkRadian);
        mDfmRadians.add(mFlameRadian);
        mDfmRadians.add(mMasterRadian);

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

        mDfmPaints = new ArrayList<>();
        mDfmPaints.add(mDark);
        mDfmPaints.add(mFlame);
        mDfmPaints.add(mMaster);
        mDfmPath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        float l, t, r, b, radius;
        if (mWidth >= mHeight) {
            mPadding = mHeight * 0.05f;
            radius = mHeight / 2 - mPadding;
        } else {
            mPadding = mWidth * 0.05f;
            radius = mWidth / 2 - mPadding;
        }
        rectFs = new ArrayList<>();
        RectF rectF;
        for (int i = 0 ; i < 3; i++) {
            l = mWidth / 2 - radius + i * (mDfmSpacing + mDfmWidth);
            t = mHeight / 2 - radius + i * (mDfmSpacing + mDfmWidth);
            r = mWidth / 2 + radius - i * (mDfmSpacing + mDfmWidth);
            b = mHeight / 2 + radius - i * (mDfmSpacing + mDfmWidth);
            rectF = new RectF(l, t, r, b);
            rectFs.add(rectF);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDarkFlameMaster(canvas);
    }

    private void drawDarkFlameMaster(Canvas canvas) {
        if (rectFs == null)return;
        for (int i = 0; i < rectFs.size(); i++) {
            mDfmPath.reset();
            mDfmPath.addArc(rectFs.get(i), mStartAngle, mDfmRadians.get(i));
            canvas.drawPath(mDfmPath, mDfmPaints.get(i));
        }
    }

    private void startDarkFlameMaster() {

    }
}
