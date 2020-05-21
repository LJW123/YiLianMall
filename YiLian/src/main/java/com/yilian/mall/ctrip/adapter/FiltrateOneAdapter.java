package com.yilian.mall.ctrip.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.base.BaseInfoAdapter;
import com.yilian.mall.ctrip.bean.FiltrateOneContentBean;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/8/31 17:14
 * 智能排序列表 适配器
 */

public class FiltrateOneAdapter extends BaseQuickAdapter<SearchFilterBean.ComSortBean,BaseViewHolder> {

    public FiltrateOneAdapter() {
        super(R.layout.item_filtrate_one);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchFilterBean.ComSortBean comSortBean) {
        helper.setText(R.id.tv_price_sorted_name,comSortBean.getTitle());
        ImageView iv_price_sorted_ischeck = helper.getView(R.id.iv_price_sorted_ischeck);
        TextView tv_price_sorted_name = helper.getView(R.id.tv_price_sorted_name);
        if (comSortBean.isCheck() == true) {
            iv_price_sorted_ischeck.setVisibility(View.VISIBLE);
            tv_price_sorted_name.setTextColor(Color.parseColor("#4881d9"));
        } else {
            iv_price_sorted_ischeck.setVisibility(View.GONE);
            tv_price_sorted_name.setTextColor(Color.parseColor("#1d201f"));
        }
    }
}
