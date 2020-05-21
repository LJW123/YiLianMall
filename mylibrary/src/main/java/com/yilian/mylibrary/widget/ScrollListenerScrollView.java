package com.yilian.mylibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by  on 2017/4/18 0018.
 */

public class ScrollListenerScrollView extends ScrollView {
    private OnScrollChanged onScrollChanged;

    public ScrollListenerScrollView(Context context) {
        super(context);
    }

    public ScrollListenerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListenerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(onScrollChanged!=null){
            onScrollChanged.onScroll(l,t,oldl,oldt);
        }
    }
    public void setOnScrollChanged(OnScrollChanged onScrollChanged){
        this.onScrollChanged = onScrollChanged;
    }
    public interface OnScrollChanged{
        void onScroll(int l,int t,int oldl,int oldt);
    }
}
