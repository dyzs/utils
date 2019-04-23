package com.dyzs.common.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.dyzs.common.R;
import com.dyzs.common.utils.FontMatrixUtils;
import com.dyzs.common.utils.LogUtils;

/**
 * Created by dyzs on 2019/4/21.
 */
public class KnockBackupListSidebar extends View {
    private static final String TAG = KnockBackupListSidebar.class.getSimpleName();
    private float mSpacing = 10f;
    private float mIndicatorWidth = 10f, mIndicatorHeight = 50f, mIndicatorBgWidth = 6f;
    private Bitmap mDrawable;
    private RectF mRectIndicatorBg, mRectFTextBg;
    private int mDrawableWidth, mDrawableHeight;
    private int mWidth, mHeight;
    private Paint mPaint;
    private String mTextDate = "2019-04-22 09:22";
    private float mTextSize = 12f;
    private int mItemCount = 4;
    private SparseArray<String> mStringDateSparseArray = new SparseArray<>();

    private RectF mStartTouchingArea = new RectF();
    public KnockBackupListSidebar(Context context) {
        this(context, null);
    }

    public KnockBackupListSidebar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public KnockBackupListSidebar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.KnockBackupListSidebar);

        ta.recycle();
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

        mStartTouchingArea.set(0, 0, mWidth, mHeight);
        mIndicatorHeight = mHeight / mItemCount;

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
        drawIndicator(canvas);

        // draw date
        drawPartOfDate(canvas);

    }

    private void drawIndicator(Canvas canvas) {
        float startX, startY, stopX, stopY;
        if (mStartTouching) {
            startX = mDrawableWidth + mSpacing + mIndicatorWidth / 2;
            startY = mTouchY - mIndicatorHeight / 2;// mCurrentIndex * mIndicatorHeight;
            stopX = startX;
            stopY = mTouchY + mIndicatorHeight / 2;//(mCurrentIndex + 1) * mIndicatorHeight;
        } else {
            startX = mDrawableWidth + mSpacing + mIndicatorWidth / 2;
            startY = mCurrentIndex * mIndicatorHeight;
            stopX = startX;
            stopY = (mCurrentIndex + 1) * mIndicatorHeight;
        }
        mPaint.setStrokeWidth(mIndicatorWidth);
        mPaint.setColor(Color.CYAN);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
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

    public void setDateArray(@NonNull SparseArray<String> stringSparseArray) {
        if (stringSparseArray.size() > 0) {
            this.mStringDateSparseArray = stringSparseArray;
            setItemCount(mStringDateSparseArray.size());
            if (mCurrentIndex >= mStringDateSparseArray.size()) {
                mCurrentIndex = mStringDateSparseArray.size() - 1;
            }
        }
        invalidate();
    }

    private void setItemCount(int itemCount) {
        mItemCount = itemCount;
    }

    public void setCurrentIndex(int index) {
        mCurrentIndex = index;
        if (mCurrentIndex >= mStringDateSparseArray.size()) {
            mCurrentIndex = mStringDateSparseArray.size() - 1;
        }
        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
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

        LogUtils.v(TAG, "eventY:["+eventY+"]");
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

    private int mTouchY = -1;
    private int getSelectedIndex(float eventY) {
        mTouchY = (int) eventY;
        if (mTouchY <= mIndicatorHeight / 2) {
            mTouchY = (int) (mIndicatorHeight / 2);
            return 0;
        }
        if (mTouchY >= mHeight - mIndicatorHeight / 2) {
            mTouchY = (int) (mHeight - mIndicatorHeight / 2);
        }

        int index = (int) (mTouchY / this.mIndicatorHeight);
        if (index >= this.mItemCount) {
            index = this.mItemCount - 1;
        }
        return index;
    }
}
