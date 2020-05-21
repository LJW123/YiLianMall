package com.yilian.mall.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.JPGoodsEntity;
import com.yilian.networkingmodule.entity.JPShopEntity;
import com.yilian.networkingmodule.entity.MainCategoryData;
import com.yilian.networkingmodule.entity.MainCategoryGoodsTitleView;

import java.util.List;

/**
 * Created by  on 2017/7/31 0031.
 */

public class MainCategoryListAdapter<T extends MainCategoryData> extends BaseMultiItemQuickAdapter<T, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MainCategoryListAdapter(List<T> data) {
        super(data);
        addItemType(MainCategoryData.SHOP, R.layout.item_jp_shop);
        addItemType(MainCategoryData.GOOD, R.layout.item_jp_good);
        addItemType(MainCategoryData.TITLE, R.layout.view_category_title);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        int imageWidth = ScreenUtils.getScreenWidth(mContext) / 2;

        switch (helper.getItemViewType()) {
            case MainCategoryData.SHOP:
                JPShopEntity.DataBean.SuppliersBean data = (JPShopEntity.DataBean.SuppliersBean) item;
//                View includeShopInfo = helper.getView(R.id.include_shop_info);
//                ((TextView)includeShopInfo.findViewById(R.id.tv_shop_name1)).setText(data.JPShopSupplierName);
                helper.setText(R.id.tv_shop_name1, data.JPShopSupplierName);
                helper.getView(R.id.tv_activity_name).setVisibility(View.INVISIBLE);
                ImageView ivShop = helper.getView(R.id.iv_goods);
                ivShop.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlSetSuffix(data.JPShopImageUrl, String.valueOf(imageWidth / 2), String.valueOf(imageWidth / 2)), ivShop);
                switch (helper.getLayoutPosition() % 2) {
                    case 0:
                        helper.getView(R.id.view_line).setVisibility(View.GONE);
                        break;
                    case 1:
                        helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case MainCategoryData.GOOD:
                JPGoodsEntity jpFragmentGoodEntity = (JPGoodsEntity) item;
                helper.setText(R.id.tv_goods_name, jpFragmentGoodEntity.JPGoodsName);
                helper.setText(R.id.tv_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(jpFragmentGoodEntity.JPGoodsCost)));
                TextView tvMarketPrice = helper.getView(R.id.tv_market_price);
                tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                tvMarketPrice.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(jpFragmentGoodEntity.JPGoodsPrice)));
                helper.setText(R.id.tv_discount, mContext.getString(R.string.back_score) + MoneyUtil.getLeXiangBiNoZero(jpFragmentGoodEntity.returnIntegral));
                helper.getView(R.id.tv_tag).setVisibility(View.GONE);
                helper.setText(R.id.tv_sold_number, "已售:" + jpFragmentGoodEntity.JPSellCount);
                if (jpFragmentGoodEntity.hasGoods.equals("0")) {
                    helper.getView(R.id.iv_sold_out).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.iv_sold_out).setVisibility(View.GONE);
                }
                ImageView ivGoods = helper.getView(R.id.iv_goods1);
                ivGoods.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlSetSuffix(jpFragmentGoodEntity.JPImageUrl, String.valueOf(imageWidth / 2), String.valueOf(imageWidth / 2)), ivGoods);
                switch (helper.getLayoutPosition() % 2) {
                    case 0:
                        helper.getView(R.id.view_line).setVisibility(View.GONE);
                        break;
                    case 1:
                        helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case MainCategoryData.TITLE:
                MainCategoryGoodsTitleView item1 = (MainCategoryGoodsTitleView) item;
                helper.setText(R.id.tv_goods_list_title, item1.title);
                break;
        }
    }
}
