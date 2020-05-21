package com.yilian.mall.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ui.MTComboDetailsActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.V5MerchantDetailEntity;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/12/25 0025
 */

public class V5ComboAdapter extends BaseQuickAdapter<V5MerchantDetailEntity.InfoBean.PackageBean, com.chad.library.adapter.base.BaseViewHolder> {
    private String merchantId;

    public V5ComboAdapter(@LayoutRes int layoutResId, String merchantId) {
        super(layoutResId);
        this.merchantId = merchantId;
    }

    @Override
    protected void convert(BaseViewHolder helper, V5MerchantDetailEntity.InfoBean.PackageBean item) {
        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showImage(mContext, item.packageIcon, iv);

        helper.setText(R.id.tv_name, item.name);

        helper.setText(R.id.tv_sale_count, "已售" + item.sellCount);

        helper.setText(R.id.tv_price, "¥" + MoneyUtil.getLeXiangBiNoZero(item.price));

        helper.setText(R.id.tv_integral, "可得奖券" + MoneyUtil.getLeXiangBiNoZero(item.returnIntegral));

        LinearLayout itemLayout = helper.getView(R.id.item_layout);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MTComboDetailsActivity.class);
                intent.putExtra("package_id", item.packageId);
                intent.putExtra("merchant_id", merchantId);
                mContext.startActivity(intent);
            }
        });
    }
}
