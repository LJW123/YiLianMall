package com.yilian.mall.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.FavoriteEntity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by liuyuqi on 2017/8/20 0020.
 */

public class FavoriteGoodsRecycleAdapter extends BaseQuickAdapter<FavoriteEntity.ListBean, BaseViewHolder> {

    public final Map<Integer, Boolean> flags;

    public FavoriteGoodsRecycleAdapter(int item_favorite_goods2, ArrayList<FavoriteEntity.ListBean> goodsList, Map<Integer, Boolean> flags) {
        super(item_favorite_goods2, goodsList);
        this.flags = flags;
    }

    @Override
    protected void convert(BaseViewHolder helper, FavoriteEntity.ListBean item) {

        ImageView ivIcon = helper.getView(R.id.iv_icon);
        ImageView ivYhs = helper.getView(R.id.iv_yhs_icon);
        CheckBox chCheck = helper.getView(R.id.cb_select);
        GlideUtil.showImageWithSuffix(mContext, item.collectIcon, ivIcon);
        helper.setText(R.id.tv_name, item.collectName);
        helper.addOnClickListener(R.id.cb_select);//绑定控件的点击事件
        if ("0".equals(item.status)) {
            helper.getView(R.id.iv_non).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_non).setVisibility(View.GONE);
        }
        if (item.isShowCheck) {
            chCheck.setVisibility(View.VISIBLE);
            if (item.isCheck) {
                chCheck.setChecked(true);
            } else {
                chCheck.setChecked(false);
            }
        } else {
            chCheck.setVisibility(View.GONE);
        }
        TextView tvMarketPrice = helper.getView(R.id.tv_market_price);
        TextView tvCostIntegral = helper.getView(R.id.tv_integral_cost);
        if (!TextUtils.isEmpty(item.type)) {
            switch (item.type) {
                case "0"://普通商品
                    ivYhs.setVisibility(View.GONE);
                    helper.getView(R.id.tv_cost_price).setVisibility(View.VISIBLE);
                    tvMarketPrice.setVisibility(View.VISIBLE);
                    tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    helper.getView(R.id.tv_goods_integral).setVisibility(View.GONE);
                    tvCostIntegral.setVisibility(View.INVISIBLE);
                    helper.setText(R.id.tv_cost_price, MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(item.collectCost)));
                    helper.setText(R.id.tv_market_price, MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(item.collectPrice)));
                    helper.getView(R.id.tv_use_quan).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_use_quan, mContext.getString(R.string.back_score) + MoneyUtil.getLeXiangBi(item.returnIntegral));
                    helper.getView(R.id.tv_use_bean).setVisibility(View.GONE);
                    helper.setText(R.id.tv_use_bean, "");
                    break;
                case "1"://易划算
                    ivYhs.setVisibility(View.VISIBLE);
                    ivYhs.setImageResource(R.mipmap.icon_yhs);
                    helper.getView(R.id.tv_cost_price).setVisibility(View.GONE);
                    helper.getView(R.id.tv_goods_integral).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_use_quan).setVisibility(View.GONE);
                    tvCostIntegral.setVisibility(View.INVISIBLE);
                    tvMarketPrice.setVisibility(View.GONE);
                    helper.setText(R.id.tv_goods_integral, MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(item.goodsIntegral)));
                    helper.getView(R.id.tv_use_bean).setVisibility(View.GONE);
                    helper.setText(R.id.tv_use_bean, "");   //零购券
                    break;
                case "2"://奖券购
                    ivYhs.setVisibility(View.VISIBLE);
                    ivYhs.setImageResource(R.mipmap.icon_jfg);
                    helper.getView(R.id.tv_use_quan).setVisibility(View.GONE);
                    tvMarketPrice.setVisibility(View.GONE);
                    tvCostIntegral.setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_goods_integral).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_cost_price, MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(item.collectCost)));
                    float hasIntegral = Float.parseFloat(item.collectPrice) - Float.parseFloat(item.collectCost);
                    helper.setText(R.id.tv_goods_integral, MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(hasIntegral)));
                    tvCostIntegral.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
                    helper.setText(R.id.tv_integral_cost, MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(item.collectPrice)));
                    helper.getView(R.id.tv_use_bean).setVisibility(View.GONE);
                    helper.setText(R.id.tv_use_bean, "");
                    break;
                case "3":
                case GoodsType.CAL_POWER:
                    ivYhs.setVisibility(View.GONE);
                    helper.getView(R.id.tv_cost_price).setVisibility(View.VISIBLE);
                    if ("1".equals(item.isFan)) {
                        tvMarketPrice.setVisibility(View.GONE);
                    } else {
                        tvMarketPrice.setVisibility(View.VISIBLE);
                        tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    }
                    helper.getView(R.id.tv_goods_integral).setVisibility(View.GONE);
                    tvCostIntegral.setVisibility(View.INVISIBLE);
                    helper.setText(R.id.tv_cost_price, MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(item.collectCost)));
                    helper.setText(R.id.tv_market_price, MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(item.collectPrice)));
                    helper.getView(R.id.tv_use_quan).setVisibility(View.GONE);
                    helper.getView(R.id.tv_use_bean).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_use_bean, "送  " + MoneyUtil.getLeXiangBi(item.returnBean));
                    break;
                default:
                    break;
            }
        }


    }
}
