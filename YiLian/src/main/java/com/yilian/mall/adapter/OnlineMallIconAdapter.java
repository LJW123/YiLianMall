package com.yilian.mall.adapter;


import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.OnlineMallBannerJsonEntity;

/**
 * Created by ${zhaiyaohua} on 2017/12/30 0030.
 * @author zhaiyaohua
 */

public class OnlineMallIconAdapter extends BaseQuickAdapter<OnlineMallBannerJsonEntity.IconClass,BaseViewHolder> {


    public OnlineMallIconAdapter(@LayoutRes int layoutResId,boolean hanHeader) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, OnlineMallBannerJsonEntity.IconClass item) {
       GlideUtil.showImageWithSuffix(mContext,item.image,helper.getView(R.id.imageView));
       helper.setText(R.id.textView,item.name);
    }
}
