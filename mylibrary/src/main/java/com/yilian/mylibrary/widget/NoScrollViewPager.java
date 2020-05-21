package com.yilian.mylibrary.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by  on 2017/8/3 0003.
 */

public class NoScrollViewPager extends ViewPager{
    public boolean isCanScroll=true;
    public NoScrollViewPager(Context context) {
        this(context,null);
    }
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean isCanScroll){
        this.isCanScroll=isCanScroll;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!isCanScroll){
            return false;
        }
        return super.onTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(!isCanScroll){
            return false;
        }
        return super.onInterceptHoverEvent(event);
    }

}
