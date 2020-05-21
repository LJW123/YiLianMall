package com.yilian.mall.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by yukun on 2016/4/25.
 */
public class NoScrollRecyclerView extends RecyclerView{
    public NoScrollRecyclerView(Context context) {
        super(context);
    }
    public NoScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int newHeightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, newHeightSpec);
    }
}
