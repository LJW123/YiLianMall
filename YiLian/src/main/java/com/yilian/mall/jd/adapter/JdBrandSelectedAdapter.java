package com.yilian.mall.jd.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.view.NestRecyclerView.BetterRecyclerView;
import com.yilian.networkingmodule.entity.jd.JdGoodsBrandSelectedEntity;

/**
 * 京东首页品牌精选适配器
 *
 * @author Created by zhaiyaohua on 2018/5/24.
 */

public class JdBrandSelectedAdapter extends BaseQuickAdapter<JdGoodsBrandSelectedEntity.Data, BaseViewHolder> {
    private JdBrandSelectedGoodsInfoAdapter jdBrandSelectedGoodsInfoAdapter;
    private int jdType = JumpToOtherPageUtil.JD_GOODS_TYPE_COMMON;

    public JdBrandSelectedAdapter(@JumpToOtherPageUtil.JDGoodsType int jdType) {
        super(R.layout.jd_item_goods_brand_selected);
        this.jdType = jdType;
    }

    @Override
    protected void convert(BaseViewHolder helper, JdGoodsBrandSelectedEntity.Data item) {
        GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N2(item.imagePath), helper.getView(R.id.jd_iv_goods));
        helper.setText(R.id.jd_name_brand, item.brandName);
        helper.setText(R.id.jd_address_brand, item.productArea);
        BetterRecyclerView recyclerView = helper.getView(R.id.jd_brand_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        jdBrandSelectedGoodsInfoAdapter = new JdBrandSelectedGoodsInfoAdapter(jdType,item.goodsList);
        recyclerView.setAdapter(jdBrandSelectedGoodsInfoAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        helper.addOnClickListener(R.id.jd_option_brand);
        jdBrandSelectedGoodsInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JdGoodsBrandSelectedEntity.Data.Goods goods = (JdGoodsBrandSelectedEntity.Data.Goods) adapter.getItem(position);
                if (!TextUtils.isEmpty(goods.sku)) {
                    if (jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
                        JumpCardActivityUtils.toGoodsDetail(mContext, goods.sku);
                    } else {
                        JumpJdActivityUtils.toGoodsDetail(mContext, goods.sku);
                    }
                }
            }
        });
    }
}
