package com.yilian.mall.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.JPGoodsClassfiyEntity;

import java.util.List;

/**
 * 商品种类适配器
 *
 * @author Created by zhaiyaohua on 2018/5/11.
 */

public class OnlineGoodsTypeAdapter extends BaseQuickAdapter<JPGoodsClassfiyEntity.ListBean, com.chad.library.adapter.base.BaseViewHolder> {
    private int currentPosition = 0;

    public OnlineGoodsTypeAdapter(int layoutResId, @Nullable List<JPGoodsClassfiyEntity.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JPGoodsClassfiyEntity.ListBean item) {
        int pos = helper.getLayoutPosition();
        TextView tvGoodsType = helper.getView(R.id.tv_goods_type);
        tvGoodsType.setText(item.name);
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
