package com.dyzs.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

import com.dyzs.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dyzs
 */

public class LineChartReport extends View {
    private static final String TAG = LineChartReport.class.getSimpleName();
    private int viewWith;
    private float viewHeight;

    private float brokenLineWith = 0.5f;

    private int brokenLineColor   = 0xff02bbb7;
    private int straightLineColor = 0xffe2e2e2;//0xffeaeaea
    private int textNormalColor   = 0xff7e7e7e;

    private int mScoreMax = 600;
    private int mScore2nd = 400;
    private int mScore3rd = 200;
    private int mScoreMin = 0;

    private ArrayList<String> mAllMonth = new ArrayList<>();
    private ArrayList<String> mMonthText = new ArrayList<>();
    @Deprecated
    private ArrayList<Integer> mAllScore = new ArrayList<>();
    private ArrayList<Integer> mScore = new ArrayList<>();
    private int monthCount  = 5;
    private int selectMonth = 0;//选中的月份

    private List<Point> scorePoints;// 总的点数
    private ArrayList<Point> mDrawPoints;// 需要画的点数

    private int textSize = dipToPx(15);

    private Paint brokenPaint;
    private Paint straightPaint;
    private Paint dottedPaint;
    private Paint textPaint;
    private Paint mCoverPaint;       // cover 左右两边的折线值

    private Path brokenPath;

    private float mMonthLinePercent = 0.88f;
    private float mDottedLine1st = 0.22f;
    private float mDottedLine2nd = 0.44f;
    private float mDottedLine3rd = 0.66f;
    private float mDottedLine4th = 0.88f;

    private float mStartX = 0.1f;
    private float mStartTextX = 0.1f;//mStartX * 4 / 5;
    private int mStartXWidth = 0;
    private int mChartWidth = 0;
    private float mAverageWidth;
    private int mTotalBackoffX = 0;  // score size 大于显示数量 month count 时, 计算后退偏移量

    private Interpolator mInterpolator;
    private Animation mAnimation;
    private Context mContext;
    public LineChartReport(Context context) {
        this(context, null);
    }

    public LineChartReport(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LineChartReport(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineChartReport);
        mScoreMax = a.getInt(R.styleable.LineChartReport_max_score, mScoreMax);
        mScoreMin = a.getInt(R.styleable.LineChartReport_min_score, mScoreMin);
        brokenLineColor = a.getColor(R.styleable.LineChartReport_broken_line_color,brokenLineColor);
        a.recycle();
        mContext = context;
        mScore = new ArrayList<>();
        mDrawPoints = new ArrayList<>();
        initConfig();
        init();
    }

    private void initConfig() {
        if (mScore.size() == 0) {return;}
        selectMonth = mScore.size() - 1;
        for (int i = 0; i < mScore.size(); i++) {
            mScoreMax = mScoreMax > mScore.get(i) ? mScoreMax : mScore.get(i);
        }
        mScore2nd = mScoreMax / 3 * 2;
        mScore3rd = mScoreMax / 3;
    }

    private void init() {
        brokenPath = new Path();

        brokenPaint = new Paint();
        brokenPaint.setAntiAlias(true);
        brokenPaint.setStyle(Paint.Style.STROKE);
        brokenPaint.setStrokeWidth(dipToPx(brokenLineWith));
        brokenPaint.setStrokeCap(Paint.Cap.ROUND);

        straightPaint = new Paint();
        straightPaint.setAntiAlias(true);
        straightPaint.setStyle(Paint.Style.STROKE);
        straightPaint.setStrokeWidth(brokenLineWith);
        straightPaint.setColor((straightLineColor));
        straightPaint.setStrokeCap(Paint.Cap.ROUND);

        dottedPaint = new Paint();
        dottedPaint.setAntiAlias(true);
        dottedPaint.setStyle(Paint.Style.STROKE);
        dottedPaint.setStrokeWidth(brokenLineWith);
        dottedPaint.setColor((straightLineColor));
        dottedPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor((textNormalColor));
        textPaint.setTextSize(dipToPx(15));

        mCoverPaint = new Paint();
        mCoverPaint.setColor(ContextCompat.getColor(mContext, R.color.blue));
        mCoverPaint.setAntiAlias(true);
    }

    /**
     * 计算 point 点
     *
     */
    private void initData() {
        scorePoints = new ArrayList<>();
        mDrawPoints = new ArrayList<>();
        float maxScoreYCoordinate = viewHeight * mDottedLine1st;
        float minScoreYCoordinate = viewHeight * mDottedLine4th;
        mStartXWidth = (int) (viewWith * mStartX);
        mChartWidth = viewWith - mStartXWidth * 2;//分隔线距离最左边和最右边的距离是0.15倍的viewWith
        int   coordinateX;
        // monthCount = mScore.length;
        mAverageWidth = mChartWidth / (monthCount - 1);
        if(mScore.size() > 5) {
            mTotalBackoffX = (int) (mAverageWidth * (mScore.size() - monthCount));
            for(int i = 0; i < mScore.size(); i++) {
                Point point = new Point();
                coordinateX = (int) (mChartWidth * ((float) (i) / (mMonthText.size() - 1)) + (viewWith * mStartX));
                point.x = coordinateX + offsetXCount - mTotalBackoffX;
                point.y = (int) (((float) (mScoreMax - mScore.get(i)) / (mScoreMax)) * (minScoreYCoordinate - maxScoreYCoordinate) + maxScoreYCoordinate);
                scorePoints.add(point);
                if (i >= mScore.size() - monthCount) {
                    mDrawPoints.add(point);
                }
            }
            if (scorePoints.get(0).x > mStartXWidth) {
                scorePoints.get(0).x = mStartXWidth;
                isAllowMoveL2R = false;
                isAllowMoveR2L = true;
            }
            if (scorePoints.get(mScore.size() - 1).x < (mChartWidth + mStartXWidth)) {
                scorePoints.get(mScore.size() - 1).x = (mChartWidth + mStartXWidth);
                isAllowMoveL2R = true;
                isAllowMoveR2L = false;
            }
        } else {
            mTotalBackoffX = 0;
            for(int i = 0; i < mScore.size(); i++) {
                Point point = new Point();
                coordinateX = (int) (mChartWidth * ((float) (i) / (mMonthText.size() - 1)) + (viewWith * mStartX));
                point.x = coordinateX + offsetXCount;
                point.y = (int) (((float) (mScoreMax - mScore.get(i)) / (mScoreMax)) * (minScoreYCoordinate - maxScoreYCoordinate) + maxScoreYCoordinate);
                scorePoints.add(point);
                mDrawPoints.add(point);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWith = w;
        viewHeight = h;
        Log.v(TAG, "on size changed");
        initData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDottedLine(canvas, viewWith * mStartX, viewHeight * mDottedLine1st, viewWith, viewHeight * mDottedLine1st);
        drawDottedLine(canvas, viewWith * mStartX, viewHeight * mDottedLine2nd, viewWith, viewHeight * mDottedLine2nd);
        drawDottedLine(canvas, viewWith * mStartX, viewHeight * mDottedLine3rd, viewWith, viewHeight * mDottedLine3rd);

        restoreData();

        drawMonthLine(canvas);
        drawBrokenLine(canvas);

        drawFakeCover(canvas);

        drawText(canvas);

        drawPoint(canvas);

        // drawRequestPoint(canvas);
    }

    private void restoreData() {
        if (mScore == null) {return;}
        int coordinateX;
        if(mScore.size() > 5) {
            mDrawPoints = new ArrayList<>();
            for(int i = 0; i < mScore.size(); i++) {
                Point point = scorePoints.get(i);
                // coordinateX = (int) (mChartWidth * ((float) (i) / (mMonthText.size() - 1)) + (viewWith * mStartX));
                // point.x = coordinateX + offsetXCount - mTotalBackoffX;
                point.x += offsetX;
                if (i + temp >= mScore.size() - monthCount ) {
                    mDrawPoints.add(point);
                }
            }
            if (scorePoints.get(0).x > mStartXWidth) {
                // scorePoints.get(0).x = mStartXWidth;
                /*for(int i = 0; i < mScore.size(); i++) {
                    Point point = scorePoints.get(i);
                    point.x = (int) (mStartXWidth + mAverageWidth * i);
                    if (i >= mScore.size() - monthCount) {
                        mDrawPoints.add(point);
                    }
                }*/
                isAllowMoveL2R = false;
                isAllowMoveR2L = true;
                return;
            }
            if (scorePoints.get(mScore.size() - 1).x < (mChartWidth + mStartXWidth)) {
                // scorePoints.get(mScore.size() - 1).x = (mChartWidth + mStartXWidth);
                /*for(int i = mScore.size(); i > 0; i--) {
                    Point point = scorePoints.get(i - 1);
                    point.x = (int) (mStartXWidth + mChartWidth - mAverageWidth * (mScore.size() - i));
                    if (i >= mScore.size() - monthCount) {
                        mDrawPoints.add(point);
                    }
                }*/
                isAllowMoveL2R = true;
                isAllowMoveR2L = false;
                return;
            }
        }
    }


    private int downX, downY;
    private int offsetX, offsetY;
    private int firstDownX, firstDownY;
    private int offsetXCount = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /* 一旦底层View收到touch的action后调用这个方法那么父层View就不会再调用onInterceptTouchEvent了，也无法截获以后的action */
        this.getParent().requestDisallowInterceptTouchEvent(true);
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                firstDownX = (int) event.getX();
                firstDownY = (int) event.getY();
                isAllowMoveL2R = true;
                isAllowMoveR2L = true;
                break;
            case MotionEvent.ACTION_MOVE:
                onActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onActionUpRestore(event);
                onActionUpEvent(event);
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    private int l2RCount = 1, r2LCount = 1;
    private boolean isAllowMoveL2R = true, isAllowMoveR2L = true;
    private boolean isLeft2Right = false;
    private int temp = 0;
    // TODO: 2017/12/4 偏移时计算偏移数量
    private void onActionMove(MotionEvent event) {
        int moveX = (int) event.getX();
        int moveY = (int) event.getY();
        offsetY = moveY - downY;
        int absY = Math.abs(moveY - firstDownY);
        int absX = moveX - firstDownX;
        offsetX = moveX - downX;
        if (isAllowMoveL2R && absX > 100 && absY < 100) {
            // Log.v(TAG, "left to right");
            isLeft2Right = true;
            // offsetX = moveX - downX;
        }
        if (isAllowMoveR2L && absX < -100 && absY < 100) {
            // Log.v(TAG, "right to left");
            isLeft2Right = false;
            // offsetX = moveX - downX;
        }

        /*if (isAllowMoveL2R && isLeft2Right && offsetXCount > mAverageWidth * l2RCount) {
            l2RCount = (int) (offsetXCount / mAverageWidth);
            selectMonth -= 1;
            l2RCount += 1;
        }
        if (isAllowMoveR2L && !isLeft2Right && offsetXCount < -mAverageWidth * r2LCount) {
            r2LCount = (int) (Math.abs(offsetXCount) / mAverageWidth);
            selectMonth += 1;
            r2LCount += 1;
        }*/

        temp = (int) (offsetXCount / mAverageWidth);

        Log.v(TAG, "offsetX:.." + offsetX);
        Log.v(TAG, "offsetXCount:....." + offsetXCount);
        offsetXCount += offsetX;
        downX = moveX;
        downY = moveY;
        invalidate();
    }

    /**
     * 重置 offsetXCount 的值，重置为平均值的固定倍数
     */
    private void onActionUpRestore(MotionEvent event) {
        if (isAllowMoveL2R == false && isAllowMoveR2L) {
            offsetXCount = Math.abs(mTotalBackoffX);
            invalidate();
            return;
        }
        if (isAllowMoveR2L == false && isAllowMoveL2R) {
            offsetXCount = 0;
            invalidate();
            return;
        }
        // temp = (int) (offsetXCount / mAverageWidth);
        offsetXCount = (int) (mAverageWidth * temp);
        selectMonth += temp;
        /*if (selectMonth < 1) {
            selectMonth = 1;
        }
        if (selectMonth >= mScore.size() - 1) {
            selectMonth = mScore.size() - 1;
        }*/
        invalidate();
    }

    private void onActionUpEvent(MotionEvent event) {
        boolean isValidTouch = validateTouch(event.getX(), event.getY());
        if(isValidTouch) {
            invalidate();
        }
    }

    //是否是有效的触摸范围
    private boolean validateTouch(float x, float y) {
        //曲线触摸区域
        for(int i = 0; i < scorePoints.size(); i++) {
            // dipToPx(8)乘以2为了适当增大触摸面积
            if(x > (scorePoints.get(i).x - dipToPx(8) * 2) && x < (scorePoints.get(i).x + dipToPx(8) * 2)) {
                if(y > (scorePoints.get(i).y - dipToPx(8) * 2) && y < (scorePoints.get(i).y + dipToPx(8) * 2)) {
                    selectMonth = i + 1;
                    return true;
                }
            }
        }

        /*//月份触摸区域
        //计算每个月份X坐标的中心点
        float monthTouchY = viewHeight * mMonthLinePercent - dipToPx(3);//减去dipToPx(3)增大触摸面积

        float newWith = viewWith - (viewWith * mStartX) * 2;//分隔线距离最左边和最右边的距离是0.15倍的viewWith
        float validTouchX[] = new float[mScore.size()];
        for(int i = 0; i < mScore.size(); i++) {
            validTouchX[i] = newWith * ((float) (i) / (mMonthText.size() - 1)) + (viewWith * mStartX);
        }

        if(y > monthTouchY) {
            for(int i = 0; i < validTouchX.length; i++) {
                Log.v(TAG, "validateTouch: validTouchX:" + validTouchX[i]);
                if(x < validTouchX[i] + dipToPx(8) && x > validTouchX[i] - dipToPx(8)) {
                    Log.v(TAG, "validateTouch: " + (i + 1));
                    selectMonth = i + 1;
                    return true;
                }
            }
        }*/

        return false;
    }

    private void drawFakeCover(Canvas canvas) {
        Rect rect = new Rect();
        rect.set(0, 0, mStartXWidth, (int) (viewHeight * mMonthLinePercent));
        canvas.drawRect(rect, mCoverPaint);
        rect = new Rect();
        rect.set((mChartWidth + mStartXWidth), 0, viewWith, (int) (viewHeight * mMonthLinePercent));
        canvas.drawRect(rect, mCoverPaint);
    }

    //绘制折线穿过的点
    protected void drawRequestPoint(Canvas canvas) {
        if(mDrawPoints == null) {
            return;
        }
        brokenPaint.setStrokeWidth(dipToPx(1));
        int pointX;
        for(int i = 0; i < mDrawPoints.size(); i++) {
            pointX = mDrawPoints.get(i).x;
            brokenPaint.setColor(brokenLineColor);
            brokenPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(pointX, mDrawPoints.get(i).y, dipToPx(3), brokenPaint);
            brokenPaint.setColor(Color.WHITE);
            brokenPaint.setStyle(Paint.Style.FILL);
            if(i == selectMonth - 1) {
                pointX = mDrawPoints.get(i).x;
                brokenPaint.setColor(0xffd0f3f2);
                canvas.drawCircle(pointX, mDrawPoints.get(i).y, dipToPx(8f), brokenPaint);
                brokenPaint.setColor(0xff81dddb);
                canvas.drawCircle(pointX, mDrawPoints.get(i).y, dipToPx(5f), brokenPaint);

                //绘制浮动文本背景框
                drawFloatTextBackground(canvas, pointX, mDrawPoints.get(i).y - dipToPx(8f));

                textPaint.setColor(0xffffffff);
                //绘制浮动文字
                canvas.drawText(String.valueOf(mScore.get(i)), pointX, mDrawPoints.get(i).y - dipToPx(5f) - textSize, textPaint);
            }
            brokenPaint.setColor(0xffffffff);
            canvas.drawCircle(pointX, mDrawPoints.get(i).y, dipToPx(1.5f), brokenPaint);
            brokenPaint.setStyle(Paint.Style.STROKE);
            brokenPaint.setColor(brokenLineColor);
            canvas.drawCircle(pointX, mDrawPoints.get(i).y, dipToPx(2.5f), brokenPaint);
        }
    }

    //绘制折线穿过的点
    protected void drawPoint(Canvas canvas) {
        if(scorePoints == null) {
            return;
        }
        brokenPaint.setStrokeWidth(dipToPx(1));
        int pointX;
        for(int i = 0; i < scorePoints.size(); i++) {
            pointX = scorePoints.get(i).x;
            brokenPaint.setColor(brokenLineColor);
            brokenPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(pointX, scorePoints.get(i).y, dipToPx(3), brokenPaint);
            brokenPaint.setColor(Color.WHITE);
            brokenPaint.setStyle(Paint.Style.FILL);
            if(i == selectMonth - 1) {
                pointX = scorePoints.get(i).x;
                brokenPaint.setColor(0xffd0f3f2);
                canvas.drawCircle(pointX, scorePoints.get(i).y, dipToPx(8f), brokenPaint);
                brokenPaint.setColor(0xff81dddb);
                canvas.drawCircle(pointX, scorePoints.get(i).y, dipToPx(5f), brokenPaint);

                //绘制浮动文本背景框
                drawFloatTextBackground(canvas, pointX, scorePoints.get(i).y - dipToPx(8f));

                textPaint.setColor(0xffffffff);
                //绘制浮动文字
                canvas.drawText(String.valueOf(mScore.get(i)), pointX, scorePoints.get(i).y - dipToPx(5f) - textSize, textPaint);
            }
            brokenPaint.setColor(0xffffffff);
            canvas.drawCircle(pointX, scorePoints.get(i).y, dipToPx(1.5f), brokenPaint);
            brokenPaint.setStyle(Paint.Style.STROKE);
            brokenPaint.setColor(brokenLineColor);
            canvas.drawCircle(pointX, scorePoints.get(i).y, dipToPx(2.5f), brokenPaint);
        }
    }

    /**
     * 绘制月份的直线(包括刻度)
     */
    private void drawMonthLine(Canvas canvas) {
        straightPaint.setStrokeWidth(dipToPx(1));
        canvas.drawLine(0, viewHeight * mMonthLinePercent, viewWith, viewHeight * mMonthLinePercent, straightPaint);

        float newWith = viewWith - (viewWith * mStartX) * 2;//分隔线距离最左边和最右边的距离是0.15倍的viewWith
        float coordinateX;//分隔线X坐标
        for(int i = 0; i < mMonthText.size(); i++) {
            coordinateX = newWith * ((float) (i) / (mMonthText.size() - 1)) + (viewWith * mStartX);// + offsetXCount;
            canvas.drawLine(coordinateX, viewHeight * mMonthLinePercent, coordinateX, viewHeight * mMonthLinePercent + dipToPx(4), straightPaint);
        }
    }

    //绘制折线
    private void drawBrokenLine(Canvas canvas) {
        brokenPath.reset();
        brokenPaint.setColor(brokenLineColor);
        brokenPaint.setStyle(Paint.Style.STROKE);
        if(mScore.size() == 0) {
            return;
        }
        // Log.v(TAG, "drawBrokenLine: " + scorePoints.get(0));
        brokenPath.moveTo(scorePoints.get(0).x, scorePoints.get(0).y);
        for(int i = 0; i < scorePoints.size(); i++) {
            brokenPath.lineTo(scorePoints.get(i).x, scorePoints.get(i).y);
        }
        canvas.drawPath(brokenPath, brokenPaint);

    }

    /**
     * 绘制月份文本和月份框框
     */
    private void drawText(Canvas canvas) {
        textPaint.setTextSize(dipToPx(12));
        textPaint.setColor(textNormalColor);

        /*canvas.drawText(
                String.valueOf(mScoreMax),
                viewWith * mStartTextX - dipToPx(10),
                viewHeight * mDottedLine1st + textSize * 0.25f,
                textPaint);
        canvas.drawText(
                String.valueOf(mScore2nd),
                viewWith * mStartTextX - dipToPx(10),
                viewHeight * mDottedLine2nd + textSize * 0.25f,
                textPaint);
        canvas.drawText(
                String.valueOf(mScore3rd),
                viewWith * mStartTextX - dipToPx(10),
                viewHeight * mDottedLine3rd + textSize * 0.25f,
                textPaint);*/

        textPaint.setColor(0xff7c7c7c);

        float coordinateX;//分隔线X坐标
        textPaint.setTextSize(dipToPx(12));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textNormalColor);
        textSize = (int) textPaint.getTextSize();
        for(int i = 0; i < mMonthText.size(); i++) {
            coordinateX = mChartWidth * ((float) (i) / (mMonthText.size() - 1)) + (viewWith * mStartX);
            coordinateX += offsetXCount - mTotalBackoffX;
            if(i == selectMonth - 1) {
                textPaint.setStyle(Paint.Style.STROKE);
                textPaint.setColor(brokenLineColor);
                RectF r2 = new RectF();
                r2.left = coordinateX - textSize - dipToPx(4);
                r2.top = viewHeight * mMonthLinePercent + dipToPx(4) + textSize / 2;
                r2.right = coordinateX + textSize + dipToPx(4);
                r2.bottom = viewHeight * mMonthLinePercent + dipToPx(4) + textSize + dipToPx(8);
                canvas.drawRoundRect(r2, 10, 10, textPaint);
            }
            //绘制月份
            canvas.drawText(
                    mMonthText.get(i),
                    coordinateX,
                    viewHeight * mMonthLinePercent + dipToPx(4) + textSize + dipToPx(5),
                    textPaint);

            textPaint.setColor(textNormalColor);

        }

    }

    /**
     * 绘制显示浮动文字的背景
     */
    private void drawFloatTextBackground(Canvas canvas, int x, int y) {
        brokenPath.reset();
        brokenPaint.setColor(brokenLineColor);
        brokenPaint.setStyle(Paint.Style.FILL);

        //P1
        Point point = new Point(x, y);
        brokenPath.moveTo(point.x, point.y);

        //P2
        point.x = point.x + dipToPx(5);
        point.y = point.y - dipToPx(5);
        brokenPath.lineTo(point.x, point.y);

        //P3
        point.x = point.x + dipToPx(12);
        brokenPath.lineTo(point.x, point.y);

        //P4
        point.y = point.y - dipToPx(17);
        brokenPath.lineTo(point.x, point.y);

        //P5
        point.x = point.x - dipToPx(34);
        brokenPath.lineTo(point.x, point.y);

        //P6
        point.y = point.y + dipToPx(17);
        brokenPath.lineTo(point.x, point.y);

        //P7
        point.x = point.x + dipToPx(12);
        brokenPath.lineTo(point.x, point.y);

        //最后一个点连接到第一个点
        brokenPath.lineTo(x, y);

        canvas.drawPath(brokenPath, brokenPaint);
    }

    /**
     * 画虚线
     *
     * @param canvas 画布
     * @param startX 起始点X坐标
     * @param startY 起始点Y坐标
     * @param stopX  终点X坐标
     * @param stopY  终点Y坐标
     */
    private void drawDottedLine(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        dottedPaint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 4));
        dottedPaint.setStrokeWidth(1);
        // 实例化路径
        Path mPath = new Path();
        mPath.reset();
        // 定义路径的起点
        mPath.moveTo(startX, startY);
        mPath.lineTo(stopX, stopY);
        canvas.drawPath(mPath, dottedPaint);

    }

    public void setScore(ArrayList<Integer> score) {
        if (mScore == null) {
            mScore = new ArrayList<>();
        }
        this.mScore = score;
        initConfig();
        initData();
        invalidate();
    }

    public void setMonthText(ArrayList<String> monthText) {
        this.mMonthText.clear();
        this.mMonthText = monthText;
        this.monthCount = mMonthText.size();
    }

    public void setAllMonth(ArrayList<String> allMonth) {
        this.mAllMonth = allMonth;
    }

    public void setAllScore(ArrayList<Integer> allScore) {
        this.mAllScore = allScore;
        mScore = new ArrayList<>();
        if (mAllScore != null && mAllScore.size() > 0) {
            for (int i = 0; i < 5; i ++) {
                mScore.add(mAllScore.get(mAllScore.size() - 5 + i));
            }
        }
        initConfig();
        initData();
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

}
