package com.yilian.mall.jd.fragment.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.OnTabSelectListener;
import com.yilian.mylibrary.entity.TabEntity;

import java.util.ArrayList;

/**
 * 京东售后
 *
 * @author Created by Zg on 2018/5/25.
 */
@SuppressLint("ValidFragment")
public class JDAfterSaleFragment extends JPBaseFragment implements OnTabSelectListener {

    public static final String[] mTitles = {
            "售后申请", "申请记录"
    };
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private CommonTabLayout mTabLayout_3;

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.jd_fragment_after_sale, null);
        }
        initView();
        initData();
        initListener();
        return rootView;
    }

    public void initView() {
        mFragments.add(new JDAfterSaleApplyForFragment());
        mFragments.add(new JDAfterSaleApplicationRecordFragment());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
        }
        /**自定义部分属性*/
        mTabLayout_3 = rootView.findViewById(R.id.tl_3);
        mTabLayout_3.setTabData(mTabEntities, getActivity(), R.id.fl_change, mFragments);

    }


    public void initData() {

    }

    public void initListener() {


    }

    @Override
    public void onTabSelect(int position) {
//        Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
//        Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
    }




}