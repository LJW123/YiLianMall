package com.yilian.mall.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by liuyuqi on 2017/3/31 0031.
 */

public class mCustomButton extends Button {
    public mCustomButton(Context context) {
        super(context);
    }

    public mCustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mCustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *  计算所有的ChildView的宽度和高度，然后根据ChildView的计算结果，设置自己的宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 对齐所有的ChildView确定位置
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    /**
     * 开始绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
