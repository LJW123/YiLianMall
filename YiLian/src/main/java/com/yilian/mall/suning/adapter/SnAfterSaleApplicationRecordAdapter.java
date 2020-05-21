package com.yilian.mall.suning.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.suning.SnAfterSaleApplicationRecordListEntity;


public class SnAfterSaleApplicationRecordAdapter extends BaseQuickAdapter<SnAfterSaleApplicationRecordListEntity.DataBean, BaseViewHolder> {


    public SnAfterSaleApplicationRecordAdapter() {
        super(R.layout.sn_item_after_sale_application_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnAfterSaleApplicationRecordListEntity.DataBean item) {
        helper.setText(R.id.tv_order_number,item.getOrderId());
        helper.setText(R.id.tv_order_time, DateUtils.formatDate(item.getApplyTime()*1000));
        GlideUtil.showImageWithSuffix(mContext, item.getSkuPic(), helper.getView(R.id.iv_goods_img));
        helper.setText(R.id.tv_goods_name, item.getSkuName());

        if(item.getIsRefund().equals("1")){
            helper.setText(R.id.tv_service_status, "已退款");
        }else {
            if(item.getApplyStatus().equals("1")){
                helper.setText(R.id.tv_service_status, "已通过");
            }else {
                helper.setText(R.id.tv_service_status, "审核失败");
            }
        }



    }

}
