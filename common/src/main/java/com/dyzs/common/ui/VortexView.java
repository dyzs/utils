package com.dyzs.common.ui;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import com.dyzs.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class VortexView extends View {
    private static final String TAG = VortexView.class.getSimpleName();
    private Context mCtx;
    private float mWidth, mHeight;
    private float mSpacing, mPadding;
    private static final int[] SYS_ATTRS = {
            android.R.attr.padding,
            android.R.attr.layout_width,
            android.R.attr.layout_height
    };
    private ArrayList<RectF> mRectFs;
    private RectF mCenterRectF;
    private Paint mPaint, mPaintArc;
    private Path mPath;
    private float[] mArcPaintStrokeWidth; // 初始化定义外围的 Arc 宽度
    private int mCountOfArc = 5;// 初始化定义外围 Arc 个数
    private int[] mArcPaintColor = {
            R.color.white,
            R.color.alice_white,
            R.color.emma_white,
            R.color.gray_white,
            R.color.misty_white
    };
    private ArrayList<SweepGradient> mArcSweepGradients;// 颜色值渐变参数
    private ArrayList<Float> mArcStartAngleValues; // 圆弧起始角度, 固定不变
    private ArrayList<Float> mArcStartAngleValuesAfterRotate; // 圆弧旋转角度
    private ArrayList<Float> mArcRadians;// 圆弧弧度
    private int mSpeedRate = 2;

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
    private long mTextLineDuration = 5000L;// 两点成线的动画持续时间
    private Paint mTextPaint;


    private String[] mTempText2Test = {"上原亚衣","友田彩也香","程潇","张含韵","蒋欣","蒋梦婕","佟亚丽","呵呵哒","张檬","雪莉","刘诗诗","倪妮",
            "Serena","Alex","古力娜扎","周冬雨","秋瓷炫","新垣结衣"};
    //---------文本参数定义区域--------------------------
    public VortexView(Context context) {
        this(context, null);
    }

    public VortexView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public VortexView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public VortexView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mCtx = context;
        init(context, attrs);
        postDelayed(() -> {
            startAnimator();
            startTextAnimator();
        }, 1000L);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, SYS_ATTRS);
        mPadding = ta.getDimension(0, 5f);
        ta.recycle();
        mRectFs = new ArrayList<>();
        mCenterRectF = new RectF();
        mRectF = new RectF();
        mSpacing = 15f;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.MAGENTA);

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
            mArcStartAngleValues.add(i * 30f);
            mArcStartAngleValuesAfterRotate.add(i * 30f);
            mArcRadians.add(i * 30f + 60f);
            // mArcRadians.add(360f / 100 * Math.abs(30 - 5 * i) % 360);
        }

        // 初始化文字--------------
        mTotalText = new ArrayList<>();
        mCurrText = new ArrayList<>();
        mTextPathMapping = new HashMap<>();
        mTextPathMeasure = new PathMeasure();
        mTextReplacePath = new Path();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(20f);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(Color.MAGENTA);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPathSegments = new float[mCountOfShownText];

    }

    private RectF mRectF;
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        float l, t, r, b, radius;
        if (mWidth >= mHeight) {
            radius = mHeight / 2 - mPadding;
        } else {
            radius = mWidth / 2 - mPadding;
        }

        // 初始化设置center大小为view 的 1/3
        l = mWidth / 3;
        r = mWidth / 3 * 2;
        t = mHeight / 3;
        b = mHeight / 3 * 2;
        mCenterRectF.set(l, t, r, b);

        // 初始化设置外围 Arc rect 数组，定义 paint stroke width 为 5 等分, 计算最外圈额外留一份 spacing,
        float leftOutWidth = mWidth / 3;
        float tempSize = (leftOutWidth - mSpacing) / mCountOfArc - mSpacing;
        mRectFs.clear();
        for (int i = 0 ; i < mCountOfArc; i++) {
            mArcPaintStrokeWidth[i] = tempSize;
            l = mSpacing + tempSize / 2 + (mSpacing + tempSize) * i;  // 需要计算额外计算 tempSize / 2
            t = mSpacing + tempSize / 2 + (mSpacing + tempSize) * i;
            r = mWidth - (mSpacing + tempSize / 2 + (mSpacing + tempSize) * i);
            b = mWidth - (mSpacing + tempSize / 2 + (mSpacing + tempSize) * i);
            RectF rectF = new RectF(l, t, r, b);
            mRectFs.add(rectF);
        }

        SweepGradient sweepGradient;
        for (int i = 0; i < mCountOfArc; i ++) {
            sweepGradient = new SweepGradient(
                    mRectFs.get(i).centerX(),
                    mRectFs.get(i).centerY(),
                    new int[]{ContextCompat.getColor(getContext(), mArcPaintColor[i]), Color.TRANSPARENT},
                    new float[]{0f, 1f} // 颜色数组对应的 position, 若为 null 表示颜色渐变平均分布
            );
            mArcSweepGradients.add(sweepGradient);
        }
        ArrayList<String> list = new ArrayList<>(Arrays.asList(mTempText2Test));
        setAllTexts(list);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float tempSize = 20f;
        mPaint.setStrokeWidth(tempSize);

        float cx = (mCenterRectF.right + mCenterRectF.left) / 2;
        float cy = (mCenterRectF.bottom + mCenterRectF.top) / 2;
        float radius = (mCenterRectF.bottom - mCenterRectF.top) / 2;
        canvas.drawCircle(cx, cy, radius, mPaint);

        RectF rectF;
        for (int i = 0; i < mRectFs.size(); i++) {
            rectF = mRectFs.get(i);
            mPaintArc.setColor(ContextCompat.getColor(getContext(), mArcPaintColor[i]));
            mPaintArc.setStrokeWidth(mArcPaintStrokeWidth[i]);
            // mPaintArc.setShader(mArcSweepGradients.get(i));
            mPath.reset();
            mPath.addArc(rectF, mArcStartAngleValuesAfterRotate.get(i), mArcRadians.get(i));
            canvas.drawPath(mPath, mPaintArc);
        }

        drawFlyingText(canvas);
    }

    private void drawFlyingText(Canvas canvas) {
        if (mTextPathMapping.size() == 0)return;
        for (int i = 0; i < mCurrText.size(); i++) {
            mTextReplacePath.reset();
            Path textPath = mTextPathMapping.get(mCurrText.get(i));
            float stopD = mTextPathSegment / 100 * mTextPathMeasure.getLength();
            mTextPathMeasure.setPath(textPath, false);
            mTextPathMeasure.getSegment(0, stopD, mTextReplacePath, true);
            float pos[] = new float[2], tan[] = new float[2];
            mTextPathMeasure.getPosTan(stopD, pos, tan);
            canvas.drawText(mCurrText.get(i), pos[0], pos[1], mTextPaint);
        }
    }


    private ValueAnimator mAnimator;
    private void startAnimator() {
        mAnimator = ValueAnimator.ofFloat(360);
        mAnimator.setDuration(5000);
        mAnimator.setRepeatCount(-1);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            // mTextPathSegment = value / 360;
            mArcStartAngleValuesAfterRotate.clear();
            for (int i = 0; i < mCountOfArc; i++) {
                mArcStartAngleValuesAfterRotate.add(mArcStartAngleValues.get(i) + value * mSpeedRate % 360);
            }
            postInvalidate();
        });
        mAnimator.start();
    }

    public void setAllTexts(ArrayList<String> list) {
        if (list == null)list = new ArrayList<>();
        this.mTotalText = list;
        this.mCurrText.clear();
        for (int i = 0; i < mTotalText.size(); i++) {
            if (i < mCountOfShownText) {
                mCurrText.add(mTotalText.get(i));
            }
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        LogUtils.v(TAG, "width:[" + width + "], height:[" + height + "]");
        LogUtils.v(TAG, "start calc all the path of text, it will be random automatic generate");
        mTextPathMapping.clear();
        Path path = new Path();
        Point center = new Point(width / 2, height / 2);
        Point startPoint;
        Point bezierController;
        for (int i = 0; i < mTotalText.size(); i++) {
            path = new Path();
            startPoint = getTextRandomStartPoint();
            bezierController = getBezierPoint(startPoint, center);
            path.reset();
            path.moveTo(startPoint.x, startPoint.y);
            path.quadTo(bezierController.x, bezierController.y, center.x, center.y);
            // path.close(); this place the close method can't be call
            mTextPathMapping.put(mTotalText.get(i), path);
        }


        Path path2 = new Path();
    }

    /**
     * @return point
     */
    private Point getTextRandomStartPoint() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int radius = (int) Math.sqrt((width / 2) * (width / 2) + height * height / 4);
        int pointX, pointY;
        int centerX = width / 2;
        int centerY = height / 2;
        Random random = new Random();
        int randomPointIndex = random.nextInt(361);
        pointX = (int) (centerX - radius * Math.sin(Math.PI * (randomPointIndex - 90) / 180));
        pointY = (int) (centerY - radius - 10 + radius * Math.cos(Math.PI * (randomPointIndex - 90) / 180));
        LogUtils.v(TAG, "start point:[" + 0 + ", " + 0 + "]");
        return new Point(pointX, pointY);
    }

    /**
     * 已知三角形两点及两线 A(x1,y1), B(x2,y2), 求第三点坐标C(x3,y3)
     x3=（x1ctgA2+x2ctgA1+y2-y1）/（ctgA1+ctgA2）
     y3=（y1ctgA2+y2ctgA1+x1-x2）/（ctgA1+ctgA2）
     * @param aPoint
     * @param bPoint
     * @return
     */
    private Point getBezierPoint(Point aPoint, Point bPoint) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int radiusLarge = width / 2;
        float radius = (mCenterRectF.bottom - mCenterRectF.top) / 2;
        float bezierRadius = radius / 2;
        float lengthAC = (float) Math.sqrt(radiusLarge * radiusLarge + bezierRadius * bezierRadius);
        int cPointX = 100, cPointY = 200;
        cPointX = (int) (aPoint.x + lengthAC * Math.cos(90));
        cPointY = (int) (aPoint.y + lengthAC * Math.sin(90));
        LogUtils.v(TAG, "controller point:[" + cPointX + ", " + cPointX + "]" + "///" + Math.cos(90) + "///" + lengthAC);
        return new Point(cPointX, cPointY);
    }



    /**
     * 开启 path measure 绘制 path 路径
     */
    public void startTextAnimator() {
        mTextPathMeasureAnimator = ValueAnimator.ofInt(0, 100);
        mTextPathMeasureAnimator.setDuration(mTextLineDuration);
        mTextPathMeasureAnimator.setRepeatCount(mTotalText.size());
        mTextPathMeasureAnimator.setInterpolator(new LinearInterpolator());
        mTextPathMeasureAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mTextPathSegment = value;
                LogUtils.v(TAG, "segment value:" + value);
                postInvalidate();
            }
        });
        mTextPathMeasureAnimator.start();
    }

    public void startTextAnimator1() {
        ValueAnimator mText1Animator = ValueAnimator.ofInt(0, 100);
        mText1Animator.setDuration(mTextLineDuration);
        mText1Animator.setRepeatCount(mTotalText.size());
        mText1Animator.setInterpolator(new LinearInterpolator());
        mText1Animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mTextPathSegments[1] = value;
                LogUtils.v(TAG, "segment value:" + value);
                postInvalidate();
            }
        });
        mText1Animator.start();
    }

    public void startTextAnimator2() {
        ValueAnimator mText2Animator = ValueAnimator.ofInt(0, 100);
        mText2Animator.setDuration(mTextLineDuration);
        mText2Animator.setRepeatCount(mTotalText.size());
        mText2Animator.setInterpolator(new LinearInterpolator());
        mText2Animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mTextPathSegments[2] = value;
                postInvalidate();
            }
        });
        mText2Animator.start();
    }

    public void startTextAnimator3() {
        ValueAnimator mText3Animator = ValueAnimator.ofInt(0, 100);
        mText3Animator.setDuration(mTextLineDuration);
        mText3Animator.setRepeatCount(mTotalText.size());
        mText3Animator.setInterpolator(new LinearInterpolator());
        mText3Animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mTextPathSegments[3] = value;
                postInvalidate();
            }
        });
        mText3Animator.start();
    }

    public void startTextAnimator4() {
        ValueAnimator mText4Animator = ValueAnimator.ofInt(0, 100);
        mText4Animator.setDuration(mTextLineDuration);
        mText4Animator.setRepeatCount(mTotalText.size());
        mText4Animator.setInterpolator(new LinearInterpolator());
        mText4Animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mTextPathSegments[4] = value;
                postInvalidate();
            }
        });
        mText4Animator.start();
    }

    private int index = 0;


}
