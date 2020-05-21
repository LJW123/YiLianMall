package com.yilian.mylibrary.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.R;

import java.util.List;

/**
 * Created by Ray_L_Pain on 2017/12/25 0025.
 */

public class ImageHozAdapter extends BaseQuickAdapter<String, com.chad.library.adapter.base.BaseViewHolder> {

    public ImageHozAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, String item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) ivIcon.getLayoutParams();
        params.setMargins(0, 0, DPXUnitUtil.dp2px(mContext,28f), 0);
        ivIcon.setLayoutParams(params);
        GlideUtil.showImageWithSuffix(mContext, item, ivIcon);
    }
}