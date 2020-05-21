package com.yilian.mall.widgets;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by  on 2016/10/19 0019.
 * 处理Viewpager嵌套ViewPager时 滑动时间冲突的内部ViewPager
 */

public class InnerViewPager extends ViewPager {
    /**
     * 触摸时按下的点
     **/
    PointF downP = new PointF();
    /**
     * 触摸时当前的点
     **/
    PointF curP = new PointF();
    OnSingleTouchListener onSingleTouchListener;
    private Context context;

    public InnerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public InnerViewPager(Context context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        //每次进行onTouch事件都记录当前的按下的坐标
        if (getChildCount() <= 1) {
            return super.onTouchEvent(arg0);
        }
        curP.x = arg0.getX();
        curP.y = arg0.getY();

        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {

            //记录按下时候的坐标
            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            downP.x = arg0.getX();
            downP.y = arg0.getY();
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (arg0.getAction() == MotionEvent.ACTION_UP || arg0.getAction() == MotionEvent.ACTION_CANCEL) {
            //在up时判断是否按下和松手的坐标为一个点
            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
            getParent().requestDisallowInterceptTouchEvent(false);
            if (downP.x == curP.x && downP.y == curP.y) {

//                Toast.makeText(mContext, "Test", Toast.LENGTH_SHORT).show();
                return true;
            } else {
//                Toast.makeText(mContext, "Test2", Toast.LENGTH_SHORT).show();
            }
        }
        super.onTouchEvent(arg0); //注意这句不能 return super.onTouchEvent(arg0); 否则触发parent滑动
        return true;
    }

    public void onSingleTouch() {
        if (onSingleTouchListener != null) {

            onSingleTouchListener.onSingleTouch();
        }
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }


}
