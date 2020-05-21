package com.yilian.mall.jd.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.jd.JDGoodsAbstractInfoEntity;

/**
 * 京东品牌商品列表适配器
 *
 * @author Created by zhaiyaohua on 2018/5/25.
 */

public class JdBrandGoodsListAdapter extends BaseQuickAdapter<JDGoodsAbstractInfoEntity, BaseViewHolder> {
    public JdBrandGoodsListAdapter() {
        super(R.layout.jd_item_home_page);
    }

    @Override
    protected void convert(BaseViewHolder helper, JDGoodsAbstractInfoEntity data) {
        ImageView ivTop = helper.getView(R.id.iv_goods);
        GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N1(data.imagePath), ivTop);
        helper.setText(R.id.tv_goods_des, data.name);
        helper.setText(R.id.tv_goods_price, "¥" + data.jdPrice + "");
        helper.setText(R.id.tv_sale_num, "已售：" + data.saleCount);
        helper.setText(R.id.tv_has_rate, "预计 " + data.returnBean);
    }
}
