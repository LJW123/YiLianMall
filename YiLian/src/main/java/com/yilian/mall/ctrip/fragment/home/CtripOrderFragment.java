package com.yilian.mall.ctrip.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.fragment.order.CtripOrderCommonFragment;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.OnTabSelectListener;
import com.yilian.mylibrary.widget.SlidingTabLayout;

import java.util.ArrayList;


/**
 * 携程 订单
 *
 * @author Created by Zg on 2018/8/15.
 */
public class CtripOrderFragment extends JPBaseFragment implements OnTabSelectListener, View.OnClickListener {
    public static final String[] mTitles = {
            "全部", "未提交", "已确认", "已消费", "已取消"
    };
    private SlidingTabLayout tabLayout_2;
    private ViewPager vp;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.ctrip_fragment_order, null);
        }
        initView();
        initData();
        initListener();
        return rootView;
    }

    private void initView() {
        /**  更据 mTitles[] 进行赋值             */
        mFragments.add(CtripOrderCommonFragment.getInstance(CtripOrderCommonFragment.OrderType_all));
        mFragments.add(CtripOrderCommonFragment.getInstance(CtripOrderCommonFragment.OrderType_submit));
        mFragments.add(CtripOrderCommonFragment.getInstance(CtripOrderCommonFragment.OrderType_affirm));
        mFragments.add(CtripOrderCommonFragment.getInstance(CtripOrderCommonFragment.OrderType_expense));
        mFragments.add(CtripOrderCommonFragment.getInstance(CtripOrderCommonFragment.OrderType_cancel));

        vp = rootView.findViewById(R.id.vp);
        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        vp.setAdapter(mAdapter);
        /**自定义部分属性*/
        tabLayout_2 = rootView.findViewById(R.id.tl_2);
        tabLayout_2.setViewPager(vp);
        tabLayout_2.setOnTabSelectListener(this);
    }


    private void initData() {

    }


    private void initListener() {


    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.v3Back:
//                finish();
//                break;
//            default:
//                break;
//        }
    }

    public void setCurrentTab(int p){
        vp.setCurrentItem(p);
    }

    @Override
    public void onTabSelect(int position) {
//        Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
//        Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
