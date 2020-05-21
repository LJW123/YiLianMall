package com.yilian.mall.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author zhaiyoahua
 */
public class OnlineMallPageViewPagerAdapter extends PagerAdapter {
    private List<RecyclerView> mViewList;

    public OnlineMallPageViewPagerAdapter(List<RecyclerView> mViewList) {
        this.mViewList = mViewList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {

        ((ViewPager)container).removeView(mViewList.get(position));
    }

    @Override
    public RecyclerView instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return (mViewList.get(position));
    }

    @Override
    public int getCount() {
        if (mViewList == null) {
            return 0;
        }
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}