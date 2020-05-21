package com.yilian.mall.adapter;

import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.JPGoodsEntity;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.ArrayList;

/**
 * Created by  on 2017/7/29 0029.
 */

public class CategoryGoodsAdapter extends BaseQuickAdapter<JPGoodsEntity, BaseViewHolder> {

    private final RecyclerView recyclerView;

    public CategoryGoodsAdapter(@LayoutRes int layoutResId, @Nullable ArrayList<JPGoodsEntity> data, RecyclerView recyclerView) {
        super(layoutResId, data);
        this.recyclerView = recyclerView;
    }

    @Override
    protected void convert(BaseViewHolder helper, JPGoodsEntity item) {
        Logger.i("holder内存地址值：" + helper.hashCode());
        helper.setText(R.id.tv_goods_name, item.JPGoodsName);
        ImageView ivGood = helper.getView(R.id.iv_goods1);
        int imageWidth = ScreenUtils.getScreenWidth(mContext) / 2;
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlSetSuffix(item.JPImageUrl, String.valueOf(imageWidth / 2), String.valueOf(imageWidth / 2)), ivGood);
        if (recyclerView.getScrollState()== RecyclerView.SCROLL_STATE_IDLE) {
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imageWidth, imageWidth);
//        ivGood.setLayoutParams(params);
        helper.setText(R.id.tv_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBi(item.JPGoodsCost)));//售价
        TextView tvMarketPrice = helper.getView(R.id.tv_market_price);
        tvMarketPrice.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBi(item.JPGoodsPrice)));//市场价
        tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//设置中划线
        helper.setText(R.id.tv_discount, mContext.getString(R.string.back_score) + MoneyUtil.getLeXiangBiNoZero(item.returnIntegral));//返利
        helper.getView(R.id.tv_tag).setVisibility(View.GONE);
        helper.setText(R.id.tv_sold_number, "已售:" + item.JPSellCount);
        if (item.hasGoods.equals("0")) {
            helper.getView(R.id.iv_sold_out).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_sold_out).setVisibility(View.GONE);
        }
        switch (helper.getLayoutPosition() % 2) {
            case 0:
                helper.getView(R.id.view_line).setVisibility(View.GONE);
                break;
            case 1:
                helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
                break;
        }
    }
}
