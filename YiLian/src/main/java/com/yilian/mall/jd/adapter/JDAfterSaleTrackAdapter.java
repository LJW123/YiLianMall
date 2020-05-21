package com.yilian.mall.jd.adapter;


import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplyForDataEntity;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleDetialEntity;
import com.yilian.networkingmodule.entity.jd.JDOrderLogisticsEntity;


public class JDAfterSaleTrackAdapter extends BaseQuickAdapter<JDAfterSaleDetialEntity.serviceTrackInfoDTOs, BaseViewHolder> {

    private int listSize;

    public JDAfterSaleTrackAdapter(int listSize) {
        super(R.layout.jd_item_after_sale_track);
        this.listSize = listSize;
    }

    @Override
    protected void convert(BaseViewHolder helper, JDAfterSaleDetialEntity.serviceTrackInfoDTOs item) {
        //处理不同条目展示不同效果
        helper.getView(R.id.rl_end).setVisibility(View.GONE);
        helper.getView(R.id.rl_middle).setVisibility(View.GONE);
        helper.getView(R.id.rl_head).setVisibility(View.GONE);
        if (listSize > 1 && helper.getLayoutPosition() == 0) {
            helper.getView(R.id.rl_end).setVisibility(View.VISIBLE);
        } else if (helper.getLayoutPosition() == listSize - 1) {
            helper.getView(R.id.rl_head).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.rl_middle).setVisibility(View.VISIBLE);
        }
        //绑定数据
        helper.setText(R.id.tv_content, item.getContext());
        helper.setText(R.id.tv_handler, item.getCreateName());
        helper.setText(R.id.tv_time, item.getCreateDate());
    }


}
