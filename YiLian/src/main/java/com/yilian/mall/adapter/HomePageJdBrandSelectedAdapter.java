package com.yilian.mall.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.jd.JdBrandSelectedMultiItem;
import com.yilian.networkingmodule.entity.jd.JdGoodsBrandSelectedEntity;

import java.util.List;

/**
 * 首页品牌精选适配器
 *
 * @author Created by zhaiyaohua on 2018/6/19.
 */

public class HomePageJdBrandSelectedAdapter<T extends JdBrandSelectedMultiItem> extends BaseMultiItemQuickAdapter<T, com.chad.library.adapter.base.BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HomePageJdBrandSelectedAdapter(List<T> data) {
        super(data);
        addItemType(JdBrandSelectedMultiItem.ITEM_TYPE_ONE, R.layout.item_jp_brand_selected);
        addItemType(JdBrandSelectedMultiItem.ITEM_TYPE_THREE, R.layout.jd_item_brand_selected_option);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        switch (item.getItemType()) {
            case JdBrandSelectedMultiItem.ITEM_TYPE_THREE:
                JdGoodsBrandSelectedEntity.Data entity = (JdGoodsBrandSelectedEntity.Data) item;
                GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N2(entity.imagePath), helper.getView(R.id.jd_iv_goods));
                helper.setText(R.id.jd_name_brand, entity.brandName);
                helper.setText(R.id.jd_address_brand, entity.productArea);
                break;
            case JdBrandSelectedMultiItem.ITEM_TYPE_ONE:
                JdGoodsBrandSelectedEntity.Data.Goods goods = (JdGoodsBrandSelectedEntity.Data.Goods) item;
                GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N2(goods.imagePath), helper.getView(R.id.iv_brand_goods));
                helper.setText(R.id.tv_le_dou, "预计 " + goods.returnBean);
                helper.setText(R.id.tv_goods_des, goods.name);
                helper.setText(R.id.tv_goods_price, "¥ " + goods.jdPrice + "");
                if (goods.getTag() == 1) {
                    helper.getView(R.id.left_line).setVisibility(View.VISIBLE);
                    helper.getView(R.id.right_line).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.left_line).setVisibility(View.GONE);
                    helper.getView(R.id.right_line).setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }

    }
}
