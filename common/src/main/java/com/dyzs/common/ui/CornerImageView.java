package com.dyzs.common.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.dyzs.common.R;

/**
 * @author dyzs
 * Created on 2016/4/1.
 * 自定义圆角图片，使用 path + 贝塞尔曲线 + clip裁剪
 * 仅用于正方形图片~~
 *
 * TODO
 * 1、添加 xml 配置方式的半径
 * 2、半径的 get 和 set 方法，限制半径大小不超过当前控件宽度的一半
 *
 * 可以在 xml 中配置 src 或者 bg，但是一定得是透明的，可以设置一个透明的有边框的 shape 资源文件
 *
 */
@SuppressLint("AppCompatCustomView")
public class CornerImageView extends ImageView{
    private float mViewWidth, mViewHeight;
    private float mCircleRadius = 10f;          // 圆角半径

    private Path clipPath;
    private Paint paint;

    private Bitmap mBitmap;         // 背景图片
    private Bitmap mSelectedBitmap; // 选中的样式
    private int mImageResources;    // 资源id
    private int mPaintColor;        // 画笔颜色
    private int mPureColorBg = -1;  // 背景纯颜色

    private boolean mSelectedState; // 选中标记
    private Context mContext;
    /**
     *
     * @param context
     */
    public CornerImageView(Context context) {
        this(context, null);
    }
    public CornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }
    private void init(AttributeSet attrs){
        // 需要声明的 xml 属性
        mCircleRadius = 30f;
        mPureColorBg = -1;
        mPaintColor = Color.BLACK;
        mImageResources = 0;
        mSelectedState = false;

        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CornerImageView, 0, 0);
        mCircleRadius = ta.getDimension(R.styleable.CornerImageView_circleRadius, mCircleRadius);
        mPureColorBg = ta.getColor(R.styleable.CornerImageView_pureColor, mPureColorBg);
        mPaintColor = ta.getColor(R.styleable.CornerImageView_paintColor, mPaintColor);
//        mImageResources = ta.getResourceId(R.styleable.CornerImageView_imageResource, mImageResources);
        mSelectedState = ta.getBoolean(R.styleable.CornerImageView_selectedState, mSelectedState);
        ta.recycle();
        clipPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth() * 1.0f;
        mViewHeight = getMeasuredHeight() * 1.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float[] mFloatPoints = new float[16];
        // A
        mFloatPoints[0] = 0f;                           // A pointX
        mFloatPoints[1] = mCircleRadius;                // A pointY
        // B
        mFloatPoints[2] = mCircleRadius;
        mFloatPoints[3] = 0f;
        // C
        mFloatPoints[4] = mViewWidth - mCircleRadius;
        mFloatPoints[5] = 0f;
        // D
        mFloatPoints[6] = mViewWidth;
        mFloatPoints[7] = mCircleRadius;
        // E
        mFloatPoints[8] = mViewWidth;
        mFloatPoints[9] = mViewHeight - mCircleRadius;
        // F
        mFloatPoints[10] = mViewWidth - mCircleRadius;
        mFloatPoints[11] = mViewHeight;
        // G
        mFloatPoints[12] = mCircleRadius;
        mFloatPoints[13] = mViewHeight;
        // H
        mFloatPoints[14] = 0f;
        mFloatPoints[15] = mViewHeight - mCircleRadius;
        // 两条直线绘制，用 path


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mPaintStrokeWidth);
        paint.setColor(mPaintColor);

        clipPath.reset();                       // 重置
        clipPath.moveTo(mFloatPoints[0], mFloatPoints[1]);     // 移动到 A 点开始，为起始点
        float controlPointX = 0f;               // A 点的操纵点 X
        float controlPointY = 0f;               // A 点的操纵点 Y
        // 绘制 A 点到 B 点的贝塞尔, 中心点为左上角
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[2], // 终点x
                mFloatPoints[3]  // 终点y
        );

        // 曲线到达B点，再lineTo到C
        clipPath.lineTo(mFloatPoints[4], mFloatPoints[5]);
        // 曲线达到 C 点，通过贝塞尔曲线，绘制到 D 点，中心点为右上角
        controlPointX = mViewWidth;
        controlPointY = 0f;
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[6], // 终点x
                mFloatPoints[7]  // 终点y
        );
        // 曲线到达 D 点，再 lineTo 到 E
        clipPath.lineTo(mFloatPoints[8], mFloatPoints[9]);
        // 曲线达到 E 点，通过贝塞尔曲线，绘制到 F 点，中心点为右下角
        controlPointX = mViewWidth;
        controlPointY = mViewHeight;
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[10],// 终点x
                mFloatPoints[11] // 终点y
        );
        // 曲线到达 F 点，再 lineTo 到 G
        clipPath.lineTo(mFloatPoints[12], mFloatPoints[13]);
        // 曲线达到 G 点，通过贝塞尔曲线，绘制到 H 点，中心点为左下角
        controlPointX = 0f;
        controlPointY = mViewHeight;
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[14],// 终点x
                mFloatPoints[15] // 终点y
        );
        // 曲线到达 G 点，再 lineTo 到 A，完成一圈绘制
        clipPath.lineTo(mFloatPoints[0], mFloatPoints[1]);
        clipPath.close();
        clipPath.setFillType(Path.FillType.WINDING);
        canvas.clipPath(clipPath);

        // 如果设置了颜色，那么就取代背景图片
        if (mPureColorBg != -1) {
            canvas.save();
            canvas.drawColor(mPureColorBg);
            canvas.restore();
        }
        else if (mBitmap != null) {
            canvas.save();
            canvas.drawBitmap(mBitmap, 0f, 0f, null);
            canvas.restore();
        }
        if (mSelectedState) {
            canvas.save();
            canvas.drawBitmap(mSelectedBitmap, 0f, 0f, null);
            canvas.restore();
        }
        // 边框绘制~
        if (isDrawBorder) {
            canvas.save();
            canvas.drawPath(clipPath, paint);
            canvas.restore();
        } else {    // 默认的边框
            canvas.save();
            canvas.drawPath(clipPath, paint);
            canvas.restore();
        }
        super.onDraw(canvas);
    }

    public int getPaintColor() {
        return mPaintColor;
    }

    public void setPaintColor(int color) {
        this.mPaintColor = color;
        invalidate();
    }

    public void setCornerRadius(float radius) {
        if (radius > Math.min(mViewWidth, mViewHeight) / 2) {
            this.mCircleRadius = mViewWidth / 3;
        }
        this.mCircleRadius = radius;
    }

    /**
     * @param color context.getResources.getColor
     */
    public void setPureColor(int color) {
        this.mPureColorBg = color;
        invalidate();
    }

    /**
     * 只能设置小图片
     * @param bitmap
     */
    public void setSmallBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        invalidate();
    }

    public synchronized void setSelectedState(boolean b) {
        this.mSelectedState = b;
        invalidate();
    }
    public void setSelectedBitmap(Bitmap bitmap) {
        this.mSelectedBitmap = bitmap;
    }

    private float mPaintStrokeWidth = 3f;
    public void setPaintStrokeWidth(float f) {
        mPaintStrokeWidth = f;
    }

    private boolean isDrawBorder = false;
    // 绘制边框, 选中和未选中样式
    public void setIsDrawBorder(boolean b) {
        isDrawBorder = b;
    }
}
