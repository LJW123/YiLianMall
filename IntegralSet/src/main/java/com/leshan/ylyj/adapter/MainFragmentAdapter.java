package com.leshan.ylyj.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.leshan.ylyj.view.activity.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;

    public MainFragmentAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public BaseFragment getItem(int fragment) {
        return fragments.get(fragment);
    }

    public int getCount() {
        return fragments.size();
    }
}
