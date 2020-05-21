package com.yilian.mall.jd.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplicationRecordListEntity;


public class JDAfterSaleApplicationRecordAdapter extends BaseQuickAdapter<JDAfterSaleApplicationRecordListEntity.DataBean, BaseViewHolder> {


    public JDAfterSaleApplicationRecordAdapter() {
        super(R.layout.jd_item_after_sale_application_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, JDAfterSaleApplicationRecordListEntity.DataBean item) {
        helper.setText(R.id.tv_order_number, "服务单号：" + item.getAfsServiceId());
        if(item.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD){
            helper.getView(R.id.iv_order_card_tab).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.iv_order_card_tab).setVisibility(View.GONE);
        }

        GlideUtil.showImageWithSuffix(mContext, JDImageUtil.getJDImageUrl_N3(item.getSkuPic()), helper.getView(R.id.iv_goods_img));
        helper.setText(R.id.tv_goods_name, item.getSkuName());
        //售后类型
        if (item.getCustomerExpectName().equals("10")) {
            helper.setText(R.id.tv_order_type, "退货");
        } else if (item.getCustomerExpectName().equals("20")) {
            helper.setText(R.id.tv_order_type, "换货");
        } else if (item.getCustomerExpectName().equals("30")) {
            helper.setText(R.id.tv_order_type, "维修");
        } else {
            helper.setText(R.id.tv_order_type, "");
        }
        helper.setText(R.id.tv_state, item.getAfsServiceStepName());
        helper.setText(R.id.tv_desc, item.getDec());

    }

}
