package com.yilian.mall.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.JPGoodsEntity;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/9/1 0001.
 */

public class MallListRecycleAdapter extends BaseQuickAdapter<JPGoodsEntity, com.chad.library.adapter.base.BaseViewHolder> {
    private final String type;

    public MallListRecycleAdapter(int item_jp_good, ArrayList<JPGoodsEntity> list, String category) {
        super(item_jp_good, list);
        this.type = category;
    }

    @Override
    protected void convert(BaseViewHolder helper, JPGoodsEntity entity) {
        helper.addOnClickListener(R.id.item_layout);
        ImageView ivLeft = helper.getView(R.id.iv_left);
        TextView tvCost = helper.getView(R.id.tv_cost_jfg);
        helper.getView(R.id.item_mall).setVisibility(View.GONE);
        helper.getView(R.id.iv_left).setVisibility(View.VISIBLE);
        ImageView ivIcon = helper.getView(R.id.iv_goods1);
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlSetSuffix(entity.JPImageUrl, ScreenUtils.getScreenWidth(mContext) + "", ScreenUtils.getScreenWidth(mContext) + ""), ivIcon);
        helper.setText(R.id.tv_goods_name, entity.JPGoodsName);
        if ("0".equals(entity.hasGoods)) {
            helper.getView(R.id.iv_sold_out).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_sold_out).setVisibility(View.GONE);
        }
        int jifen = Integer.parseInt(entity.JPGoodsPrice) - Integer.parseInt(entity.JPGoodsCost);
        Logger.i("adpater用到的type：" + type);
        switch (type) {
            case "1":
                helper.getView(R.id.item_yhs).setVisibility(View.VISIBLE);
                helper.getView(R.id.item_jfg).setVisibility(View.GONE);
                helper.setText(R.id.tv_goods_name_yhs, entity.JPGoodsName);
                ivLeft.setImageResource(R.mipmap.icon_yhs);
                helper.setText(R.id.tv_price_yhs, MoneyUtil.getLeXiangBi(entity.goodsIntegral));
                helper.setText(R.id.tv_sold_number_yhs, "已售：" + entity.JPSellCount);
                break;
            case "2":
                helper.getView(R.id.item_yhs).setVisibility(View.GONE);
                helper.getView(R.id.item_jfg).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_goods_name_jfg, entity.JPGoodsName);
                ivLeft.setImageResource(R.mipmap.icon_yhs);
                helper.setText(R.id.tv_price_jfg, MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(entity.JPGoodsCost)));
                helper.setText(R.id.tv_jifen_jfg, "+" + MoneyUtil.getLeXiangBi(jifen));
                helper.setText(R.id.tv_cost_jfg, "¥" + MoneyUtil.getLeXiangBi(entity.JPGoodsPrice));
                tvCost.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                helper.setText(R.id.tv_sold_number_jfg, "已售：" + entity.JPSellCount);
                break;
        }

    }
}
