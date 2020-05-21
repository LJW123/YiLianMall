package com.yilian.mall.suning.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.suning.SnGoodsClassifyEntity;

/**
 * 苏宁首页商品二级分类适配器
 * 按照品牌分类
 *
 * @author Created by zhaiyaohua on 2018/5/23.
 */

public class SnGoodsSecondClassifyAdapter extends BaseQuickAdapter<SnGoodsClassifyEntity.Data.Content, BaseViewHolder> {
    public SnGoodsSecondClassifyAdapter() {
        super(R.layout.item_online_goods_sort);

    }

    @Override
    protected void convert(BaseViewHolder helper, SnGoodsClassifyEntity.Data.Content item) {
        helper.setText(R.id.tv_brand_name, item.title);
        GlideUtil.showImage(mContext, item.img, helper.getView(R.id.iv_goods_icon));
    }
}
