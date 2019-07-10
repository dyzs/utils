package com.dyzs.common.ui;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dyzs.common.utils.LogUtils;

/**
 * User: LJM
 * Date&Time: 2016-12-07 & 15:44
 * Describe: 伸缩视图
 */
public class StretchView_VerBackup extends ViewGroup {

    public static String TAG = "STRETCH_VIEW";
    private View mUpPart;       // 上半部分
    private View mDownPart;     // 下半部分
    private ViewDragHelper viewDragHelper;
    private int mUpPartWidth;
    private int mUpPartHeight;
    private int mDownPartWidth;
    private int mDownPartHeight;
    private boolean isFullStretch = true;

    public StretchView_VerBackup(Context context) {
        super(context);
    }

    public StretchView_VerBackup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchView_VerBackup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mUpPart = getChildAt(0);
        mDownPart = getChildAt(1);
        viewDragHelper = ViewDragHelper.create(this, new StretchDragHelper());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // measure upPart
        LayoutParams upPartLayoutParams = mUpPart.getLayoutParams();
        int upPartMeasureHeight = MeasureSpec.makeMeasureSpec(upPartLayoutParams.height,MeasureSpec.EXACTLY);
        mUpPart.measure(widthMeasureSpec,upPartMeasureHeight);

        // measure downPart
        LayoutParams downLayoutParams = mDownPart.getLayoutParams();
        int downMeasurePartHeight = MeasureSpec.makeMeasureSpec(downLayoutParams.height,MeasureSpec.EXACTLY);
        mDownPart.measure(widthMeasureSpec,downMeasurePartHeight);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mUpPartWidth = mUpPart.getMeasuredWidth();
        mUpPartHeight = mUpPart.getMeasuredHeight();
        showTag("mUpPartWidth  "+ mUpPartWidth);
        showTag("mUpPartHeight  "+ mUpPartHeight);
        mUpPart.layout(0,0, mUpPartWidth, mUpPartHeight); // 摆放上部分的位置


        mDownPartWidth = mDownPart.getMeasuredWidth();
        mDownPartHeight = mDownPart.getMeasuredHeight();
        showTag("mDownPartWidth  "+ mDownPartWidth);
        showTag("mDownPartHeight  "+ mDownPartHeight);
        mDownPart.layout(0, mUpPartHeight,
                mDownPartWidth, mUpPartHeight + mDownPartHeight); // 摆放删除部分的位置
        
    }


    /**
     * ViewDragHelper
     *
     * 使用ViewDragHelper必须复写onTouchEvent并调用这个方法,才能使touch被消费
     */
    class StretchDragHelper extends  ViewDragHelper.Callback {

        /**
         * Touch的down事件会回调这个方法 tryCaptureView
         *
         * @Child：指定要动的孩子  （哪个孩子需要动起来）
         * @pointerId: 点的标记
         * @return : ViewDragHelper是否继续分析处理 child的相关touch事件
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mDownPart == child;
        }


        /**
         *
         * 捕获了水平方向移动的位移数据
         * @param child 移动的孩子View
         * @param left 父容器的左上角到孩子View的距离
         * @param dx 增量值，其实就是移动的孩子View的左上角距离控件（父亲）的距离，包含正负
         * @return 如何动
         *
         * 调用完此方法，在android2.3以上就会动起来了，2.3以及以下是海动不了的
         * 2.3不兼容怎么办？没事，我们复写onViewPositionChanged就是为了解决这个问题的
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        /**
         *  捕获了垂直方向移动的位移数据
         * @param child
         * @param top
         * @param dy
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //return super.clampViewPositionVertical(child, top, dy);
            showTag("clampViewPositionVertical  "+ top);

            // 颜色的边界控制
            /*if(top<0){
                return 0;
            }else if(top>mUpPartHeight){
                return mUpPartHeight;
            }*/

            return top;

        }

        /**
         * 当View的位置改变时的回调
         * @param changedView  哪个View的位置改变了
         * @param left  changedView的left
         * @param top  changedView的top
         * @param dx x方向的上的增量值
         * @param dy y方向上的增量值  取值范围为 ±24000 之间  向下为正,向上负
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            invalidate();
            showTag("onViewPositionChanged  left:"+ left + "// top:" + top + "// dy:" + dy);
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        /**
         * release 状态
         * @param releasedChild
         * @param xVel
         * @param yVel
         */
        @Override
        public void onViewReleased(View releasedChild, float xVel, float yVel) {
            // 方法的参数里面没有top，那么我们就采用 getTop()这个方法
            int releasePartTop = mDownPart.getTop();
            showTag("记录 yVel  " + yVel);

            float changeStatusValue = 200;
            if (yVel > changeStatusValue && isFullStretch == false) { // 关闭状态下,向下的滑动速率足够即使不到一半也展开downPartView
                openStretchView();
            } else if (yVel < -changeStatusValue && isFullStretch == true) { // 打开状态,向上的滑动速率足够也关闭downPartView
                closeStretchView();
            } else {
                // 普通滑动速率,以为upPart的中间点为临界点
                if ((mUpPartHeight * 0.5) > releasePartTop) {
                    //mDownPart.layout(0,0, mUpPartWidth, mUpPartHeight);
                    // 利用smoothSlideViewTo 产生平滑过渡的效果  (需要结合invalidate)
                    closeStretchView();
                } else {
                    //mDownPart.layout(0, mUpPartHeight,mDownPartWidth, mUpPartHeight + mDownPartHeight);
                    showTag("启动第三形态：");
                    openStretchView();
                }

            }
            invalidate();
            super.onViewReleased(releasedChild, yVel, yVel);
        }

        /**
         * 整个View拓展起来
         */
        private void openStretchView() {
            viewDragHelper.smoothSlideViewTo(mDownPart,0, mUpPartHeight);
            isFullStretch = true;
        }

        /**
         * 整个view收缩起来
         * @return
         */
        private void closeStretchView() {
            viewDragHelper.smoothSlideViewTo(mDownPart,0,0);
            isFullStretch = false;
        }

        /**
         * View 第三形态启动
         */
        private void thirdStretchView() {
            viewDragHelper.smoothSlideViewTo(mDownPart, 0, -mUpPartHeight);
        }
    }


    @Override
    public void computeScroll() {
        //super.computeScroll();
        // 把捕获的View适当的时间移动，其实也可以理解为 smoothSlideViewTo 的模拟过程还没完成
        if(viewDragHelper.continueSettling(true)){
            invalidate();
        }
        // 其实这个动画过渡的过程大概在怎么走呢？
        // 1、smoothSlideViewTo方法进行模拟数据，模拟后就就调用invalidate();
        // 2、invalidate()最终调用computeScroll，computeScroll做一次细微动画，
        //    computeScroll判断模拟数据是否彻底完成，还没完成会再次调用invalidate
        // 3、递归调用，知道数据noni完成。
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        /**Process a touch event received by the parent view. This method will dispatch callback events
         as needed before returning. The parent view's onTouchEvent implementation should call this. */
        viewDragHelper.processTouchEvent(event); // 使用ViewDragHelper必须复写onTouchEvent并调用这个方法
        return true; //消费这个touch
    }


    private void showTag(String str){
        LogUtils.v(TAG, str);
    }

}
