package com.yilian.mall.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.networkingmodule.entity.DataBean;

import java.util.List;

import static com.yilian.mall.ui.JPNewCommDetailActivity.LE_DOU_GOODS_2;

/**
 * @author Created by  on 2018/2/12.
 */

public class LeDouListAdapter extends BaseQuickAdapter<DataBean, com.chad.library.adapter.base.BaseViewHolder> {
    public LeDouListAdapter(int layoutResId) {
        super(layoutResId);
    }

    public LeDouListAdapter(int layoutResId, @Nullable List<DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean item) {
        ImageView imageView = helper.getView(R.id.iv_goods);
        FrameLayout flSubsidy = helper.getView(R.id.fl_subsidy);
        TextView tvPlatformGiveHashRate = helper.getView(R.id.tv_platform_give_hash_rate);
        GlideUtil.showImage(mContext, item.imageUrl, imageView);
        helper.setText(R.id.tv_goods_des, item.goodsName);
        helper.setText(R.id.tv_goods_beans, "送 " + MoneyUtil.getLeXiangBiNoZero(item.returnBean));
        helper.setText(R.id.tv_goods_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(item.goodsCost)));
        TextView tvGoodsPrice = (TextView) helper.getView(R.id.tv_high_price);
        if (item.goodsType.equals(LE_DOU_GOODS_2)) {
            if (tvGoodsPrice != null) {
                tvGoodsPrice.setVisibility(View.INVISIBLE);
            }
        } else {
            if (tvGoodsPrice != null) {
                tvGoodsPrice.setVisibility(View.VISIBLE);
            }
        }
        if (tvGoodsPrice != null) {
            tvGoodsPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvGoodsPrice.setText(MoneyUtil.setSmall¥Money(MoneyUtil.getLeXiangBi(item.goodsPrice)));
        }
        helper.setText(R.id.tv_goods_sell_num, "已售" + item.sellCount);
        //设置平台加赠益豆
        if (null != flSubsidy) {
            if (null == item.subsidy || Double.parseDouble(item.subsidy) <= 0) {
                flSubsidy.setVisibility(View.GONE);
            } else {
                flSubsidy.setVisibility(View.VISIBLE);
                tvPlatformGiveHashRate.setText("平台加赠" + com.yilian.mylibrary.MoneyUtil.getLeXiangBiNoZero(item.subsidy));
            }
        }

    }
}
