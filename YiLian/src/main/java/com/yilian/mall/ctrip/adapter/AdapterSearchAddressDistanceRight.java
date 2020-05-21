package com.yilian.mall.ctrip.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.base.BaseInfoAdapter;
import com.yilian.mall.ctrip.mvp.BaseView;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/8/30 11:06
 * 酒店查询_距离筛选
 */

public class AdapterSearchAddressDistanceRight extends BaseQuickAdapter<SearchFilterBean.DistSortBean.SortsBean, BaseViewHolder> {

    public AdapterSearchAddressDistanceRight() {
        super(R.layout.item_filtrate_distance_right);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchFilterBean.DistSortBean.SortsBean sortsBean) {
        ImageView iv_check = helper.getView(R.id.iv_check);
        TextView tv_filtrate_right_distance = helper.getView(R.id.tv_filtrate_right_distance);
        if (sortsBean.isCheck() == true) {
            iv_check.setBackgroundResource(R.mipmap.iv_cb_checked);
        } else {
            iv_check.setBackgroundResource(R.mipmap.iv_cb_not_checked);
        }
        tv_filtrate_right_distance.setText(sortsBean.getTitle());
    }
}
