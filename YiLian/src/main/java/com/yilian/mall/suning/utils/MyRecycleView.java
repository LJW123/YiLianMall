package com.yilian.mall.suning.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 防止卡顿
 * Created by Zg on 2018/7/31 0031.
 */

public class MyRecycleView extends RecyclerView {

    public MyRecycleView(Context context) {
        super(context);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //返回false，则把事件交给子控件的onInterceptTouchEvent()处理
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //返回true,则后续事件可以继续传递给该View的onTouchEvent()处理
        return true;
    }
}
