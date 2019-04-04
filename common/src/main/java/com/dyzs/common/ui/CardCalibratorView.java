package com.dyzs.common.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.dyzs.common.R;

/**
 * Created by dyzs on 2019/1/25.
 */
public class CardCalibratorView extends View {

    private float mWidth, mHeight;
    private float mPadding, mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;
    private float mRectStorkWidth, mRectCornerRadius;
    private int mCardRegionColor, mCardRectStorkColor;

    private Path mRectPath;
    private RectF mRectFVenue, mRectFArena;
    private Paint mPaint;
    private Xfermode mXFerMode_DST_OUT, mXFerMode_SRC;
    private float mArenaRatio;
    private float mArenaWidth, mArenaHeight;

    private static final int[] SYS_ATTRS = {
            android.R.attr.padding,
            android.R.attr.paddingLeft,
            android.R.attr.paddingTop,
            android.R.attr.paddingRight,
            android.R.attr.paddingBottom,
            android.R.attr.background,
    };

    public CardCalibratorView(Context context) {
        this(context, null);
    }

    public CardCalibratorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CardCalibratorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mArenaWidth = mWidth - mPaddingLeft - mPaddingRight;
        mArenaHeight = mArenaWidth / mArenaRatio;

        mHeight = getMeasuredHeight();
        float tempHeight = mPaddingTop + mArenaHeight + mPaddingBottom;
        if (tempHeight > mHeight) {
            mHeight = tempHeight;
        }
        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, SYS_ATTRS);
        mPadding = ta.getDimension(0, 0f);
        mPaddingLeft = ta.getDimension(1, 0f);
        mPaddingTop = ta.getDimension(2, 0f);
        mPaddingRight = ta.getDimension(3, 0f);
        mPaddingBottom = ta.getDimension(4, 0f);
        if (mPadding != 0) {
            mPaddingLeft = mPadding;
            mPaddingTop = mPadding;
            mPaddingRight = mPadding;
            mPaddingBottom = mPadding;
        }

        ta.recycle();
        ta = context.obtainStyledAttributes(attrs, R.styleable.CardCalibratorView);
        mRectStorkWidth = ta.getDimension(R.styleable.CardCalibratorView_ccvCardRectStorkWidth, dp2Px(10f));
        mRectCornerRadius = ta.getDimension(R.styleable.CardCalibratorView_ccvCardRectCornerRadius, dp2Px(8f));
        mCardRegionColor = ta.getColor(R.styleable.CardCalibratorView_ccvCardRegionColor, ContextCompat.getColor(getContext(), R.color.half_white));
        mCardRectStorkColor = ta.getColor(R.styleable.CardCalibratorView_ccvCardRectStorkColor, ContextCompat.getColor(getContext(), R.color.white));
        mArenaRatio = ta.getFloat(R.styleable.CardCalibratorView_ccvCardRatio, 4f/3f);
        ta.recycle();


        mRectFVenue = new RectF();
        mRectFArena = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mXFerMode_DST_OUT = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mXFerMode_SRC = new PorterDuffXfermode(PorterDuff.Mode.SRC);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int layerId = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
        // draw card cover
        mRectFVenue.set(0, 0, mWidth, mHeight);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCardRegionColor);
        canvas.drawRect(mRectFVenue, mPaint);

        // draw arena
        mRectFArena.set(mPaddingLeft, mPaddingTop, mPaddingLeft + mArenaWidth, mPaddingTop + mArenaHeight);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.MAGENTA);
        mPaint.setXfermode(mXFerMode_DST_OUT);
        canvas.drawRoundRect(mRectFArena, mRectCornerRadius, mRectCornerRadius, mPaint);

        // draw rect border
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCardRectStorkColor);
        mPaint.setStrokeWidth(mRectStorkWidth);
        mPaint.setXfermode(mXFerMode_SRC);
        canvas.drawRoundRect(mRectFArena, mRectCornerRadius, mRectCornerRadius, mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(layerId);


    }

    private float dp2Px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
