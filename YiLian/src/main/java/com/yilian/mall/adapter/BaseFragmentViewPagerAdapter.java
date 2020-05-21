package com.yilian.mall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yilian.mall.ui.fragment.BaseFragment;

import java.util.ArrayList;

public class BaseFragmentViewPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager fragmentManager;
    private final ArrayList<BaseFragment> fragments;
    private final String[] titles;

    public BaseFragmentViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments, String[] title) {
        super(fm);
        this.fragmentManager = fm;
        this.fragments = fragments;
        this.titles = title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
