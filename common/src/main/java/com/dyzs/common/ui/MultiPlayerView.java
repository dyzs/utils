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
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mRoundRectPaint;
    private LinearGradient mLG;

    private Paint mTestPoint;

    private float mViewWidth, mViewHeight;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth() * 1.0f;
        mViewHeight = getMeasuredHeight() * 1.0f;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startX = mViewWidth * 0.05f;
        float startY = mViewHeight * 0.6f;
        drawLine(canvas, startX, startY);

        // 计算 points
        float lineWidth = mViewWidth - startX * 2;
        // points 点的存在范围应该在 line 中，所以开始结束向内缩小 0.05f
        float totalCalcWidth = lineWidth - startX * 2;
        float aPieceOfTotal = totalCalcWidth / mList.get(mList.size() - 1).getPeople();
        mListPoints.clear();
        for (int i = 0; i < mList.size(); i ++) {
            Point point = new Point();
            point.x = (int) (mList.get(i).getPeople() * aPieceOfTotal + startX + startX);
            point.y = (int) startY;
            mListPoints.add(point);
        }

        drawPeople(canvas, startX, startY);
        drawPriceAndRect(canvas, startX, startY);

    }

    /**
     * 画黄色横线
     * @param canvas
     * @param startX
     * @param startY
     */
    private void drawLine(Canvas canvas, float startX, float startY) {
        mLinePaint.setStrokeWidth(mViewHeight * 0.1f);
        canvas.drawLine(startX, startY, mViewWidth - startX, startY, mLinePaint);
    }

    /**
     * 画人数文字
     * @param canvas
     * @param startX
     * @param startY
     */
    private void drawPeople(Canvas canvas, float startX, float startY) {
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
     * @param startX
     * @param startY
     */
    private void drawPriceAndRect(Canvas canvas, float startX, float startY) {
        if (mListPoints == null || mListPoints.size() <= 0) {return;}
        mTextPaint.setTextSize(dp2Px(10));
        float currencySymbolWidth = mTextPaint.measureText("￥");
        mTextPaint.setTextSize(dp2Px(15));
        String text;
        float textTotalWidth;
        float pointPriceY = startY / 2;// 中心点为 line 向上的一半
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
