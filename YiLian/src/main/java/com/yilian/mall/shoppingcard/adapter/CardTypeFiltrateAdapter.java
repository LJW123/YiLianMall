package com.yilian.mall.shoppingcard.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.shoppingcard.CardTypeFiltrateEntity;

/**
 * 作者：马铁超 on 2018/11/16 17:25
 * 购物卡类型筛选 适配器
 */

public class CardTypeFiltrateAdapter extends BaseQuickAdapter<CardTypeFiltrateEntity.DataBean, BaseViewHolder> {
    public CardTypeFiltrateAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardTypeFiltrateEntity.DataBean item) {
        helper.setText(R.id.tv_filtrate_item,item.typeValue);
        if(item.isCheck){
            helper.setBackgroundRes(R.id.tv_filtrate_item,R.drawable.border_red_content_white_radiu18);
            helper.setTextColor(R.id.tv_filtrate_item, Color.parseColor("#fff22f24"));
        }else {
            helper.setBackgroundRes(R.id.tv_filtrate_item,R.drawable.border_gray_content_appbg_radiu18);
            helper.setTextColor(R.id.tv_filtrate_item, Color.parseColor("#ff666666"));
        }
    }
}
