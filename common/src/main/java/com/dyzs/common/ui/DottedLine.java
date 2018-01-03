package com.dyzs.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.dyzs.common.R;

/**
 * @author dyzs
 * Created on 2018/1/2.
 */

public class DottedLine extends View{
    private Context mCtx;
    private float mDashGap = 10f;
    private float mDashWidth = 10f;
    private int mType = 0;// 0 vertical, 1 horizontal
    private Paint mPaint;
    private Path mPath;
    private float mWidth, mHeight;
    private int mDotColor = R.color.colorPrimary;
    public DottedLine(Context context) {
        this(context, null);
    }

    public DottedLine(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DottedLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCtx = context;
        initialized(context, attrs, defStyleAttr);
    }

    private void initialized(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DottedLine);
        mType = ta.getInteger(R.styleable.DottedLine_lineType, 0);
        mDotColor = ta.getColor(R.styleable.DottedLine_lineDotColor, ContextCompat.getColor(mCtx, R.color.oxygen_grey));
        ta.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(mDotColor);
        PathEffect effects = new DashPathEffect(new float[] { 0, 3, 10, 0}, 1);
        mPaint.setPathEffect(effects);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDash(canvas);
    }

    private void drawDash(Canvas canvas) {
        mPath = new Path();
        switch (mType) {
            case 0:
                mPaint.setStrokeWidth(mWidth);
                mPath.moveTo(mWidth / 2, 0);
                mPath.lineTo(mWidth / 2, mHeight);
                break;
            case 1:
                mPaint.setStrokeWidth(mHeight);
                mPath = new Path();
                mPath.moveTo(0, mHeight / 2);
                mPath.lineTo(mWidth, mHeight / 2);
                break;
        }
        canvas.drawPath(mPath, mPaint);
    }

    public void setType(int type) {
        this.mType = type;
        invalidate();
    }
}
