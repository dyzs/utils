package com.dyzs.common.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyzs.common.R;

import java.util.Locale;

/**
 *
 */
public class TabViewPagerIndicator extends HorizontalScrollView {

    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[] {
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    // @formatter:on

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public ViewPager.OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private boolean checkedTabWidths = false;

    private int indicatorColor = 0x00000000 ;//#0084FF
    private int underlineColor = 0x00000000;
    private int dividerColor = 0xfff2f5f6;

    private boolean shouldExpand = false;
    private boolean textAllCaps = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 100;//线的高度
    private int underlineHeight = 0;
    private int dividerPadding = 0;
    private int tabPadding = 15;
    private float dividerWidth = 0.5f;//分割线的宽度

    private float tabTextSize = 17;//当前文字大小
    private float tabTextSizeNormal = 14;//默认文字大小
    private int tabTextColor = 0xFFffffff;//当前文字颜色
    private int tabTextColorNormal = 0x77FFFFFF;//默认文字颜色

    private int lastScrollX = 0;

    private Context context;

    private int tabBackgroundResId;// = R.drawable.background_tab;//title按下的selector

    private Locale locale;

    public TabViewPagerIndicator(Context context) {
        this(context, null);
    }

    public TabViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabTextSize, dm);
        tabTextSizeNormal = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabTextSizeNormal, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, (int)tabTextSize);
        tabTextSizeNormal = a.getDimensionPixelSize(0, (int)tabTextSizeNormal);
        tabTextColor = a.getColor(1, tabTextColor);
        tabTextColorNormal = a.getColor(1, tabTextColorNormal);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.TabViewPagerIndicator);

        indicatorColor = a.getColor(R.styleable.TabViewPagerIndicator_indicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.TabViewPagerIndicator_underlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.TabViewPagerIndicator_dividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.TabViewPagerIndicator_indicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.TabViewPagerIndicator_underlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.TabViewPagerIndicator_pst_dividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.TabViewPagerIndicator_tabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.TabViewPagerIndicator_tabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.TabViewPagerIndicator_shouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.TabViewPagerIndicator_scrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.TabViewPagerIndicator_pst_textAllCaps, textAllCaps);

        tabTextSize = a.getDimensionPixelSize(R.styleable.TabViewPagerIndicator_tabTextSize, (int) tabTextSize);
        tabTextSizeNormal = a.getDimensionPixelSize(R.styleable.TabViewPagerIndicator_tabTextSizeNormal, (int) tabTextSizeNormal);

        tabTextColor = a.getColor(R.styleable.TabViewPagerIndicator_tabTextColor, tabTextColor);
        tabTextColorNormal = a.getColor(R.styleable.TabViewPagerIndicator_tabTextColorNormal, tabTextColorNormal);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        //设置每一个tab的宽度(现在是平均分成两个)
        //defaultTabLayoutParams = new LinearLayout.LayoutParams((ScreenUtils.getScreenWidth(context)/2), LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    private int mPosition = 0;
    public void setPosition(int pos){
        this.mPosition = pos;
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        //设置每一个tab的宽度(现在是平均分成两个)
        int count = pager.getAdapter().getCount();
        if(count>5){
            defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }else{
            defaultTabLayoutParams = new LinearLayout.LayoutParams(((context.getResources().getDisplayMetrics().widthPixels/count)), LayoutParams.MATCH_PARENT);
        }

        pager.setOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {

            if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
            } else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }

        }

        updateTabStyles();

        checkedTabWidths = false;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                getViewTreeObserver().removeGlobalOnLayoutListener(this);
//				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//				} else {
//					getViewTreeObserver().removeOnGlobalLayoutListener(this);
//				}

                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });

    }

    private void addTextTab(final int position, String title) {

        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setFocusable(true);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();

        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tabsContainer.addView(tab);

    }

    private void addIconTab(final int position, int resId) {

        ImageButton tab = new ImageButton(getContext());
        tab.setFocusable(true);
        tab.setImageResource(resId);

        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tabsContainer.addView(tab);

    }

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            v.setLayoutParams(defaultTabLayoutParams);
            v.setBackgroundResource(tabBackgroundResId);
            if (shouldExpand) {
                v.setPadding(0, 0, 0, 0);
            } else {
                v.setPadding(tabPadding, 0, tabPadding, 0);
            }

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, (i==mPosition)?tabTextSize:tabTextSizeNormal);
               // tab.setTypeface(tabTypeface, tabTypefaceStyle);
                tab.setTextColor((i==mPosition)?tabTextColor:tabTextColorNormal);

                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!shouldExpand || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            return;
        }

        int myWidth = getMeasuredWidth();
        int childWidth = 0;
        for (int i = 0; i < tabCount; i++) {
            childWidth += tabsContainer.getChildAt(i).getMeasuredWidth();
        }

        if (!checkedTabWidths && childWidth > 0 && myWidth > 0) {

            if (childWidth <= myWidth) {
                for (int i = 0; i < tabCount; i++) {
                    tabsContainer.getChildAt(i).setLayoutParams(expandedTabLayoutParams);
                }
            }

            checkedTabWidths = true;
        }
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        // draw indicator line

        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

        // draw underline

        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // draw divider

        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;

            //CommLog.e("currentPosition:"+currentPosition);
            //CommLog.e("currentPositionOffset:"+currentPositionOffset);
            //CommLog.e("%:"+(int)(currentPositionOffset*100));
            float size = tabTextSize-tabTextSizeNormal;
            float fSize = (size/100);//将字体大小的差距分寸100份
            int scrollSize = (int)(currentPositionOffset*100);//滚动的份数
            //CommLog.e("每份的大小:"+fSize);
            //CommLog.e("滚动的份数:"+scrollSize);
            //CommLog.e("大小:"+(tabTextSize-fSize*scrollSize));
            /**
             * 添加字体大小和颜色改变
             */
            //使当前item字体大小改变
            for (int i = 0; i < tabCount; i++) {
                View v = tabsContainer.getChildAt(i);
                if(i==currentPosition){
                    if(v instanceof TextView){
                        TextView textView = (TextView) v;
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize-fSize*scrollSize);
                        int color = getCurrentColor((float)scrollSize/100,tabTextColor,tabTextColorNormal);
                        textView.setTextColor(color);
                    }
                }else if(i==currentPosition+1){
                    if(v instanceof TextView){
                        TextView textView = (TextView) v;
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSizeNormal+fSize*scrollSize);
                        int color = getCurrentColor((float) scrollSize/100,tabTextColorNormal,tabTextColor);
                        textView.setTextColor(color);
                    }
                }else{
                    if(v instanceof TextView){
                        TextView textView = (TextView) v;
                        textView.setTextColor(tabTextColorNormal);
                    }
                }
            }



            /*View v = tabsContainer.getChildAt(currentPosition);
            if(v instanceof TextView){
                TextView textView = (TextView) v;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize-fSize*scrollSize);
                int color = getCurrentColor((float)scrollSize/100,tabTextColor,tabTextColorNormal);
                textView.setTextColor(color);
            }
            View v2 = tabsContainer.getChildAt(currentPosition+1);
            if(v2 instanceof TextView){
                TextView textView = (TextView) v2;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSizeNormal+fSize*scrollSize);
                int color = getCurrentColor((float) scrollSize/100,tabTextColorNormal,tabTextColor);
                textView.setTextColor(color);
            }*/

            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }

            //使当前item高亮(现在不需要)
            /*for (int i = 0; i < tabCount; i++) {
                View v = tabsContainer.getChildAt(i);
                if(v instanceof TextView){
                    TextView textView = (TextView) v;
                    //textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, i==pager.getCurrentItem()?tabTextSize:tabTextSizeNormal);
                    textView.setTextColor(i==pager.getCurrentItem()?tabTextColor:tabTextColorNormal);
                }
            }*/
        }
    }

    /**
     * 根据fraction值来计算当前的颜色。取值(0-1)
     */
    private int getCurrentColor(float fraction, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + fraction * redDifference);
        blueCurrent = (int) (blueStart + fraction * blueDifference);
        greenCurrent = (int) (greenStart + fraction * greenDifference);
        alphaCurrent = (int) (alphaStart + fraction * alphaDifference);

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public float getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
       // this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
