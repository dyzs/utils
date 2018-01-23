package com.dyzs.common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


import com.dyzs.common.utils.FontMatrixUtils;

import java.util.ArrayList;

/**
 * Created by maidou on 2017/12/21.
 */
public class MultiPlayerView extends View{
    private Context mCtx;
    private ArrayList<ViewItem> mList;

    private ArrayList<Point> mListPoints;
    private ArrayList<Rect> mListRoundRect;
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mRoundRectPaint;
    private LinearGradient mLG;

    private Paint mTestPoint;

    private float mViewWidth, mViewHeight;
    private float mStartX = 10f;// line start x
    private float mStartY = 10f;// line start y
    private float mPointStartX = 11f;// point start X
    private float mPointEndX = 99f;// point end x
    private int selection = -1;// init selection
    private float mLineWidth = 100f;// total line width
    private float mTotalPointsWidth = 90f;// total points width
    private float mPointSpacingWidth = 10f;// the width between point and point

    private float offsetX = 0f, downX;

    public MultiPlayerView(Context context) {
        this(context, null);
    }

    public MultiPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MultiPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCtx = context;

        initialize();

        testLoadData();
    }
    private void testLoadData() {
        ViewItem viewItem = new ViewItem();
        viewItem.setPeople(3);
        viewItem.setPrice(10 + "");
        mList.add(viewItem);
        viewItem = new ViewItem();
        viewItem.setPeople(13);
        viewItem.setPrice(25 + "");
        mList.add(viewItem);
        viewItem = new ViewItem();
        viewItem.setPeople(30);
        viewItem.setPrice(50 + "");
        mList.add(viewItem);
    }

    private void initialize() {
        mList = new ArrayList<>();
        mListPoints = new ArrayList<>();
        mListRoundRect = new ArrayList<>();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.YELLOW);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeWidth(10f);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStrokeWidth(4f);
        mTextPaint.setTextSize(dp2Px(15));// 初始化 15sp

        mLG = new LinearGradient(0, 0, 100, 100, Color.MAGENTA, Color.BLACK, Shader.TileMode.MIRROR);
        mRoundRectPaint = new Paint();
        mRoundRectPaint.setAntiAlias(true);
        mRoundRectPaint.setColor(Color.MAGENTA);
        mRoundRectPaint.setShader(mLG);



        mTestPoint = new Paint();
        mTestPoint.setAntiAlias(true);
        mTestPoint.setColor(Color.BLACK);
        mTestPoint.setStrokeWidth(4f);
    }

    /**
     * calc initialize value
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth() * 1.0f;
        mViewHeight = getMeasuredHeight() * 1.0f;

        mLinePaint.setStrokeWidth(mViewHeight * 0.1f);

        mStartX = mViewWidth * 0.05f;
        mStartY = mViewHeight * 0.6f;
        mLineWidth = mViewWidth - mStartX * 2;
        // 计算 points, points 点的存在范围应该在 line 中，所以开始结束向内缩小 0.05f
        mTotalPointsWidth = mLineWidth - mStartX * 2;
        mPointSpacingWidth = mTotalPointsWidth / mList.get(mList.size() - 1).getPeople();

        mPointStartX = mPointSpacingWidth * 1 + mStartX * 2;
        mPointEndX = mTotalPointsWidth;

        // 计算点数据的距离
        if (mList != null && mList.size() > 0) {
            mListPoints.clear();
            Point point;
            for (int i = 0; i < mList.size(); i ++) {
                point = new Point();
                point.x = (int) (mList.get(i).getPeople() * mPointSpacingWidth + mStartX * 2);
                point.y = (int) mStartY;
                mListPoints.add(point);
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLineAndPoints(canvas);
        /* calc point include offset */
        if (mListPoints != null && mListPoints.size() > 0) {
            for (int i = 0; i < mListPoints.size(); i ++) {
                if (i == selection) {
                    mListPoints.get(i).x += offsetX;
                }
            }
        }
        drawPeople(canvas);
        drawPriceAndRect(canvas);
    }

    /**
     * 画黄色横线
     * @param canvas
     */
    private void drawLineAndPoints(Canvas canvas) {
        canvas.drawLine(mStartX, mStartY, mViewWidth - mStartX, mStartY, mLinePaint);
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.get(mList.size() - 1).getPeople(); i++) {
                canvas.drawCircle(mPointSpacingWidth * (i + 1) + mStartX * 2, mStartY, 5f, mTextPaint);
            }
        }
    }

    /**
     * 画人数文字
     * @param canvas
     */
    private void drawPeople(Canvas canvas) {
        if (mListPoints == null || mListPoints.size() <= 0) {return;}
        for (int i = 0; i < mListPoints.size(); i ++) {
            String text = mList.get(i).getPeople() + "人";
            float textTotalWidth = mTextPaint.measureText(text);
            canvas.drawPoint(mListPoints.get(i).x, mListPoints.get(i).y, mTestPoint);
            canvas.drawText(
                    text,
                    mListPoints.get(i).x - textTotalWidth / 2,
                    mListPoints.get(i).y + mViewHeight * 0.1f + FontMatrixUtils.calcTextHalfHeightPoint(mTextPaint),
                    mTextPaint);
        }
    }

    /**
     * todo 计算圆角 rect 画出来
     * @param canvas
     */
    private void drawPriceAndRect(Canvas canvas) {
        if (mListPoints == null || mListPoints.size() <= 0) {return;}
        mListRoundRect.clear();
        mTextPaint.setTextSize(dp2Px(10));
        float currencySymbolWidth = mTextPaint.measureText("￥");
        mTextPaint.setTextSize(dp2Px(15));
        String text;
        float textTotalWidth;
        float pointPriceY = mStartY / 2;// 中心点为 line 向上的一半
        float textHeight = FontMatrixUtils.calcTextHalfHeightPoint(mTextPaint);
        Rect rect;
        for (int i = 0; i < mListPoints.size(); i ++) {
            text = mList.get(i).getPrice();
            textTotalWidth = currencySymbolWidth + mTextPaint.measureText(text);

            // draw round rect
            rect = new Rect();  // 以中心点，左上右下扩展
            int l = (int) (mListPoints.get(i).x - textTotalWidth / 2 - textHeight);
            int t = (int) (pointPriceY - textHeight * 3 / 2);
            int r = (int) (mListPoints.get(i).x + textTotalWidth / 2 + textHeight);
            int b = (int) (pointPriceY + textHeight * 3 / 2);
            rect.set(l, t, r, b);
            canvas.drawRoundRect(new RectF(rect), dp2Px(5), dp2Px(5), mRoundRectPaint);
            mListRoundRect.add(rect);

            // 绘一个矩形，中心点是 b，然后旋转 90 °
            canvas.save();
            Path path = new Path();
            path.moveTo(mListPoints.get(i).x - dp2Px(5), b);
            path.lineTo(mListPoints.get(i).x + dp2Px(5), b);
            path.lineTo(mListPoints.get(i).x, b + dp2Px(5));
            canvas.drawPath(path, mRoundRectPaint);
            /*l = (int) (mListPoints.get(i).x - dp2Px(5));
            t = (int) (b - dp2Px(5));
            r = (int) (mListPoints.get(i).x + dp2Px(5));
            b = (int) (b + dp2Px(5));
            canvas.drawRect(l, t, r, b, mTextPaint);*/
            canvas.restore();


            // draw currency symbol text
            mTextPaint.setTextSize(dp2Px(10));
            canvas.drawText(
                    "￥",
                    mListPoints.get(i).x - textTotalWidth / 2,
                    pointPriceY + textHeight / 2,
                    mTextPaint);

            mTextPaint.setTextSize(dp2Px(15));
            canvas.drawText(
                    text,
                    mListPoints.get(i).x - (textTotalWidth - currencySymbolWidth) / 2 + dp2Px(3),
                    pointPriceY + textHeight / 2,
                    mTextPaint);


            // draw test center point
            canvas.drawPoint(mListPoints.get(i).x, pointPriceY, mTestPoint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                handleActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(event);
                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }

    private void handleActionDown(MotionEvent event) {
        selection = -1;
        int dX = (int) event.getX();
        int dY = (int) event.getY();
        for (int i = 0; i < mListRoundRect.size(); i++) {
            Rect rect = mListRoundRect.get(i);
            if (dX > rect.left && dX < rect.right) {
                if (dY > rect.top && dY < rect.bottom) {
                    selection = i;
                    break;
                }
            }
        }
    }

    private boolean isMoving = false;
    private void handleActionUp(MotionEvent event) {
        offsetX = 0f;
        if (isMoving == false) {
            int upx = (int) event.getX();
            int upy = (int) event.getY();
            for (int i = 0; i < mListRoundRect.size(); i++) {
                Rect rect = mListRoundRect.get(i);
                if (upx > rect.left && upx < rect.right) {
                    if (upy > rect.top && upy < rect.bottom) {
                        System.out.println("click item:" + (i + 1));
                    }
                }
            }
        }
    }

    private void handleActionMove(MotionEvent event) {
        float moveX = event.getX();
        offsetX = moveX - downX;
        downX = moveX;
        invalidate();
    }

    private float dp2Px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mCtx.getResources().getDisplayMetrics());
    }

    public class ViewItem {
        private int people = 0;
        private String price = "";
        public int getPeople() {
            return people;
        }

        public void setPeople(int people) {
            this.people = people;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
