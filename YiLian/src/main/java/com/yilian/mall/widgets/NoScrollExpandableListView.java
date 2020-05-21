package com.yilian.mall.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by yukun on 2016/4/25.
 */
public class NoScrollExpandableListView extends ExpandableListView{
    public NoScrollExpandableListView(Context context) {
        super(context);
    }
    public NoScrollExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int newHeightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, newHeightSpec);
    }
}
