package com.yilian.mall.suning.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.suning.SnGoodsBrandSelectedEntity;

/**
 * 苏宁首页品牌精选适配器
 *
 * @author Created by zhaiyaohua on 2018/5/24.
 */

public class SnBrandSelectedAdapter extends BaseQuickAdapter<SnGoodsBrandSelectedEntity.Data, BaseViewHolder> {
    private SnBrandSelectedGoodsInfoAdapter snBrandSelectedGoodsInfoAdapter;

    public SnBrandSelectedAdapter() {
        super(R.layout.sn_item_goods_brand_selected);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnGoodsBrandSelectedEntity.Data item) {
        GlideUtil.showImage(mContext, item.image, helper.getView(R.id.sn_iv_goods));
        helper.setText(R.id.sn_name_brand, item.brand);
        helper.setText(R.id.sn_address_brand, "产地：" + item.productArea);
        RecyclerView recyclerView = helper.getView(R.id.sn_brand_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        snBrandSelectedGoodsInfoAdapter = new SnBrandSelectedGoodsInfoAdapter(item.goodsList);
        recyclerView.setAdapter(snBrandSelectedGoodsInfoAdapter);
        helper.addOnClickListener(R.id.sn_more);
        snBrandSelectedGoodsInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SnGoodsBrandSelectedEntity.Data.Goods goods = (SnGoodsBrandSelectedEntity.Data.Goods) adapter.getItem(position);
                if (!TextUtils.isEmpty(goods.skuId)) {
                    //苏宁商品详情
                    JumpSnActivityUtils.toSnGoodsDetailActivity(mContext, goods.skuId);
                }
            }
        });
    }
}
