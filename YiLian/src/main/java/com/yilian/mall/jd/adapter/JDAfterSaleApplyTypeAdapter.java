package com.yilian.mall.jd.adapter;


import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplyForDataEntity;


public class JDAfterSaleApplyTypeAdapter extends BaseQuickAdapter<JDAfterSaleApplyForDataEntity.TypeBean, BaseViewHolder> {


    public JDAfterSaleApplyTypeAdapter() {
        super(R.layout.jd_item_after_sale_apply_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, JDAfterSaleApplyForDataEntity.TypeBean item) {
        TextView tv_name = helper.getView(R.id.tv_name);
        tv_name.setText(item.getName());
        if (item.isSelected()) {
            tv_name.setTextColor(Color.parseColor("#F10215"));
            tv_name.setBackgroundResource(R.drawable.jd_order_bt_bg_red);
        } else {
            tv_name.setTextColor(Color.parseColor("#222222"));
            tv_name.setBackgroundResource(R.drawable.jd_order_bt_bg_black);
        }
    }


}
