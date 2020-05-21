package com.yilian.mall.suning.adapter;


import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.suning.SnOrderDetailEntity;


public class SnOrderDetailsGoodsListAdapter extends BaseQuickAdapter<SnOrderDetailEntity.GoodsList, BaseViewHolder> {
    //结算状态 0未结算  1已结算
    private int settleStatus = 0;

    public SnOrderDetailsGoodsListAdapter() {
        super(R.layout.sn_item_order_details_goods_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnOrderDetailEntity.GoodsList item) {
        GlideUtil.showImageWithSuffix(mContext, item.getSkuPic(), helper.getView(R.id.iv_goods_img));
        helper.setText(R.id.tv_goods_name, item.getSkuName());
        helper.setText(R.id.tv_goods_price, "¥ " + item.getSnPrice());
        helper.setText(R.id.tv_goods_num, item.getSkuNum());
        helper.setText(R.id.tv_has_rate, "送 "+item.getReturnBean());

        helper.addOnClickListener(R.id.ll_goods_info);
        initMenu(helper);
        //根据订单状态 展示不同菜单按钮
        if (item.getOrderStatus() == 4) {
            //订单状态为完成
            helper.getView(R.id.ll_menu).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_logistics).setVisibility(View.VISIBLE);
            if (settleStatus == 0 && item.getAfterSaleStatus() == 0) {
                //settleStatus  结算状态 0未结算  1已结算
                //afterSaleStatus 申请售后状态 0未申请或者审核取消/拒绝(可申请) 1申请中(不可申请)
                helper.getView(R.id.tv_apply_after_sales).setVisibility(View.VISIBLE);
            }
        } else if (item.getOrderStatus() == 3) {
            //订单状态为待收获
            helper.getView(R.id.ll_menu).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_affirm).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_logistics).setVisibility(View.VISIBLE);
        }

    }

    /**
     * 初始化按钮
     */
    public void initMenu(BaseViewHolder helper) {
        helper.getView(R.id.ll_menu).setVisibility(View.GONE);
        helper.getView(R.id.tv_affirm).setVisibility(View.GONE);
        helper.addOnClickListener(R.id.tv_affirm);
        helper.getView(R.id.tv_logistics).setVisibility(View.GONE);
        helper.addOnClickListener(R.id.tv_logistics);
        helper.getView(R.id.tv_apply_after_sales).setVisibility(View.GONE);
        helper.addOnClickListener(R.id.tv_apply_after_sales);


    }

    /**
     * @param settleStatus 结算状态 0未结算  1已结算
     */
    public void setSettleStatus(int settleStatus) {
        this.settleStatus = settleStatus;
    }

}
