package com.yilian.mall.suning.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.suning.SnGoodsClassifyEntity;

/**
 * 苏宁首页商品分类适配器
 * 按照品牌分类
 *
 * @author Created by zhaiyaohua on 2018/5/23.
 */

public class SnGoodsClassifyAdapter extends BaseQuickAdapter<SnGoodsClassifyEntity.Data, BaseViewHolder> {
    private int currentPosition = 0;

    public SnGoodsClassifyAdapter() {
        super(R.layout.item_online_goods_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnGoodsClassifyEntity.Data item) {
        int pos = helper.getLayoutPosition();
        TextView tvGoodsType = helper.getView(R.id.tv_goods_type);
        tvGoodsType.setText(item.title);
        if (pos == currentPosition) {
            tvGoodsType.setSelected(true);
        } else {
            tvGoodsType.setSelected(false);
        }
    }

    public void setNotisifySelectedPositon(int positon) {
        this.currentPosition = positon;
        notifyDataSetChanged();
    }

}
