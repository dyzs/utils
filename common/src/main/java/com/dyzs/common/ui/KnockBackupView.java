package com.dyzs.common.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.dyzs.common.R;
import com.dyzs.common.utils.ColorUtils;
import com.dyzs.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class KnockBackupView extends View {
    private static final String TAG = KnockBackupView.class.getSimpleName();
    private Context mCtx;
    private float mWidth, mHeight;
    private float mSpacing, mPadding;
    private static final int[] SYS_ATTRS = new int[]{
            android.R.attr.padding,
            android.R.attr.layout_width,
            android.R.attr.layout_height
    };
    private ArrayList<RectF> mRectFs;// Arc Rect
    private RectF mCenterRectF;
    private Paint mPaint, mPaintArc;
    private Path mPath;
    private float[] mArcPaintStrokeWidth; // 初始化定义外围的 Arc 宽度
    private int mCountOfArc = 4;// 初始化定义外围 Arc 个数
    private int[] mArcPaintColor = {
            R.color.white,
            R.color.alice_blue,
            R.color.emma_white,
            R.color.oxygen_yellow,
            R.color.oxygen_green,
            R.color.cinnabar_red,
            R.color.alice_blue,
            R.color.oxygen_yellow
    };
    private ArrayList<SweepGradient> mArcSweepGradients;// 颜色值渐变参数
    private ArrayList<Float> mArcStartAngleValues; // 圆弧起始角度, 固定不变
    private ArrayList<Float> mArcStartAngleValuesAfterRotate; // 圆弧旋转角度
    private ArrayList<Float> mArcRadians;// 圆弧弧度
    private int mSpeedRateInit = 5;// Arc 速率按照个数依次叠加

    private float mCenterArcStartAngleValues = 0f;
    private float mCenterArcSweepRadians = 90f;

    private ArrayList<RectF> mSecondFs;
    private int[] mSecondFsColors = {
            R.color.half_white,
            R.color.eighty_opacity_white,
            R.color.misty_white
    };
    //---------文本参数定义区域--------------------------
    private ArrayList<String> mTotalText;
    private ArrayList<String> mCurrText;
    private ArrayList<Path> mTextPaths;
    private int mCountOfShownText = 5; // 固定值为5
    private HashMap<String, Path> mTextPathMapping;
    private PathMeasure mTextPathMeasure;
    private Path mTextReplacePath;// path measure 的路径, 用来替换定义的 path 路径
    private float mTextPathSegment = 0;
    private float[] mTextPathSegments;
    private ValueAnimator mTextPathMeasureAnimator;
    private long mTextDrawLineDuration = 1000L;// 两点成线的动画持续时间
    private Paint mTextPaint;
    private float mTextSizeInit = 30f;
    private float mTextSizeMinimum = 5f;
    public static String[] TEXT2TEST = {
            "上原亚衣","友田彩也香","程潇","张含韵","蒋欣","蒋梦婕",
            "佟亚丽", "呵呵哒","张檬","雪莉","刘诗诗","倪妮", "Serena",
            "Alex","古力娜扎","周冬雨","秋瓷炫","新垣结衣"
    };
    //---------文本参数定义区域--------------------------

    private BackupStatus mBackupStatus = BackupStatus.STATUS_IDLE;
    public KnockBackupView(Context context) {
        this(context, null);
    }

    public KnockBackupView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public KnockBackupView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public KnockBackupView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mCtx = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, SYS_ATTRS);
        mPadding = ta.getDimension(0, 5f);
        ta.recycle();
        mRectFs = new ArrayList<>();
        mCenterRectF = new RectF();
        mRectFTemporary = new RectF();
        mSpacing = 10f;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.MAGENTA);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintArc = new Paint();
        mPaintArc.setAntiAlias(true);
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setColor(Color.BLUE);
        mPaintArc.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();

        mArcPaintStrokeWidth = new float[mCountOfArc];
        mArcSweepGradients = new ArrayList<>();

        mArcStartAngleValues = new ArrayList<>();
        mArcStartAngleValuesAfterRotate = new ArrayList<>();
        mArcRadians = new ArrayList<>();
        for (int i = 0; i < mCountOfArc; i++) {
            mArcStartAngleValues.add(i * 360f / mCountOfArc);
            mArcStartAngleValuesAfterRotate.add(i * 360f / mCountOfArc);
            mArcRadians.add(360f / mCountOfArc);
            // mArcRadians.add(360f / 100 * Math.abs(30 - 5 * i) % 360);
        }

        // 第二区域
        mSecondFs = new ArrayList<>();

        // 初始化文字--------------
        mTextSizeInit = getResources().getDimensionPixelSize(R.dimen.backup_view_text_size);
        mTextSizeMinimum = getResources().getDimensionPixelSize(R.dimen.backup_view_minimum_text_size);
        mTotalText = new ArrayList<>();
        mCurrText = new ArrayList<>();
        mTextPathMapping = new HashMap<>();
        mTextPathMeasure = new PathMeasure();
        mTextReplacePath = new Path();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSizeInit);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(Color.MAGENTA);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPathSegments = new float[mCountOfShownText];

    }

    private RectF mRectFTemporary;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LogUtils.v(TAG, "onFinishInflate");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LogUtils.v(TAG, "onLayout");
        calcInitAfterMeasure();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtils.v(TAG, "onMeasure");
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        if (widthSpecMode == MeasureSpec.EXACTLY || mHeight > mWidth) {
            mHeight = mWidth;
            setMeasuredDimension((int) mWidth, (int) mHeight);
        }
    }

    private void calcInitAfterMeasure() {
        float l, t, r, b;
        // 初始化设置center大小为view 的 1/3
        l = mWidth / 3;
        r = mWidth / 3 * 2;
        t = mHeight / 3;
        b = mHeight / 3 * 2;
        mCenterRectF.set(l, t, r, b);

        // 初始化设置外围 Arc rect 数组，定义 paint stroke width 为 5 等分, 计算最外圈额外留一份 spacing,
        // 重新定义画笔倍数大小为2
        // 次方公式S=2^(N + 1) - 2
        // 1+n公式：(1 + n) * n / 2
        float operateWidth = mWidth / 3;
        float leftOutWidth = operateWidth * 2 / 3;// *2/3 表示剩余的 rect 用于处理其它圆环
        // float tempSize = (leftOutWidth - mSpacing) / mCountOfArc - mSpacing;
        float tempSize = (leftOutWidth - mSpacing * 6);
        float paintBaseStrokeWidth = tempSize / ((1 + mCountOfArc) * mCountOfArc / 2);
        mRectFs.clear();
        for (int i = 0; i < mCountOfArc; i++) {
            float realWidth = paintBaseStrokeWidth * (mCountOfArc - i);
            float totalLastWidth = tempSize - (1 + mCountOfArc - i) * (mCountOfArc - i) / 2 * paintBaseStrokeWidth;
            mArcPaintStrokeWidth[i] = realWidth;
            l = mSpacing + realWidth / 2 + (mSpacing) * i + totalLastWidth;
            t = mSpacing + realWidth / 2 + (mSpacing) * i + totalLastWidth;
            r = mWidth - (mSpacing + realWidth / 2 + (mSpacing) * i + totalLastWidth);
            b = mWidth - (mSpacing + realWidth / 2 + (mSpacing) * i + totalLastWidth);
            RectF rectF = new RectF(l, t, r, b);
            mRectFs.add(rectF);
        }

        float offsetLT = leftOutWidth;
        float thisSpacing = mSpacing;
        leftOutWidth = operateWidth / 3;
        tempSize = leftOutWidth / 3;
        mSecondFs.clear();
        for (int i = 0; i < 3; i++) {
            l = offsetLT + thisSpacing + tempSize * i;
            t = offsetLT + thisSpacing + tempSize * i;
            r = mWidth - (offsetLT + thisSpacing + tempSize * i);
            b = mWidth - (offsetLT + thisSpacing + tempSize * i);
            RectF rectF = new RectF(l, t, r, b);
            mSecondFs.add(rectF);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mBackupStatus) {
            case STATUS_IDLE:
            case STATUS_WORKING:
                drawCenterRegion(canvas);
                drawRotateArc(canvas);
                drawFlyingText(canvas);
                break;
            case STATUS_SHOW_FAILED:
                drawCenterRegionErrorStyle(canvas);
                drawRotateArc(canvas);
                break;
        }
    }

    private void drawCenterRegion(Canvas canvas) {
        float tempSize = 10f;
        float cx = (mCenterRectF.right + mCenterRectF.left) / 2;
        float cy = (mCenterRectF.bottom + mCenterRectF.top) / 2;
        float radius = (mCenterRectF.bottom - mCenterRectF.top) / 2;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#4770F7"));
        canvas.drawCircle(cx, cy, radius, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#4661EB"));
        mPaint.setStrokeWidth(tempSize);
        canvas.drawCircle(cx, cy, radius, mPaint);

        mPaint.setColor(Color.CYAN);
        canvas.save();
        canvas.rotate(mCenterArcStartAngleValues, cx, cy);
        canvas.drawArc(mCenterRectF, 0, mCenterArcSweepRadians, false, mPaint);
        canvas.restore();
    }

    private void drawCenterRegionErrorStyle(Canvas canvas) {
        float tempSize = 10f;
        float cx = (mCenterRectF.right + mCenterRectF.left) / 2;
        float cy = (mCenterRectF.bottom + mCenterRectF.top) / 2;
        float radius = (mCenterRectF.bottom - mCenterRectF.top) / 2;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(cx, cy, radius, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(tempSize);
        canvas.drawCircle(cx, cy, radius, mPaint);
        canvas.save();
        canvas.rotate(45f, cx, cy);
        float startX, startY, stopX, stopY;
        startX = cx - radius / 2;
        startY = cy;
        stopX = cx + radius / 2;
        stopY = cy;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        startX = cx;
        startY = cy - radius / 2;
        stopX = cx;
        stopY = cy + radius / 2;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        canvas.restore();
    }

    private void drawRotateArc(Canvas canvas) {
        RectF rectF;
        for (int i = 0; i < mRectFs.size(); i++) {
            canvas.save();
            rectF = mRectFs.get(i);
            mPaintArc.setColor(ContextCompat.getColor(getContext(), mArcPaintColor[i]));
            mPaintArc.setStrokeWidth(mArcPaintStrokeWidth[i]);
            float startAngle = mArcStartAngleValuesAfterRotate.get(i);
            float sweepRadians = mArcRadians.get(i);
            canvas.rotate(startAngle, rectF.centerX(), rectF.centerY());
            int[] colors = new int[]{Color.TRANSPARENT, ContextCompat.getColor(getContext(), mArcPaintColor[i]), Color.TRANSPARENT};
            float[] positions = new float[]{0f, sweepRadians / 360f, 1f};
            SweepGradient sweepGradient;
            sweepGradient = new SweepGradient(
                    rectF.centerX(),
                    rectF.centerY(),
                    colors,
                    positions);
            mPaintArc.setShader(sweepGradient);
            mPath.reset();
            mPath.addArc(rectF, 0, sweepRadians);
            // canvas.drawRect(rectF, mPaintArc);
            canvas.drawPath(mPath, mPaintArc);
            canvas.restore();
        }

        for (int i = 0; i < mSecondFs.size(); i++) {
            rectF = mSecondFs.get(i);
            mPaint.setColor(ContextCompat.getColor(getContext(), mSecondFsColors[i % 3]));
            mPaint.setStrokeWidth(5f);
            canvas.drawArc(rectF, 0, 360, false, mPaint);
        }
    }

    /**
     * 设置文字缩放为初始值到0f;
     * @param canvas
     */
    private void drawFlyingText(Canvas canvas) {
        if (mTextPathMapping.size() == 0)return;
        int totalSize = mCountOfShownText * 100;
        for (int i = 0; i < mCurrText.size(); i++) {
            mTextReplacePath.reset();
            Path textPath = mTextPathMapping.get(mCurrText.get(i));
            float stopD = mTextPathSegments[i] % totalSize / totalSize * mTextPathMeasure.getLength();
            mTextPathMeasure.setPath(textPath, false);
            mTextPathMeasure.getSegment(0, stopD, mTextReplacePath, true);
            float pos[] = new float[2], tan[] = new float[2];
            mTextPathMeasure.getPosTan(stopD, pos, tan);
            // mTextPaint.setStrokeWidth(2f);
            // canvas.drawPath(mTextReplacePath, mTextPaint);

            float rate = mTextPathSegments[i] % totalSize / totalSize;
            float textSize = (1 - rate) * mTextSizeInit;
            if (textSize < mTextSizeMinimum) {
                textSize = mTextSizeMinimum;
            }
            int textColor = ColorUtils.getCompositeColor(Color.WHITE, ContextCompat.getColor(getContext(), R.color.half_white), 1 - rate);
            mTextPaint.setColor(textColor);
            mTextPaint.setTextSize(textSize);
            canvas.drawText(mCurrText.get(i), pos[0], pos[1], mTextPaint);
        }
    }

    public void setAllTexts(ArrayList<String> list) {
        if (list == null)list = new ArrayList<>();
        this.mTotalText = list;
        this.mCurrText.clear();
        this.mCurrText.add(mTotalText.get(0));
        /*for (int i = 0; i < mTotalText.size(); i++) {
            if (i < mCountOfShownText) {
                mCurrText.add(mTotalText.get(i));
            }
        }*/
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        LogUtils.v(TAG, "width:[" + width + "], height:[" + height + "]");
        LogUtils.v(TAG, "start calc all the path of text, it will be random automatic generate");
        mTextPathMapping.clear();
        Path path;
        Point center = new Point(width / 2, height / 2);
        Point startPoint;
        Point bezierController;
        Random random = new Random();
        for (int i = 0; i < mTotalText.size(); i++) {
            path = new Path();
            int rpi = random.nextInt(4);
            startPoint = getTextRandomStartPoint(rpi);
            bezierController = getBezierPoint(startPoint, center, rpi);
            path.reset();
            path.moveTo(startPoint.x, startPoint.y);
            path.quadTo(bezierController.x, bezierController.y, center.x, center.y);
            // path.close(); this place the close method can't be call
            mTextPathMapping.put(mTotalText.get(i), path);
        }
    }

    /**
     * pX = sin(x)+r,
     * pY = cos(x)+r
     * @return point
     * 不设置 random , 固定 4 个点, 分别代表左上右下
     */
    private Point getTextRandomStartPoint(int randomPointIndex) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int radius = width / 2;
        int pointX, pointY;
        /*Random random = new Random();
        int randomPointIndex = random.nextInt(361);
        pointX = (int) (Math.cos(Math.PI * 2 / 360 * randomPointIndex) * radius) + radius;
        pointY = (int) (Math.sin(Math.PI * 2 / 360 * randomPointIndex) * radius) + radius;
        LogUtils.v(TAG, "start point:[" + pointX + ", " + pointY + ", " + randomPointIndex + "]");*/
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(0, height / 2));
        points.add(new Point(width / 2, 0));
        points.add(new Point(width, height / 2));
        points.add(new Point(width / 2, height));
        return points.get(randomPointIndex);
    }

    /**
     * 已知三角形两点及两线 A(x1,y1), B(x2,y2), 求第三点坐标C(x3,y3)
     x3=（x1ctgA2+x2ctgA1+y2-y1）/（ctgA1+ctgA2）
     y3=（y1ctgA2+y2ctgA1+x1-x2）/（ctgA1+ctgA2）
     * @param aPoint
     * @param bPoint
     * @return
     * 按照控制点为起始点 + 1 计算
     */
    private Point getBezierPoint(Point aPoint, Point bPoint, int randomPointIndex) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int radius = (int) ((mCenterRectF.bottom - mCenterRectF.top) / 2) * 3 / 2;
        int pointX, pointY;
        int centerX = width / 2;
        int centerY = height / 2;
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(width / 2 - radius, height / 2));
        points.add(new Point(width / 2, height / 2 - radius));
        points.add(new Point(width / 2 + radius, height / 2));
        points.add(new Point(width / 2, height / 2 + radius));
        int realIndex = (randomPointIndex + 1) % 4;
        return points.get(realIndex);
    }

    private ValueAnimator mAnimator;
    private void startAnimator() {
        mAnimator = ValueAnimator.ofFloat(360);
        mAnimator.setDuration(10000);
        mAnimator.setRepeatCount(-1);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            mCenterArcStartAngleValues = value * 10 % 360;
            mArcStartAngleValuesAfterRotate.clear();
            for (int i = 0; i < mCountOfArc; i++) {
                mArcStartAngleValuesAfterRotate.add(mArcStartAngleValues.get(i) + value * (mSpeedRateInit) % 360);
                /*if (i % 2 == 0) {
                } else {
                    mArcStartAngleValuesAfterRotate.add(mArcStartAngleValues.get(i) - value * (mSpeedRateInit) % 360);
                }*/
            }
            postInvalidate();
        });
        mAnimator.start();
    }

    /**
     * 开启 path measure 绘制 path 路径
     * 定义每条 path 路径生命周期为 1000L,
     */
    private int totalValues = 0;
    public void startTextAnimator() {
        index = 0;
        mTextPathMeasureAnimator = ValueAnimator.ofInt(0, 100);
        mTextPathMeasureAnimator.setDuration(mTextDrawLineDuration);
        mTextPathMeasureAnimator.setRepeatCount(-1);
        mTextPathMeasureAnimator.setInterpolator(new LinearInterpolator());
        mTextPathMeasureAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                totalValues = value + index % mCountOfShownText * 100 + mCountOfShownText * 100;// 100 * mCountOfShownText 表示初始化多出一圈
                for (int i = 0; i < mCountOfShownText; i++) {
                    mTextPathSegments[i] = totalValues - i * 100;
                }
            }
        });

        mTextPathMeasureAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                LogUtils.v(TAG, "TEXT REPEAT...." + index++);
                if (index < mTotalText.size()) {
                    String text = mTotalText.get(index);
                    if (mCurrText.size() < mCountOfShownText) {
                        mCurrText.add(text);
                    } else {
                        mCurrText.set(index % mCountOfShownText, text);
                    }
                } else {
                    int tempIndex = index % mTotalText.size();
                    String text = mTotalText.get(tempIndex);
                    if (mCurrText.size() < mCountOfShownText) {
                        mCurrText.add(text);
                    } else {
                        mCurrText.set(index % mCountOfShownText, text);
                    }
                }

            }
        });
        mTextPathMeasureAnimator.start();
    }

    public void startAnimation() {
        mBackupStatus = BackupStatus.STATUS_WORKING;
        cancelAnimator();
        postDelayed(() -> {
            startAnimator();
            startTextAnimator();
        }, 500L);
    }

    public void cancelAnimator() {
        mBackupStatus = BackupStatus.STATUS_IDLE;
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
            mAnimator.removeAllListeners();
            mAnimator.removeAllUpdateListeners();
        }

        if (mTextPathMeasureAnimator != null && mTextPathMeasureAnimator.isRunning()) {
            mTextPathMeasureAnimator.cancel();
            mTextPathMeasureAnimator.removeAllUpdateListeners();
            mTextPathMeasureAnimator.removeAllListeners();
        }
    }
    private int index = 0;

    public void setErrorStyle() {
        cancelAnimator();
        mCurrText.clear();
        mBackupStatus = BackupStatus.STATUS_SHOW_FAILED;
        postInvalidate();
    }

    public enum BackupStatus {
        STATUS_IDLE, STATUS_WORKING, STATUS_SHOW_FAILED
    }
}
