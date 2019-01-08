package com.dyzs.common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class DarkArcView extends View {

    private float mWidth, mHeight;
    private Paint mPaint;
    private Rect mRect;
    private Path mPath;

    public DarkArcView(Context context) {
        super(context);
        init();
    }

    public DarkArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DarkArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setShadowLayer(2, 0, 0, Color.BLUE);
        mRect = new Rect();
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int left = 0;
        int right = (int) (mHeight / 3 * 2);
        int top = (int) (mHeight / 3 * 2);
        int bottom = (int) mHeight;
        mRect.left = 0;
        mRect.right = (int) mWidth;
        mRect.top = (int) (mHeight / 3 * 2);
        mRect.bottom = (int) mHeight;

        canvas.drawRect(mRect, mPaint);


        mRect.set(0, 0, right, top);
        mPath.addArc(new RectF(0, 0, right, top), -90,180);

        canvas.drawPath(mPath, mPaint);

    }
}
