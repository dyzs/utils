package com.dyzs.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dyzs.common.utils.FontMatrixUtils;

/**
 * Created by dyzs on 2019/4/21.
 */
public class KnockBackupListSidebar extends View {
    private float mSpacing = 10f;
    private float mIndicatorWidth = 10f, mIndicatorHeight = 50f, mIndicatorBgWidth = 6f;
    private Bitmap mDrawable;
    private RectF mRectIndicatorBg, mRectFTextBg;
    private int mDrawableWidth, mDrawableHeight;
    private int mWidth, mHeight;
    private Paint mPaint;
    private String mTextDate = "2019-04-22 09:22";
    private float mTextSize = 12f;
    private int mItemCount = 5;

    private RectF mStartTouchingArea = new RectF();
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
        // mDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_backup_date_bg);
        mDrawable = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_backup_date_bg);
        assert mDrawable != null;
        mDrawableWidth = getResources().getDimensionPixelSize(R.dimen.backup_list_sidebar_drawable_width);
        mDrawableHeight = getResources().getDimensionPixelSize(R.dimen.backup_list_sidebar_drawable_height);
        mIndicatorHeight = mDrawableHeight;

        mTextSize = getResources().getDimensionPixelSize(R.dimen.backup_list_sidebar_text_size);
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

        mStartTouchingArea.set(mWidth / 6 * 5, 0, mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw indicator background
        float startX, startY, stopX, stopY;
        startX = mDrawableWidth + mSpacing + mIndicatorWidth / 2;
        startY = 0;
        stopX = startX;
        stopY = mHeight;
        mPaint.setStrokeWidth(mIndicatorBgWidth);
        mPaint.setColor(Color.MAGENTA);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        // draw indicator
        mIndicatorHeight = mHeight / mItemCount;
        startX = mDrawableWidth + mSpacing + mIndicatorWidth / 2;
        startY = mCurrentIndex * mIndicatorHeight;
        stopX = startX;
        stopY = (mCurrentIndex + 1) * mIndicatorHeight;
        mPaint.setStrokeWidth(mIndicatorWidth);
        mPaint.setColor(Color.CYAN);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        // draw date
        drawPartOfDate(canvas);

    }

    private void drawPartOfDate(Canvas canvas) {
        if (!mStartTouching) {
            return;
        }
        float l;
        float t;
        float r;
        float b;
        l = 0;
        t = mCurrentIndex * mIndicatorHeight + (mIndicatorHeight / 2 - mDrawableHeight / 2);
        r = mDrawableWidth;
        b = mCurrentIndex * mIndicatorHeight + (mIndicatorHeight / 2 - mDrawableHeight / 2) + mDrawableHeight;
        mRectFTextBg.set(l, t, r, b);
        canvas.drawBitmap(mDrawable, null, mRectFTextBg, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(3f);

        float textWidth = mPaint.measureText(mTextDate) * 1.0f;
        float textHalfHeight = FontMatrixUtils.calcTextHalfHeightPoint(mPaint);
        float textX = (r - l) / 2 - textWidth / 2;
        float textY = (b - t) / 2 + textHalfHeight / 2 + mCurrentIndex * mIndicatorHeight + (mIndicatorHeight - mDrawableHeight) / 2;
        canvas.drawText(mTextDate, textX, textY, mPaint);
    }

    public void setItemCount(int itemCount) {
        mItemCount = itemCount;
        invalidate();
    }

    private int mCurrentIndex = 0;
    private boolean mStartTouching = false;
    private boolean mLazyRespond = false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mItemCount == 0) {
            return super.onTouchEvent(event);
        }
        float eventY = event.getY();
        float eventX = event.getX();
        mCurrentIndex = getSelectedIndex(eventY);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mStartTouchingArea.contains(eventX, eventY)) {
                    mStartTouching = true;
                    /*if (!mLazyRespond && onSelectIndexItemListener != null) {
                        onSelectIndexItemListener.onSelectIndexItem(mIndexItems[mCurrentIndex]);
                    }*/
                    invalidate();
                    return true;
                } else {
                    mCurrentIndex = -1;
                    return false;
                }

            case MotionEvent.ACTION_MOVE:
                /*if (mStartTouching && !mLazyRespond && onSelectIndexItemListener != null) {
                    onSelectIndexItemListener.onSelectIndexItem(mIndexItems[mCurrentIndex]);
                }*/
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                /*if (mLazyRespond && onSelectIndexItemListener != null) {
                    onSelectIndexItemListener.onSelectIndexItem(mIndexItems[mCurrentIndex]);
                }*/
                mStartTouching = false;
                invalidate();
                /*if (mCancelListener != null) {
                    mCancelListener.onSelectCancel();
                }*/
                return true;
        }
        return super.onTouchEvent(event);
    }

    private int mCurrentY = -1;
    private int getSelectedIndex(float eventY) {
        mCurrentY = (int) eventY;
        if (mCurrentY <= 0) {
            return 0;
        }

        int index = (int) (mCurrentY / this.mIndicatorHeight);
        if (index >= this.mItemCount) {
            index = this.mItemCount - 1;
        }
        return index;
    }
}
