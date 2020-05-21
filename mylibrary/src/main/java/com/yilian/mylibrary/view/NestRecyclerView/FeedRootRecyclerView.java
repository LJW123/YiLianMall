package com.yilian.mylibrary.view.NestRecyclerView;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 作者： Zg   时间： 2018/11/18.
 * 修复垂直滑动RecyclerView嵌套水平滑动RecyclerView水平滑动不灵敏问题
 * https://www.cnblogs.com/ldq2016/p/5952726.html
 */
public class FeedRootRecyclerView extends BetterRecyclerView{
    public FeedRootRecyclerView(Context context) {
        this(context, null);
    }

    public FeedRootRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeedRootRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    /* do nothing */
    }
}
