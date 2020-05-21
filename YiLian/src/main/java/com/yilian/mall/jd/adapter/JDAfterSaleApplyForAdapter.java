package com.yilian.mall.jd.adapter;


import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yilian.mall.R;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.networkingmodule.entity.jd.jdMultiItem.JdAfterSaleListLayoutType;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplyForListEntity;

/**
 * 售后申请 列表 适配器
 */
public class JDAfterSaleApplyForAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public JDAfterSaleApplyForAdapter() {
        super(null);
        addItemType(JdAfterSaleListLayoutType.header, R.layout.jd_item_after_sale_apply_for_header);
        addItemType(JdAfterSaleListLayoutType.goods, R.layout.jd_item_after_sale_apply_for_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case JdAfterSaleListLayoutType.header:
                //是否展示分割线
                if (helper.getLayoutPosition() == 0) {
                    helper.getView(R.id.ll_header).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.ll_header).setVisibility(View.VISIBLE);
                }
                JDAfterSaleApplyForListEntity.DataBean dataBean = (JDAfterSaleApplyForListEntity.DataBean) item;
                helper.setText(R.id.tv_order_number, "订单编号：" + dataBean.getJdOrderId());
                helper.setText(R.id.tv_order_time, "下单时间：" + DateUtils.stampToDate(dataBean.getOrderTime() * 1000));
                break;
            case JdAfterSaleListLayoutType.goods:
                JDAfterSaleApplyForListEntity.GoodsList goods = (JDAfterSaleApplyForListEntity.GoodsList) item;
                GlideUtil.showImageWithSuffix(mContext, JDImageUtil.getJDImageUrl_N3(goods.getSkuPic()), helper.getView(R.id.iv_goods_img));
                helper.setText(R.id.tv_goods_name, goods.getSkuName());
                helper.setText(R.id.tv_goods_num, "数量：" + goods.getSkuNum());
                if(goods.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD){
                    helper.getView(R.id.iv_order_card_tab).setVisibility(View.VISIBLE);
                }else {
                    helper.getView(R.id.iv_order_card_tab).setVisibility(View.GONE);
                }
                //申请售后
                helper.addOnClickListener(R.id.tv_apply_for);
                break;
            default:
                break;
        }


    }


}
