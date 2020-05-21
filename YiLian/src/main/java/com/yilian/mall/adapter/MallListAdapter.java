package com.yilian.mall.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.ui.JPNewCommDetailActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.JPGoodsEntity;
import com.yilian.networkingmodule.entity.MainCategoryData;

import java.util.ArrayList;

import static com.yilian.mall.R.id.iv_left;
import static com.yilian.mall.ui.JPNewCommDetailActivity.LE_DOU_GOODS_2;


/**
 * @author Ray_L_Pain on 2017/7/13 0013.
 */

public class MallListAdapter<T extends MainCategoryData> extends BaseMultiItemQuickAdapter<T, com.chad.library.adapter.base.BaseViewHolder> {
    /**
     * 1 易划算 2 奖券购 3益豆
     */
    private String type;

    public MallListAdapter(ArrayList<T> data, String type) {
        super(data);
        this.type = type;
        addItemType(MainCategoryData.GOOD, R.layout.item_jp_good);
    }

    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, T item) {
        switch (helper.getItemViewType()) {
            case MainCategoryData.GOOD:
                helper.getView(R.id.item_mall).setVisibility(View.GONE);
                helper.getView(iv_left).setVisibility(View.VISIBLE);
                com.yilian.networkingmodule.entity.JPGoodsEntity entity = (com.yilian.networkingmodule.entity.JPGoodsEntity) item;
                ImageView iv_goods1 = (ImageView) helper.getView(R.id.iv_goods1);
                GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlSetSuffix(entity.JPImageUrl, ScreenUtils.getScreenWidth(mContext) + "", ScreenUtils.getScreenWidth(mContext) + ""), iv_goods1);
                helper.setText(R.id.tv_goods_name, entity.JPGoodsName);
                if ("0".equals(entity.hasGoods)) {
                    helper.getView(R.id.iv_sold_out).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.iv_sold_out).setVisibility(View.GONE);
                }
                switch (type) {
                    case "1":
                        helper.getView(R.id.item_yhs).setVisibility(View.VISIBLE);
                        helper.getView(R.id.item_le_dou).setVisibility(View.GONE);
                        helper.getView(R.id.item_jfg).setVisibility(View.GONE);
                        helper.setText(R.id.tv_goods_name_yhs, entity.JPGoodsName);
                        helper.setImageResource(R.id.iv_left, R.mipmap.icon_yhs);
                        helper.setText(R.id.tv_price_yhs, MoneyUtil.getLeXiangBi(entity.goodsIntegral));
                        helper.setText(R.id.tv_sold_number_yhs, "已售：" + entity.JPSellCount);
                        break;
                    case "2":
                        int jifen = Integer.parseInt(entity.JPGoodsPrice) - Integer.parseInt(entity.JPGoodsCost);
                        helper.getView(R.id.item_yhs).setVisibility(View.GONE);
                        helper.getView(R.id.item_le_dou).setVisibility(View.GONE);
                        helper.getView(R.id.item_jfg).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_goods_name_jfg, entity.JPGoodsName);
                        helper.setImageResource(R.id.iv_left, R.mipmap.icon_jfg);
                        helper.setText(R.id.tv_price_jfg, MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(entity.JPGoodsCost)));
                        helper.setText(R.id.tv_jifen_jfg, "+" + MoneyUtil.getLeXiangBi(jifen));
                        TextView tv_cost_jfg = helper.getView(R.id.tv_cost_jfg);
                        tv_cost_jfg.setText("¥" + MoneyUtil.getLeXiangBi(entity.JPGoodsPrice));
                        tv_cost_jfg.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        helper.setText(R.id.tv_sold_number_jfg, "已售：" + entity.JPSellCount);
                        break;
                    case "3":
                        helper.getView(R.id.item_yhs).setVisibility(View.GONE);
                        helper.getView(R.id.item_jfg).setVisibility(View.GONE);
                        helper.getView(R.id.item_le_dou).setVisibility(View.VISIBLE);

                        helper.setText(R.id.tv_goods_des, entity.JPGoodsName);
                        helper.setText(R.id.tv_goods_beans, "送 " + MoneyUtil.getLeXiangBi(entity.returnBean));
                        helper.setText(R.id.tv_goods_price, com.yilian.mylibrary.MoneyUtil.setNoSmall¥Money(com.yilian.mylibrary.MoneyUtil.getLeXiangBi(entity.JPGoodsCost)));
                        TextView tvGoodsPrice = (TextView) helper.getView(R.id.tv_high_price);
                        if (LE_DOU_GOODS_2.equals(((JPGoodsEntity) item).goodsType)) {
                            tvGoodsPrice.setVisibility(View.INVISIBLE);
                        } else {
                            tvGoodsPrice.setVisibility(View.VISIBLE);
                        }
                        tvGoodsPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        helper.setText(R.id.tv_high_price, com.yilian.mylibrary.MoneyUtil.setSmall¥Money(com.yilian.mylibrary.MoneyUtil.getLeXiangBi(entity.JPGoodsPrice)));
                        helper.setText(R.id.tv_goods_sell_num, "已售" + ((JPGoodsEntity) item).JPSellCount);

                        break;
                    default:
                        break;
                }

                LinearLayout item_layout = helper.getView(R.id.item_layout);
                item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);
                        intent.putExtra("goods_id", entity.JPGoodsId);
                        intent.putExtra("filiale_id", entity.JPFilialeId);
                        intent.putExtra("tags_name", entity.JPTagsName);
                        intent.putExtra("classify", type);
                        mContext.startActivity(intent);
                    }
                });
                break;
            default:
                break;
        }
    }

}
