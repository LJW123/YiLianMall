package com.yilian.mall.jd.adapter;


import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.jd.JDOrderListEntity;

import java.util.List;


public class JDOrderCommonGoodsListAdapter extends BaseQuickAdapter<JDOrderListEntity.GoodsList, BaseViewHolder> {

    private List<JDOrderListEntity.GoodsList> list;

    public JDOrderCommonGoodsListAdapter(List<JDOrderListEntity.GoodsList> list) {
        super(R.layout.jd_item_order_common_goods_list);
        this.list = list;
        setNewData(this.list);
    }

    @Override
    protected void convert(BaseViewHolder helper, JDOrderListEntity.GoodsList item) {
        helper.getView(R.id.view_left).setVisibility(View.GONE);
        helper.getView(R.id.view_right).setVisibility(View.GONE);
        if (helper.getLayoutPosition() == 0) {
            helper.getView(R.id.view_left).setVisibility(View.VISIBLE);
        }
        if (helper.getLayoutPosition() == list.size() - 1) {
            helper.getView(R.id.view_right).setVisibility(View.VISIBLE);
        }
        GlideUtil.showImageWithSuffix(mContext, JDImageUtil.getJDImageUrl_N3(item.getSkuPic()), helper.getView(R.id.iv_goods_img));
    }


}
