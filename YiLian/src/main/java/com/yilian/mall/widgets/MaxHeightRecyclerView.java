package com.yilian.mall.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.yilian.mylibrary.DPXUnitUtil;

/**
 * @author xiaofo on 2018/7/4.
 */

public class MaxHeightRecyclerView extends RecyclerView {

    public MaxHeightRecyclerView(Context context) {
        super(context);
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = MeasureSpec.makeMeasureSpec(DPXUnitUtil.dp2px(getContext(),240), MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}
