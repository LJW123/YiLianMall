package com.yilian.mall.ctrip.adapter;
import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.bean.FiltrateListBean;

/**
 * 作者：马铁超 on 2018/10/22 17:29
 */

public class AdapterDetailHotelFiltrateCondition extends BaseQuickAdapter<FiltrateListBean.ListItemData,BaseViewHolder> {

    public AdapterDetailHotelFiltrateCondition(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FiltrateListBean.ListItemData item) {
        helper.setText(R.id.tv_item_filtrate,item.getTitle());
        if (item.isCheck() == true) {
            helper.setBackgroundRes(R.id.tv_item_filtrate,R.drawable.yuanjiao_blue_radiu10);
            helper.setTextColor(R.id.tv_item_filtrate, Color.parseColor("#FF4289FF"));
        } else {
            helper.setBackgroundRes(R.id.tv_item_filtrate,R.drawable.bg_gray_radius_90);
            helper.setTextColor(R.id.tv_item_filtrate, Color.parseColor("#333333"));
        }
    }
}
