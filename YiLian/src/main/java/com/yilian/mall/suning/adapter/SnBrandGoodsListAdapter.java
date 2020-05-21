package com.yilian.mall.suning.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.suning.SnGoodsAbstractInfoEntity;

/**
 * 苏宁品牌商品列表适配器
 *
 * @author Created by Zg on 2018/7/19.
 */

public class SnBrandGoodsListAdapter extends BaseQuickAdapter<SnGoodsAbstractInfoEntity, BaseViewHolder> {
    public SnBrandGoodsListAdapter() {
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
