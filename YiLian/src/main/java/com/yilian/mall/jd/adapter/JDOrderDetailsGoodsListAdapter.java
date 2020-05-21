package com.yilian.mall.jd.adapter;


import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.enums.JdOrderStatusType;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.jd.JDOrderDetailEntity;


public class JDOrderDetailsGoodsListAdapter extends BaseQuickAdapter<JDOrderDetailEntity.GoodsList, BaseViewHolder> {

    public int type = 0;

    public JDOrderDetailsGoodsListAdapter() {
        super(R.layout.jd_item_order_details_goods_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, JDOrderDetailEntity.GoodsList item) {
        GlideUtil.showImageWithSuffix(mContext, JDImageUtil.getJDImageUrl_N3(item.skuPic), helper.getView(R.id.iv_goods_img));
        helper.setText(R.id.tv_goods_name, item.skuName);
        helper.setText(R.id.tv_goods_price, String.valueOf(item.skuJdPrice));
        helper.setText(R.id.tv_goods_num, String.valueOf(item.skuNum));
        //订单状态为完成
        //申请售后状态 0未申请或者审核取消/拒绝(可申请) 1申请中(不可申请)
        if (type == JdOrderStatusType.completed && item.afterSaleStatus == 0) {
            helper.getView(R.id.tv_apply_after_sales).setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.tv_apply_after_sales);
        } else {
            helper.getView(R.id.tv_apply_after_sales).setVisibility(View.GONE);
        }

    }


}
