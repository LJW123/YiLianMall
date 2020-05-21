package com.yilian.mall.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.JPGoodsClassfiyEntity;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

/**
 * 线上商城分类适配器
 * 按照品牌分类
 *
 * @author Created by zhaiyaohua on 2018/5/11.
 */

public class OnlineGoodsSortAdapter extends BaseQuickAdapter<JPGoodsClassfiyEntity.ListBean.InfoBean, com.chad.library.adapter.base.BaseViewHolder> {
    public OnlineGoodsSortAdapter(int layoutResId, @Nullable List<JPGoodsClassfiyEntity.ListBean.InfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JPGoodsClassfiyEntity.ListBean.InfoBean item) {
        helper.setText(R.id.tv_brand_name, item.name);
        GlideUtil.showImage(mContext, item.img, helper.getView(R.id.iv_goods_icon));
    }

}
