package com.yilian.mall.suning.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.suning.SnGoodsAbstractInfoEntity;

import java.util.List;

/**
 * 苏宁首页精选列表适配器
 *
 * @author Created by zhaiyaohua on 2018/5/23.
 */

public class SnHomeListAdapter extends BaseQuickAdapter<SnGoodsAbstractInfoEntity, BaseViewHolder> {


    public SnHomeListAdapter(List<SnGoodsAbstractInfoEntity> data) {
        super(R.layout.sn_item_home_page);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnGoodsAbstractInfoEntity item) {
        GlideUtil.showImage(mContext, item.image, helper.getView(R.id.iv_goods));
        helper.setText(R.id.tv_goods_des, item.name);
        helper.setText(R.id.tv_goods_price, item.snPrice + "");
        helper.setText(R.id.tv_sale_num, item.saleCount + "人购买");
        helper.setText(R.id.tv_has_rate, "预计 " + item.returnBean);
    }
}
