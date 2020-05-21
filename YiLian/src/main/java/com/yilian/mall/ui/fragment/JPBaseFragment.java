package com.yilian.mall.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

/**
 * 新版商城 首页的基础Fragment
 */
public abstract class JPBaseFragment extends BaseFragment {

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


    @Override
    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    protected void loadData() {

    }
}
