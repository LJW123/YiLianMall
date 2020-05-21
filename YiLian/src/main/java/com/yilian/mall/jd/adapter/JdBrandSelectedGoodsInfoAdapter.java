package com.yilian.mall.jd.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.networkingmodule.entity.jd.JdGoodsBrandSelectedEntity;

import java.util.List;

/**
 * 京东首页品牌精选item中的列表适配器
 *
 * @author Created by zhaiyaohua on 2018/5/24.
 */

public class JdBrandSelectedGoodsInfoAdapter extends BaseQuickAdapter<JdGoodsBrandSelectedEntity.Data.Goods, BaseViewHolder> {
    private int jdType = JumpToOtherPageUtil.JD_GOODS_TYPE_COMMON;

    public JdBrandSelectedGoodsInfoAdapter(@JumpToOtherPageUtil.JDGoodsType int jdType, List<JdGoodsBrandSelectedEntity.Data.Goods> dataList) {
        super(R.layout.jd_item_brand_selected_info, dataList);
        this.jdType = jdType;
    }

    @Override
    protected void convert(BaseViewHolder helper, JdGoodsBrandSelectedEntity.Data.Goods item) {
        GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N2(item.imagePath), helper.getView(R.id.jd_iv_goods));
        if (jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
            helper.getView(R.id.tv_has_rate).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.tv_has_rate).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_has_rate, "预计 " + item.returnBean);
        }

        helper.setText(R.id.jd_goods_des, item.name);
        helper.setText(R.id.jd_goods_price, "¥ " + item.jdPrice + "");
    }
}
