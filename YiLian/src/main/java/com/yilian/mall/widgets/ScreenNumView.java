package com.yilian.mall.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yilian.mall.R;

import java.util.ArrayList;


/**
 * view pager 内容下标
 * @author lauyk
 *
 */
public class ScreenNumView extends LinearLayout {

    private Context mContext;
    /** item 指示的宽度 */
    private static int ITEM_WIDTH;
    /** item 指示的宽度 */
    private static int ITEM_HEIGHT;
    /** 默认指示图片 */
    private Drawable mDefault;
    /** 当前指示指示图片 */
    private Drawable mCurrentPage;
    private int mCurrentNum = 0;
    private int mScreenNum;
    private int dotGap;

    private ArrayList<ImageView> childList = new ArrayList<ImageView>();

    public ScreenNumView(Context context) {
        super(context);
        mContext = context;
        initDrawables();
    }

    public ScreenNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initDrawables();
    }

    public ScreenNumView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mContext = context;
        initDrawables();
    }

    @Override
    protected void onFinishInflate() {
        removeAllViews();
    }

    private void initDrawables() {
        Resources res = mContext.getResources();
        ITEM_WIDTH = ViewGroup.LayoutParams.WRAP_CONTENT;
        ITEM_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;
        dotGap = 10;
        mDefault = res.getDrawable(R.mipmap.circle_off);
        mCurrentPage = res.getDrawable(R.mipmap.circle_on);
    }

    // add begin by ljh
    public void setDefaultPointerImage(int imageId) {
        if (mContext != null) {
            mDefault = mContext.getResources().getDrawable(imageId);
        }

        redrawLayout();
    }

    public void setCurrentPointerImage(int imageId) {
        if (mContext != null) {
            mCurrentPage = mContext.getResources().getDrawable(imageId);
        }
        redrawLayout();
    }

    public void initScreen(int screenNumber, int currentNum) {
        mCurrentNum = currentNum;
        if (mCurrentNum < 0) {
            mCurrentNum = 0;
        }
        if (mCurrentNum >= screenNumber) {
            mCurrentNum = screenNumber - 1;
        }
        initScreen(screenNumber);
    }

    public void initScreen(int screenNumber) {
        mScreenNum = screenNumber;
        setOrientation(LinearLayout.HORIZONTAL);
        redrawLayout();
    }

    public void redrawLayout() {
        removeAllViews();
        childList.clear();
        for (int i = 0; i < mScreenNum; i++) {
            ImageView child = new ImageView(mContext);
            //child.setScaleType(ScaleType.CENTER);
            if (i == mCurrentNum) {
                child.setImageDrawable(mCurrentPage);
            } else {
                child.setImageDrawable(mDefault);
            }
            childList.add(child);

            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                    ITEM_WIDTH, ITEM_HEIGHT);
            mLayoutParams.leftMargin = dotGap;
            mLayoutParams.rightMargin = dotGap;
            addView(child, mLayoutParams);
        }
        requestLayout();
    }

    public void setCurrentNum(int currentNum) {
        mCurrentNum = currentNum;
        redrawLayout();
    }

    public void snapToPage(int gotoIndex) {
        if (childList == null || childList.size() == 0
                || gotoIndex >= mScreenNum || gotoIndex == mCurrentNum
                || gotoIndex > (childList.size() - 1)) {
            return;
        }
        ImageView newChild = childList.get(gotoIndex);
        if (newChild == null) {
            return;
        }
        ImageView oldChild = childList.get(mCurrentNum);
        if (oldChild == null) {
            return;
        }

        newChild.setImageDrawable(mCurrentPage);
        oldChild.setImageDrawable(mDefault);
        mCurrentNum = gotoIndex;
        invalidate();
    }

    @Override
    public void setChildrenDrawingCacheEnabled(boolean enabled) {
        final int count = getChildCount();

        setDrawingCacheEnabled(enabled);
        // Update the drawing caches
        buildDrawingCache(true);

        for (int i = 0; i < count; i++) {
            final View view = getChildAt(i);
            view.setDrawingCacheEnabled(enabled);
            // Update the drawing caches
            view.buildDrawingCache(true);
        }
    }

    @Override
    public void setChildrenDrawnWithCacheEnabled(boolean enabled) {
        super.setChildrenDrawnWithCacheEnabled(enabled);
    }

}