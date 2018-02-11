package com.dyzs.common.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
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
import java.util.Collections;
import java.util.Random;

/**
 * Created by maidou on 2017/12/21.
 */
public class LineChartViewForYinJiVer2 extends View{
    private Context mCtx;
    private ArrayList<ViewItem> mListData;

    private ArrayList<Point> mListPoints;
    private ArrayList<Rect> mListRoundRect;
    private Paint mLineXYPaint;
    private Paint mTextPaint;
    private Paint mRoundRectPaint;
    private LinearGradient mLG;

    private Paint mTestPoint;

    private float mViewWidth, mViewHeight;
    private float mXAxisStart = 0f;// x 轴的起始位置
    private float mXAxisTerminal = 10f;// x 轴的终点位置
    private float mYAxisStart = 10f;// y 轴的起始位置
    private float mYAxisTerminal = 10f;// y 轴的终点位置

    @Deprecated
    private int selection = -1;// init selection
    private float mXAxisLength = 100f;// x 轴总长度
    private float mYAxisLength = 100f;// y 轴总长度
    private float mActualPointsLength = 90f;// x 轴上的实际显示区域总长度
    private float mPointSpacingWidth = 10f;// x 轴上的刻度点的平均长度
    private int mXAxisDisplayNumber = 6; // x 轴同时显示的个数
    private int mYAxisDisplayNumber = 3;// y 轴显示的个数
    private int mYAxisPeakValue = 90;
    private Paint mDottedPaint;

    @Deprecated
    private float offsetX = 0f, downX;

    private RectF rectFWorkArea = new RectF();

    private Paint mChartLinePaint;// 绘制折线的画笔
    private Path mChartLinePath;// 折线路径
    private Path mReplacePath;// path measure 的路径
    private PathMeasure mLinePathMeasure;
    private ValueAnimator mPathMeasureAnimator;
    private float mSegmentPathMeasure;
    private boolean isPlayLine = false;


    public LineChartViewForYinJiVer2(Context context) {
        this(context, null);
    }

    public LineChartViewForYinJiVer2(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LineChartViewForYinJiVer2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCtx = context;

        initialize();

        playLineAnimation();
    }

    private void initialize() {
        mListData = new ArrayList<>();
        mListPoints = new ArrayList<>();
        mListRoundRect = new ArrayList<>();

        initParams();

        initPaintAndPath();
    }

    private void initParams() {
        mXAxisStart = 10f;

    }

    private void initPaintAndPath() {
        mLineXYPaint = new Paint();
        mLineXYPaint.setAntiAlias(true);
        mLineXYPaint.setColor(Color.YELLOW);
        mLineXYPaint.setStrokeWidth(5f);

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

        mDottedPaint = new Paint();
        mDottedPaint.setAntiAlias(true);
        mDottedPaint.setStyle(Paint.Style.STROKE);
        mDottedPaint.setStrokeWidth(2f);
        mDottedPaint.setColor((Color.GRAY));
        mDottedPaint.setStrokeCap(Paint.Cap.ROUND);

        mTestPoint = new Paint();
        mTestPoint.setAntiAlias(true);
        mTestPoint.setColor(Color.BLACK);
        mTestPoint.setStrokeWidth(4f);

        mChartLinePath = new Path();
        mLinePathMeasure = new PathMeasure();
        mChartLinePaint = new Paint();
        mChartLinePaint.setAntiAlias(true);
        mChartLinePaint.setColor(Color.GREEN);
        mChartLinePaint.setStrokeWidth(2f);
        mChartLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mChartLinePaint.setStyle(Paint.Style.STROKE);
        mReplacePath = new Path();
    }

    /**
     * calc initialize value
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        mViewHeight = getMeasuredHeight() * 1.0f;

        // 临时计算 x 轴参数
        mXAxisStart = widthPixels * 0.1f;
        mXAxisTerminal = widthPixels - widthPixels * 0.05f;
        mXAxisLength = mXAxisTerminal - mXAxisStart;

        // 计算 points, points 点的存在范围应该在 line 中，所以开始结束向内缩小 0.05f
        mActualPointsLength = mXAxisLength - mXAxisStart * 2;
        mPointSpacingWidth = mActualPointsLength / mXAxisDisplayNumber;

        // 计算 y 轴参数
        mYAxisStart = mViewHeight * 0.1f;
        mYAxisTerminal = mViewHeight * 0.85f;
        mYAxisLength = mYAxisTerminal - mYAxisStart;

        // 计算点数据的距离
        if (mListData != null && mListData.size() > 0) {
            mListPoints.clear();
            Point point;
            for (int i = 0; i < mListData.size(); i ++) {
                point = new Point();
                float pX = mPointSpacingWidth * i + mXAxisStart + mXAxisStart;
                point.x = (int) pX;
                float pY = mYAxisLength / mYAxisPeakValue * (mYAxisPeakValue - Integer.valueOf(mListData.get(i).getPrice()));
                point.y = (int) (pY + mYAxisStart);
                mListPoints.add(point);
            }
            mViewWidth = (int) (mListPoints.get(mListPoints.size() - 1).x + mXAxisStart);
        }
        mViewWidth = mViewWidth > widthPixels ? mViewWidth : widthPixels;

        // 重新计算 X 轴的 terminal 值
        mXAxisTerminal = mViewWidth - widthPixels * 0.05f;
        setMeasuredDimension((int) mViewWidth, MeasureSpec.getSize(heightMeasureSpec));

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawYAxisAndMarks(canvas);
        drawXAxisAndPoints(canvas);

        if (mListPoints != null && mListPoints.size() > 0) {
            mChartLinePath.reset();
            mChartLinePath.moveTo(mListPoints.get(0).x, mListPoints.get(0).y);
            for (int i = 0; i < mListPoints.size(); i ++) {
                mChartLinePath.lineTo(mListPoints.get(i).x, mListPoints.get(i).y);
            }
            if (isPlayLine) {
                mReplacePath.reset();
                mLinePathMeasure.setPath(mChartLinePath, false);
                mLinePathMeasure.getSegment(0, mSegmentPathMeasure * mLinePathMeasure.getLength(), mReplacePath, true);
                canvas.drawPath(mReplacePath, mChartLinePaint);
            } else {
                canvas.drawPath(mChartLinePath, mChartLinePaint);
            }
        }
        drawValuePoint(canvas);
        drawPriceAndRect(canvas);
    }

    private void drawYAxisAndMarks(Canvas canvas) {
        // 计算每个 dotted line 的高度
        float perDottedLineHeight = (mYAxisTerminal - mYAxisStart) / 3;
        for (int i = 0; i <= mYAxisDisplayNumber; i++) {
            float y = mYAxisTerminal - perDottedLineHeight * i;
            drawDottedLine(canvas, mXAxisStart, y, mXAxisTerminal, y);

            drawYAxisText(canvas, i, mXAxisStart, y);

            canvas.drawCircle(mXAxisStart, y, 5f, mTextPaint);
        }

        canvas.drawLine(mXAxisStart, mYAxisStart, mXAxisStart, mYAxisTerminal, mLineXYPaint);
    }

    /**
     * 画黄色横线
     * @param canvas
     */
    private void drawXAxisAndPoints(Canvas canvas) {
        canvas.drawLine(mXAxisStart, mYAxisTerminal, mXAxisTerminal, mYAxisTerminal, mLineXYPaint);
        for (int i = 0; i < getXAxisDisplayCount(); i++) {
            float fx = mPointSpacingWidth * i + mXAxisStart * 2;
            float fy = mYAxisTerminal;
            canvas.drawCircle(fx, fy, 5f, mTextPaint);
            drawDottedLine(canvas, fx, mYAxisStart, fx, fy);

            String text = i + "月";
            float textTotalWidth = mTextPaint.measureText(text);
            fx -= textTotalWidth / 2;
            fy += FontMatrixUtils.calcTextHalfHeightPoint(mTextPaint) + 10f;
            canvas.drawText(text, fx, fy, mTextPaint);
        }
    }

    private int getXAxisDisplayCount() {
        return mXAxisDisplayNumber > mListPoints.size() ? mXAxisDisplayNumber : mListPoints.size();
    }

    private void drawDottedLine(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        mDottedPaint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 4));
        Path mPath = new Path();
        mPath.reset();
        mPath.moveTo(startX, startY);
        mPath.lineTo(stopX, stopY);
        canvas.drawPath(mPath, mDottedPaint);
    }

    private void drawYAxisText (Canvas canvas, int i, float x, float y) {
        int text = mYAxisPeakValue / mYAxisDisplayNumber * i;
        float textWidth = mTextPaint.measureText(text + "");
        canvas.drawText(text + "", x - textWidth, y, mTextPaint);
    }

    /**
     * 折线点
     * @param canvas
     */
    private void drawValuePoint(Canvas canvas) {
        if (mListPoints == null || mListPoints.size() <= 0) {return;}
        for (int i = 0; i < mListPoints.size(); i ++) {
            canvas.drawPoint(mListPoints.get(i).x, mListPoints.get(i).y, mTestPoint);
        }
    }

    /**
     * todo 计算圆角 rect 画出来
     * @param canvas
     */
    @Deprecated
    private void drawPriceAndRect(Canvas canvas) {
        if (mListPoints == null || mListPoints.size() <= 0) {return;}
        mListRoundRect.clear();
        mTextPaint.setTextSize(dp2Px(10));
        float currencySymbolWidth = mTextPaint.measureText("￥");
        mTextPaint.setTextSize(dp2Px(15));
        String text;
        float textTotalWidth;
        float textHeight = FontMatrixUtils.calcTextHalfHeightPoint(mTextPaint);
        Rect rect;
        for (int i = 0; i < mListPoints.size(); i ++) {
            float pY = mListPoints.get(i).y;// 中心点为 line 向上的一半

            text = mListData.get(i).getPrice();
            textTotalWidth = currencySymbolWidth + mTextPaint.measureText(text);

            // draw round rect
            rect = new Rect();  // 以中心点，左上右下扩展
            int l = (int) (mListPoints.get(i).x - textTotalWidth / 2 - textHeight);
            int t = (int) (pY - textHeight * 3 / 2);
            int r = (int) (mListPoints.get(i).x + textTotalWidth / 2 + textHeight);
            int b = (int) (pY + textHeight * 3 / 2);
            rect.set(l, t, r, b);
            // canvas.drawRoundRect(new RectF(rect), dp2Px(5), dp2Px(5), mRoundRectPaint);
            mListRoundRect.add(rect);

            canvas.save();
            Path path = new Path();
            path.moveTo(mListPoints.get(i).x - dp2Px(5), b);
            path.lineTo(mListPoints.get(i).x + dp2Px(5), b);
            path.lineTo(mListPoints.get(i).x, b + dp2Px(5));
            // canvas.drawPath(path, mRoundRectPaint);
            canvas.restore();

            // draw currency symbol text
            mTextPaint.setTextSize(dp2Px(10));
            canvas.drawText(
                    "￥",
                    mListPoints.get(i).x - textTotalWidth / 2,
                    pY + textHeight / 2,
                    mTextPaint);

            mTextPaint.setTextSize(dp2Px(15));
            canvas.drawText(
                    text,
                    mListPoints.get(i).x - (textTotalWidth - currencySymbolWidth) / 2 + dp2Px(3),
                    pY + textHeight / 2,
                    mTextPaint);


            // draw test center point
            // canvas.drawPoint(mListPoints.get(i).x, pY, mTestPoint);
        }
    }

    /**
     * 开启 path measure 绘制 path 路径
     */
    public void playLineAnimation() {
        isPlayLine = true;
        mPathMeasureAnimator = new ValueAnimator().ofFloat(0, 1);
        mPathMeasureAnimator.setDuration(300 * mListPoints.size());
        mPathMeasureAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSegmentPathMeasure = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mPathMeasureAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isPlayLine = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mPathMeasureAnimator.start();
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

    private long downTime = 0L;
    private void handleActionDown(MotionEvent event) {
        downTime = System.currentTimeMillis();
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

    private long moveTime = 0L;
    private void handleActionMove(MotionEvent event) {
        float mX = event.getX();
        offsetX = mX - downX;
        invalidate();
    }

    private void handleActionUp(MotionEvent event) {
        if (offsetX > 0) {

        }
        offsetX = 0f;
    }

    private float dp2Px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mCtx.getResources().getDisplayMetrics());
    }

    public class ViewItem {
        @Deprecated
        private int people = 0;
        private String price = "";
        public int getPeople() {
            return people;
        }

        @Deprecated
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

    public void setData(ArrayList<ViewItem> listData) {
        this.mListData = listData;
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < mListData.size(); i++) {
            temp.add(Integer.parseInt(mListData.get(i).getPrice()));
        }
        if (temp.size() > 0) {
            int max = Collections.max(temp);
            resetPeakValue(max);
        }
        requestLayout();
    }

    /**
     * 重置 peak value，计算最小公倍数，符合则结束递归
     * @param listMax
     */
    private void resetPeakValue (int listMax) {
        mYAxisPeakValue = Math.max(listMax, mYAxisPeakValue);
        if (mYAxisPeakValue % 30 != 0) {
            mYAxisPeakValue += 1;
            resetPeakValue(mYAxisPeakValue);
        }
    }

    public ArrayList<ViewItem> testLoadData(int items) {
        ArrayList<ViewItem> list = new ArrayList<>();
        for (int i = 0; i < items; i++) {
            ViewItem viewItem = new ViewItem();
            Random random = new Random();
            int p = random.nextInt(mYAxisPeakValue + 50);
            viewItem.setPrice(p + "");
            list.add(viewItem);
        }
        return list;
    }
}
