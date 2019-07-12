package com.dyzs.common.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dyzs.common.utils.LogUtils;

/**
 * @author DYZS
 * 参考：
 * https://www.jianshu.com/p/485b9b340436
 * https://gitee.com/amqr/LikePaperStretch.git
 */
public class StretchView2 extends ViewGroup {
    public static String TAG = StretchView2.class.getSimpleName();

    /**
     * {@link ViewConfiguration#MAXIMUM_FLING_VELOCITY}
     * Maximum velocity to initiate a fling, as measured in dips per second
     */
    private static final int MAXIMUM_FLING_VELOCITY = 8000;

    private View mPartSliderReferences;
    private View mPartSlider;
    private ViewDragHelper mViewDragHelper;
    private int mPartReferencesWidth;
    private int mPartReferencesHeight;
    private int mPartSliderWidth;
    private int mPartSliderHeight;
    private boolean isAutoScrolling = false;
    private StretchViewStatus mStretchViewStatus = StretchViewStatus.STATUS_INIT;
    private int mTouchSlop;

    public StretchView2(Context context) {
        super(context);
    }

    public StretchView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mPartSliderReferences = getChildAt(0);
        mPartSlider = getChildAt(1);
        mViewDragHelper = ViewDragHelper.create(this, 1f,  new StretchDragHelper());

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        LayoutParams layoutParams = mPartSliderReferences.getLayoutParams();
        int measureHeight = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
        mPartSliderReferences.measure(widthMeasureSpec,measureHeight);

        LayoutParams sliderLayoutParams = mPartSlider.getLayoutParams();
        int sliderMeasureHeight = MeasureSpec.makeMeasureSpec(sliderLayoutParams.height, MeasureSpec.EXACTLY);
        mPartSlider.measure(widthMeasureSpec, sliderMeasureHeight);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        LogUtils.v(TAG, "onLayout...........");
        if (changed) {
            // 摆放滑动参照物的位置
            mPartReferencesWidth = mPartSliderReferences.getMeasuredWidth();
            mPartReferencesHeight = mPartSliderReferences.getMeasuredHeight();
            mPartSliderReferences.layout(0,0, mPartReferencesWidth, mPartReferencesHeight);

            // 摆放滑动部分的位置
            mPartSliderWidth = mPartSlider.getMeasuredWidth();
            mPartSliderHeight = mPartSlider.getMeasuredHeight();
            mPartSlider.layout(0, 0, mPartSliderWidth, mPartSliderHeight);
        }
        
    }


    /**
     * ViewDragHelper
     *
     * 使用ViewDragHelper必须复写onTouchEvent并调用这个方法,才能使touch被消费
     */
    class StretchDragHelper extends  ViewDragHelper.Callback {

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return 1;
        }

        private String getStateValue(int state) {
            if (state == 1) {
                return "dragging";
            }
            if (state == 2) {
                return "auto scroll";
            }
            return "idle";
        }
        @Override
        public void onViewDragStateChanged(int state) {
            showTag("onViewDragStateChanged:状态码：[" + state + "] /// 状态值:[" + getStateValue(state) + "]");
            if (state == 0) {
                switch (mStretchViewStatus) {
                    // up 状态与 init 状态下, Cover 偏移和 Pic 偏移一样
                    case STATUS_PUSH_UP:
                    case STATUS_INIT:
                        if (mHoldCoverView != null && mHoldCoverView.getTop() != 0) {
                            mHoldCoverView.layout(0, mPartPicTotalOffset, mHoldCoverView.getMeasuredWidth(), mHoldCoverView.getMeasuredHeight() + mPartPicTotalOffset);
                        }
                        if (mHoldPartPicView != null) {
                            mHoldPartPicView.layout(0, 0, mHoldPartPicView.getMeasuredWidth(), mHoldPartPicView.getMeasuredHeight());
                        }
                        break;
                    case STATUS_PULL_DOWN:
                        if (mHoldCoverView != null && mHoldCoverView.getTop() != mPartReferencesHeight) {
                            mHoldCoverView.layout(0, mPartPicTotalOffset + mPartReferencesHeight, mHoldCoverView.getMeasuredWidth(), mHoldCoverView.getMeasuredHeight() + mPartReferencesHeight + mPartPicTotalOffset);
                        }
                        if (mHoldPartPicView != null) {
                            mHoldPartPicView.layout(0, mPartPicTotalOffset, mHoldPartPicView.getMeasuredWidth(), mPartPicTotalOffset + mHoldPartPicView.getMeasuredHeight());
                        }
                        break;
                }
            }
            super.onViewDragStateChanged(state);
        }

        /**
         * Touch的down事件会回调这个方法 tryCaptureView
         *
         * @Child：指定要动的孩子  （哪个孩子需要动起来）
         * @pointerId: 点的标记
         * @return : ViewDragHelper是否继续分析处理 child的相关touch事件
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mPartSlider == child;
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
            // showTag("clampViewPositionHorizontal left: " + left + "///dx:" + dx);
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
            switch (mStretchViewStatus) {
                case STATUS_INIT:
                    mHoldPartPicView.layout(0, top, mHoldPartPicView.getMeasuredWidth(), top + mHoldPartPicView.getMeasuredHeight());
                    mHoldCoverView.layout(0, top + mPartPicTotalOffset, mHoldCoverView.getMeasuredWidth(), top + mPartPicTotalOffset + mHoldCoverView.getMeasuredHeight());

                    if (top < 0) { // 当释放拖拽状态，改变为初始化时，如果 top 小于 0，则限制 pic 上移
                        mHoldPartPicView.layout(0, 0, mHoldPartPicView.getMeasuredWidth(), mHoldPartPicView.getMeasuredHeight());
                        mHoldCoverView.layout(0, mPartPicTotalOffset, mHoldCoverView.getMeasuredWidth(), mPartPicTotalOffset + mHoldCoverView.getMeasuredHeight());
                    }
                    if (top > mPartPicTotalOffset) {
                        mHoldPartPicView.layout(0, mPartPicTotalOffset, mHoldPartPicView.getMeasuredWidth(), mPartPicTotalOffset + mHoldPartPicView.getMeasuredHeight());
                    }

                    if (top > mPartReferencesHeight) {
                        mHoldCoverView.layout(0, mPartReferencesHeight + mPartPicTotalOffset, mHoldCoverView.getMeasuredWidth(), mPartReferencesHeight + mPartPicTotalOffset + mHoldCoverView.getMeasuredHeight());
                    }
                    break;
                case STATUS_PULL_DOWN:
                    if (top < 0) top = 0;
                    mHoldPartPicView.layout(0, top, mHoldPartPicView.getMeasuredWidth(), top + mHoldPartPicView.getMeasuredHeight());
                    if (top > mPartPicTotalOffset) {
                        mHoldPartPicView.layout(0, mPartPicTotalOffset, mHoldPartPicView.getMeasuredWidth(), mPartPicTotalOffset + mHoldPartPicView.getMeasuredHeight());
                    }
                    /*if (top <= mPartReferencesHeight) {
                    }*/
                    mHoldCoverView.layout(0, top + mPartPicTotalOffset, mHoldCoverView.getMeasuredWidth(), top + mPartPicTotalOffset + mHoldCoverView.getMeasuredHeight());
                    break;
                case STATUS_PUSH_UP:
                    if (top >= 0) {
                        mHoldCoverView.layout(0, top + mPartPicTotalOffset, mHoldCoverView.getMeasuredWidth(), top + mPartPicTotalOffset + mHoldCoverView.getMeasuredHeight());
                        if (top > mPartPicTotalOffset) top = mPartPicTotalOffset;
                        mHoldPartPicView.layout(0, top, mHoldPartPicView.getMeasuredWidth(), top + mHoldPartPicView.getMeasuredHeight());
                    }
                    break;
            }
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
            int releasePartTop = mPartSlider.getTop();
            showTag("onViewReleased:[" + yVel + "]" + "///releasePartTop[" + releasePartTop + "]");
            float changeStatusValue = 500;
            switch (mStretchViewStatus) {
                case STATUS_INIT:
                    if (yVel > changeStatusValue) {// 快速滑动
                        downStretchView();
                    } else if (yVel < -changeStatusValue) {
                        upStretchView();
                    } else {
                        // 普通速率滑动
                        if (releasePartTop > 0) { // 下拉
                            if ((mPartReferencesHeight * 0.5) > Math.abs(releasePartTop)) {
                                initStretchView();
                            } else {
                                downStretchView();
                            }
                        } else {// 上推
                            if ((mPartReferencesHeight * 0.5) > Math.abs(releasePartTop)) {
                                initStretchView();
                            } else {
                                upStretchView();
                            }
                        }
                    }
                    break;
                case STATUS_PULL_DOWN:
                    if (yVel > changeStatusValue) {
                        downStretchView();
                    } else if (yVel < -changeStatusValue) {
                        initStretchView();
                    } else {
                        // 普通速率滑动
                        if ((mPartReferencesHeight * 0.5) > releasePartTop) {
                            initStretchView();
                        } else {
                            downStretchView();
                        }
                    }
                    break;
                case STATUS_PUSH_UP:
                    if (yVel > changeStatusValue) {
                        initStretchView();
                    } else if (yVel < -changeStatusValue) {
                        upStretchView();
                    } else {
                        // 普通速率滑动
                        releasePartTop = releasePartTop + mPartReferencesHeight;
                        if (releasePartTop > 0) { // 下拉
                            if (releasePartTop > (mPartReferencesHeight * 0.5)) {
                                initStretchView();
                            } else {
                                upStretchView();
                            }
                        } else {// 上推
                            upStretchView();
                        }
                    }
                    break;
            }
            invalidate();
            super.onViewReleased(releasedChild, yVel, yVel);
        }

        /**
         * 下拉状态
         */
        private void downStretchView() {
            mStretchViewStatus = StretchViewStatus.STATUS_PULL_DOWN;
            mViewDragHelper.smoothSlideViewTo(mPartSlider,0, mPartReferencesHeight);
        }

        /**
         * 初始化状态
         */
        private void initStretchView() {
            mStretchViewStatus = StretchViewStatus.STATUS_INIT;
            mViewDragHelper.smoothSlideViewTo(mPartSlider,0,0);
        }

        /**
         * View 第三形态启动
         */
        private void upStretchView() {
            mStretchViewStatus = StretchViewStatus.STATUS_PUSH_UP;
            mViewDragHelper.smoothSlideViewTo(mPartSlider, 0, -mPartReferencesHeight);
        }
    }


    @Override
    public void computeScroll() {
        //super.computeScroll();
        // 把捕获的View适当的时间移动，其实也可以理解为 smoothSlideViewTo 的模拟过程还没完成
        isAutoScrolling = mViewDragHelper.continueSettling(true);
        // showTag("computeScroll:" + isAutoScrolling);
        if(isAutoScrolling) {
            // invalidate();
            ViewCompat.postInvalidateOnAnimation(this);  //是，刷新重绘viewGroup
        }
        // 其实这个动画过渡的过程大概在怎么走呢？
        // 1、smoothSlideViewTo方法进行模拟数据，模拟后就就调用invalidate();
        // 2、invalidate()最终调用computeScroll，computeScroll做一次细微动画，
        //    computeScroll判断模拟数据是否彻底完成，还没完成会再次调用invalidate
        // 3、递归调用，知道数据模拟完成。
    }

    /**
     * 检查是否可以拦截touch事件
     * 如果onInterceptTouchEvent可以return true 则进一步执行onTouchEvent
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //return super.onTouchEvent(event);
        /**Process a touch event received by the parent view. This method will dispatch callback events
         as needed before returning. The parent view's onTouchEvent implementation should call this. */
        mViewDragHelper.processTouchEvent(ev);
        return true;
    }


    private void showTag(String str) {
        LogUtils.v(TAG, str);
    }


    private View mHoldCoverView;
    public void invokeCoverView(View view) {
        mHoldCoverView = view;
    }

    private void scrollCoverView(int offset) {
        if (mHoldCoverView != null) {
            ViewCompat.offsetTopAndBottom(mHoldCoverView, offset);
        }
    }


    private View mHoldPartPicView;
    private int mPartPicTotalOffset = 50;// toolbar 实际高度
    public void invokePartPicView(View view, int totalOffset) {
        this.mHoldPartPicView = view;
        this.mPartPicTotalOffset = totalOffset;
    }

    private void scrollPicView(int offset) {
        if (mHoldPartPicView != null) {
            ViewCompat.offsetTopAndBottom(mHoldPartPicView, offset);
        }
    }



}
