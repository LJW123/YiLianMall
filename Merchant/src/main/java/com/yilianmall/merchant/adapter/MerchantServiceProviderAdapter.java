package com.yilianmall.merchant.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.MerchantServiceProviderBean;
import com.yilianmall.merchant.R;

import java.util.List;

/**
 * Created by Administrator on 2018/2/3 0003.
 */

public class MerchantServiceProviderAdapter extends BaseQuickAdapter<MerchantServiceProviderBean.DataBean, BaseViewHolder> {

    public MerchantServiceProviderAdapter(List<MerchantServiceProviderBean.DataBean> data) {
        super(R.layout.merchant_item_service_provider, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, MerchantServiceProviderBean.DataBean item) {
        GlideUtil.showImage(mContext, item.imgSrc, (ImageView) viewHolder.getView(R.id.image));
    }

}
