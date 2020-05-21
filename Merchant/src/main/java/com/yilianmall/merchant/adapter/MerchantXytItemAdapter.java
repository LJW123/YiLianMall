package com.yilianmall.merchant.adapter;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.networkingmodule.entity.MerchantXytEntity;
import com.yilianmall.merchant.R;

/**
 * Created by Ray_L_Pain on 2018/2/8 0008.
 */

public class MerchantXytItemAdapter extends BaseQuickAdapter<MerchantXytEntity.DataBean.ListBean, BaseViewHolder> {

    public MerchantXytItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MerchantXytEntity.DataBean.ListBean item) {

        TextView tvDesc = helper.getView(R.id.tv_desc);
        tvDesc.setText(item.name);
        tvDesc.setText(Html.fromHtml("<font color=\"#333333\">" + item.durationName + "</font><font color=\"#FFD7AE5C\">" + item.amountPay + "</font> <font color=\"#333333\">元</font>"));

        TextView tvZhe = helper.getView(R.id.tv_zhe);
        if ("10".equals(item.discount)) {
            tvZhe.setVisibility(View.GONE);
        } else {
            tvZhe.setVisibility(View.VISIBLE);
            tvZhe.setText(item.discount + "折");
        }

        TextView tvBtn = helper.getView(R.id.tv_btn);

    }
}
