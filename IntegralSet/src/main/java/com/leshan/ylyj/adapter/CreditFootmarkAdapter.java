package com.leshan.ylyj.adapter;


import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.view.activity.creditmodel.CreditFootmarkActivity;
import com.yilian.mylibrary.GlideUtil;

import rxfamily.entity.CreditFootmarkEntity;

public class CreditFootmarkAdapter extends BaseQuickAdapter<CreditFootmarkEntity.ContentBean, BaseViewHolder> {

    String color = CreditFootmarkActivity.tone1;

    public CreditFootmarkAdapter() {
        super(R.layout.item_credit_footmark);

    }

    @Override
    protected void convert(BaseViewHolder helper, final CreditFootmarkEntity.ContentBean item) {

        if (helper.getLayoutPosition() == 1) {
            helper.getView(R.id.first_tab).setVisibility(View.VISIBLE);
            helper.getView(R.id.common_tab).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.first_tab).setVisibility(View.GONE);
            helper.getView(R.id.common_tab).setVisibility(View.VISIBLE);
        }


        CardView card_view = helper.getView(R.id.card_view);
        card_view.setCardBackgroundColor(Color.parseColor(color));
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.intro_tv, item.getDesc());
        ImageView img_iv = helper.getView(R.id.img_iv);
        GlideUtil.showImageWithSuffix(mContext, item.getImg(), img_iv);
        helper.setText(R.id.date_tv, item.getCreateTime());
    }


    public void setColor(String color){
        this.color = color;
    }
}
