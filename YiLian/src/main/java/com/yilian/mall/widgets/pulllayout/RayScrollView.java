package com.yilian.mall.widgets.pulllayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.yilian.mylibrary.DPXUnitUtil;

/**
 * Created by Ray_L_Pain on 2017/12/6 0006.
 */

public class RayScrollView extends ScrollView {
    private float downY;
    private float currentY;

    public RayScrollView(Context context) {
        super(context);
    }

    public RayScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RayScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //当竖直方向的距离超过20时，进行拦截，使得scrollview可以活动，且不影响其他情况下，scrollview的点击事件
                currentY = ev.getY();
                if (Math.abs(currentY - downY) >= DPXUnitUtil.dp2px(getContext(), 20)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
