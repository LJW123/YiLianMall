package com.yilian.mall.suning.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.suning.SnGoodsBrandSelectedEntity;

import java.util.List;

/**
 * 苏宁首页品牌精选item中的列表适配器
 *
 * @author Created by zhaiyaohua on 2018/5/24.
 */

public class SnBrandSelectedGoodsInfoAdapter extends BaseQuickAdapter<SnGoodsBrandSelectedEntity.Data.Goods, BaseViewHolder> {
    public SnBrandSelectedGoodsInfoAdapter(List<SnGoodsBrandSelectedEntity.Data.Goods> dataList) {
        super(R.layout.sn_item_brand_selected_info, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnGoodsBrandSelectedEntity.Data.Goods item) {
        GlideUtil.showImage(mContext, item.image, helper.getView(R.id.sn_iv_goods));
        helper.setText(R.id.tv_has_rate, "预计 " + item.returnBean);
        helper.setText(R.id.sn_goods_des, item.name);
        helper.setText(R.id.sn_goods_price, "¥ " + item.snPrice + "");
    }
}
