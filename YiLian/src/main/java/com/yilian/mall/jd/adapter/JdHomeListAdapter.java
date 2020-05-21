package com.yilian.mall.jd.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.jd.JDGoodsAbstractInfoEntity;

import java.util.List;

/**
 * 首页为您推荐列表适配器
 *
 * @author Created by zhaiyaohua on 2018/5/23.
 */

public class JdHomeListAdapter extends BaseQuickAdapter<JDGoodsAbstractInfoEntity, BaseViewHolder> {


    public JdHomeListAdapter(List<JDGoodsAbstractInfoEntity> data) {
        super(R.layout.jd_item_home_page);
    }

    @Override
    protected void convert(BaseViewHolder helper, JDGoodsAbstractInfoEntity item) {
        ImageView ivTop = helper.getView(R.id.iv_goods);
        GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N1(item.imagePath), ivTop);
        helper.setText(R.id.tv_goods_des, item.name);
        helper.setText(R.id.tv_goods_price, "¥" + item.jdPrice + "");
        helper.setText(R.id.tv_sale_num, "已售：" + item.saleCount);
        helper.setText(R.id.tv_has_rate, "预计 " + item.returnBean);

    }
}
