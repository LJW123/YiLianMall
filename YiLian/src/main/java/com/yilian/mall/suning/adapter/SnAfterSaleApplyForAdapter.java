package com.yilian.mall.suning.adapter;


import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yilian.mall.R;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.suning.SnAfterSaleApplyForListEntity;
import com.yilian.networkingmodule.entity.suning.snMultiItem.SnAfterSaleListLayoutType;

/**
 * 售后申请 列表 适配器
 */
public class SnAfterSaleApplyForAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public SnAfterSaleApplyForAdapter() {
        super(null);
        addItemType(SnAfterSaleListLayoutType.header, R.layout.sn_item_after_sale_apply_for_header);
        addItemType(SnAfterSaleListLayoutType.goods, R.layout.sn_item_after_sale_apply_for_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case SnAfterSaleListLayoutType.header:
                SnAfterSaleApplyForListEntity.DataBean dataBean = (SnAfterSaleApplyForListEntity.DataBean) item;
                helper.setText(R.id.tv_order_number, dataBean.getSnOrderId());
                helper.setText(R.id.tv_order_time, DateUtils.stampToDate(dataBean.getOrderTime() * 1000));
                break;
            case SnAfterSaleListLayoutType.goods:
                SnAfterSaleApplyForListEntity.GoodsList goods = (SnAfterSaleApplyForListEntity.GoodsList) item;
                GlideUtil.showImageWithSuffix(mContext, goods.getSkuPic(), helper.getView(R.id.iv_goods_img));
                helper.setText(R.id.tv_goods_name, goods.getSkuName());
                helper.setText(R.id.tv_goods_price, String.format("¥  %s", goods.getSnPrice()));
                helper.setText(R.id.tv_has_rate, String.format("送 %s", goods.getReturnBean()));
                helper.setText(R.id.tv_goods_num, String.format("x%s", goods.getSkuNum()));
                //申请售后
                helper.addOnClickListener(R.id.tv_apply_for);
                TextView tvApplyFor = helper.getView(R.id.tv_apply_for);
                if (goods.getApplyStatus().equals("1")) {
                    //是否支持退货 0不支持 1支持
                    tvApplyFor.setText("退货退款");
                    tvApplyFor.setTextColor(mContext.getResources().getColor(R.color.color_main_suning));
                    tvApplyFor.setBackgroundResource(R.drawable.sn_order_bt_bg_orange);
                } else {
                    tvApplyFor.setText("不支持退货");
                    tvApplyFor.setTextColor(mContext.getResources().getColor(R.color.notice_text_color));
                    tvApplyFor.setBackgroundResource(R.drawable.sn_order_bt_bg_grey);
                }
                break;
            default:
                break;
        }


    }


}
