package com.yilian.mall.ctrip.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.bean.FiltrateDataBean;

/**
 * 作者：马铁超 on 2018/10/23 16:36
 */

public class AdapterFiltrateDataContent extends BaseQuickAdapter<FiltrateDataBean.FiltrateDataBeanContent, BaseViewHolder> {

    public AdapterFiltrateDataContent(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FiltrateDataBean.FiltrateDataBeanContent item) {
        helper.setText(R.id.tv_third_price, item.name);
        if (item.isCheck == true) {
            helper.getView(R.id.tv_third_price).setBackgroundColor(Color.parseColor("#E7F3FF"));
            helper.setTextColor(R.id.tv_third_price,Color.parseColor("#4289FF"));
        } else {
            helper.getView(R.id.tv_third_price).setBackgroundColor(Color.parseColor("#F5F5FA"));
            helper.setTextColor(R.id.tv_third_price,Color.parseColor("#333333"));
        }
        helper.addOnClickListener(R.id.tv_third_price);
    }

}
