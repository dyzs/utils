package com.dyzs.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dyzs on 2019/4/21.
 */
public class KnockBackupListSidebar extends View {
    private float mSpacing = 10f;
    private float mIndicatorWidth = 10f, mIndicatorHeight = 50f, mIndicatorBgWidth = 6f;
    private BitmapDrawable mDrawable;
    private RectF mRectIndicatorBg, mRectFTextBg;
    private int mDrawableWidth, mDrawableHeight;
    private int mWidth, mHeight;
    private Paint mPaint;
    public KnockBackupListSidebar(Context context) {
        super(context);
        init();
    }

    public KnockBackupListSidebar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KnockBackupListSidebar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_backup_date_bg);
        assert mDrawable != null;
        mDrawableWidth = mDrawable.getBitmap().getWidth();
        mDrawableHeight = mDrawable.getBitmap().getHeight();

        mRectIndicatorBg = new RectF();
        mRectFTextBg = new RectF();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = (int) (mDrawableWidth + mSpacing + mIndicatorWidth);
        mHeight = getMeasuredHeight();
        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float l = mDrawableWidth + mSpacing + (mIndicatorWidth - mIndicatorBgWidth) / 2;
        float t = 0;
        float r = mWidth - (mIndicatorWidth - mIndicatorBgWidth) / 2;
        float b = mHeight;
        mRectIndicatorBg.set(l, t, r, b);

        mPaint.setStrokeWidth(mIndicatorBgWidth);
        mPaint.setColor(Color.MAGENTA);
        canvas.drawLine(l, t, r, b, mPaint);

        l = mDrawableWidth + mSpacing;
        t = 0;
        r = mWidth;
        b = mIndicatorHeight;
        mPaint.setStrokeWidth(mIndicatorWidth);
        mPaint.setColor(Color.CYAN);
        canvas.drawLine(l, t, r, b, mPaint);

        l = 0;
        t = 0;
        r = mDrawableWidth;
        b = mDrawableHeight;
        mRectFTextBg.set(l, t, r, b);
        canvas.drawBitmap(mDrawable.getBitmap(), null, mRectFTextBg, mPaint);



    }
}
