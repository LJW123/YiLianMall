package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;


/**
 * @author zyh
 * @date 2017/5/11
 */

public class FlagshipAdapter2 extends BaseQuickAdapter<JPGoodsEntity, com.chad.library.adapter.base.BaseViewHolder> {
    /**
     * 显示平台加赠益豆
     */
    private Context context;

    public FlagshipAdapter2(Context context, @Nullable List<JPGoodsEntity> data) {
        super(R.layout.item_flagship_goods, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, JPGoodsEntity item) {
        FrameLayout flSubsidy = helper.getView(R.id.fl_subsidy);
        TextView tvPlateformGiveHashrate = helper.getView(R.id.tv_platform_give_hash_rate);
        ImageView ivGoods = (ImageView) helper.getView(R.id.iv_goods1);
        ImageView iv_sold_out = (ImageView) helper.getView(R.id.iv_sold_out);
        TextView tvName = (TextView) helper.getView(R.id.tv_goods_name);
        TextView tvReal = (TextView) helper.getView(R.id.tv_cost_price);
        TextView tvMark = (TextView) helper.getView(R.id.tv_market_price);
        TextView tvDiscount = (TextView) helper.getView(R.id.tv_discount);
        TextView tvSoldNumber = (TextView) helper.getView(R.id.tv_sold_number);
        TextView tvTag = (TextView) helper.getView(R.id.tv_tag);
        TextView tvPrice = (TextView) helper.getView(R.id.tv_price);
        TextView tvReturnBeans = helper.getView(R.id.tv_goods_beans);


        JPGoodsEntity jpGoodsEntity = item;
        //设置平台加赠益豆
        if (null == jpGoodsEntity.subsidy || Double.parseDouble(jpGoodsEntity.subsidy) <= 0) {
            flSubsidy.setVisibility(View.GONE);
        } else {
            flSubsidy.setVisibility(View.VISIBLE);
            tvPlateformGiveHashrate.setText("平台加赠" + com.yilian.mylibrary.MoneyUtil.getLeXiangBiNoZero(jpGoodsEntity.subsidy));
        }
        //买商品赠送益豆
        tvReturnBeans.setText("送" + com.yilian.mylibrary.MoneyUtil.getLeXiangBiNoZero(jpGoodsEntity.returnBean));
        tvReturnBeans.setVisibility(View.VISIBLE);


        GlideUtil.showImageNoSuffix(context, jpGoodsEntity.JPImageUrl, ivGoods);
        tvName.setText(jpGoodsEntity.JPGoodsName);
        tvPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(jpGoodsEntity.JPGoodsCost)));
        tvMark.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(jpGoodsEntity.JPGoodsPrice)));
        tvMark.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvDiscount.setVisibility(View.GONE);
        tvSoldNumber.setText("已售:" + jpGoodsEntity.JPSellCount);
        boolean flag = jpGoodsEntity.JPTagsName == null || "".equals(jpGoodsEntity.JPTagsName);
        if (flag) {
            tvTag.setVisibility(View.GONE);
        } else {
            tvTag.setText(jpGoodsEntity.JPTagsName);
        }

        if ("0".equals(jpGoodsEntity.hasGoods)) {
            iv_sold_out.setVisibility(View.VISIBLE);
        } else {
            iv_sold_out.setVisibility(View.GONE);
        }

    }
}
