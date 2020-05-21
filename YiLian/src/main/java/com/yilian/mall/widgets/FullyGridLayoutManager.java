package com.yilian.mall.widgets;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Ray_L_Pain on 2017/8/18 0018.
 */

public class FullyGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = true;

    public FullyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public FullyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
