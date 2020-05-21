package com.yilian.mall.shoppingcard.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.shoppingcard.activity.CardCommodityDetailActivity;
import com.yilian.mall.ui.JPNewCommDetailActivity;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.widgets.CustListView;
import com.yilian.mall.widgets.CustWebView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.StatusBarUtils;

/**
 * @auther 马铁超 on 2018/11/22 10:16
 * 购物卡详情 下拉页面
 */

public class CardCommodityButtomPageFragment extends BaseFragment{
    private Context context;
    private CardCommodityDetailActivity activity;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_commdetail_bottom, null);
        context = getContext();
        activity = (CardCommodityDetailActivity) getActivity();
        return rootView;
    }

    @Override
    protected void loadData() {

    }

    public void initView(int which) {

    }

}
