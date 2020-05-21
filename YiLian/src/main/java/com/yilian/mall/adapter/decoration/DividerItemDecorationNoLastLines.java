package com.yilian.mall.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;


public class DividerItemDecorationNoLastLines extends DividerItemDecoration {

    public DividerItemDecorationNoLastLines(Context context, int orientation) {
        super(context, orientation);
    }

    public DividerItemDecorationNoLastLines(Context context, int orientation, int dividerHeight, int dividerColor) {
        super(context, orientation, dividerHeight, dividerColor);
    }

    @Override
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount - 1; i++) {

            final View child = parent.getChildAt(i);
            RecyclerView v = new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            Logger.i("drawVertical  left   " + left + "  top  " + top + "  right  " + right + "  bottom  " + bottom + "  mDividerHeight  " + mDividerHeight);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
