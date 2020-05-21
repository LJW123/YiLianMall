package com.yilian.mall.shoppingcard.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.jd.JDGoodsAbstractInfoEntity;


public class CardGoodsListAdapter extends BaseQuickAdapter<JDGoodsAbstractInfoEntity, BaseViewHolder> {


    public CardGoodsListAdapter() {
        super(R.layout.card_item_home_list);
    }


    @Override
    protected void convert(BaseViewHolder helper, JDGoodsAbstractInfoEntity item) {
        ImageView ivTop = helper.getView(R.id.iv_goods);
        LinearLayout.LayoutParams bannerParams = (LinearLayout.LayoutParams) ivTop.getLayoutParams();
        bannerParams.width = DPXUnitUtil.getScreenWidth(mContext) / 2 - DPXUnitUtil.dp2px(mContext, 2);
        bannerParams.height = bannerParams.width;
        ivTop.setLayoutParams(bannerParams);

        GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N1(item.imagePath), ivTop);
        helper.setText(R.id.tv_goods_des, item.name);
        helper.setText(R.id.tv_goods_price, "¥" + item.jdPrice + "");
        helper.setText(R.id.tv_sale_num, "已售：" + item.saleCount);
    }

}
