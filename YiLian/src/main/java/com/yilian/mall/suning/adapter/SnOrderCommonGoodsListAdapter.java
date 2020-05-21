package com.yilian.mall.suning.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.suning.SnOrderListEntity;

import java.util.List;


public class SnOrderCommonGoodsListAdapter extends BaseQuickAdapter<SnOrderListEntity.GoodsList, BaseViewHolder> {

    private List<SnOrderListEntity.GoodsList> list;

    public SnOrderCommonGoodsListAdapter(List<SnOrderListEntity.GoodsList> list) {
        super(R.layout.sn_item_order_common_goods_list);
        this.list = list;
        setNewData(this.list);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnOrderListEntity.GoodsList item) {
        GlideUtil.showImageWithSuffix(mContext, item.getSkuPic(), helper.getView(R.id.iv_goods_img));
        helper.setText(R.id.iv_goods_name, item.getSkuName());
        helper.setText(R.id.tv_goods_price, "¥ " + item.getSnPrice());
        helper.setText(R.id.tv_has_rate, "送" + item.getReturnBean());
        helper.setText(R.id.tv_goods_num, "x" + item.getSkuNum());
    }

}
