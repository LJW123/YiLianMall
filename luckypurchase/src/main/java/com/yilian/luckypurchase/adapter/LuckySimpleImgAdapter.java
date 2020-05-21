package com.yilian.luckypurchase.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.R;
import com.yilian.mylibrary.GlideUtil;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/11/1 0001
 */

public class LuckySimpleImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public LuckySimpleImgAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView iv = helper.getView(R.id.iv);
        iv.setAdjustViewBounds(true);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        iv.setLayoutParams(params);
        if (item.toString().contains("null")) {
            iv.setVisibility(View.GONE);
        } else {
            GlideUtil.showImgAdapt(mContext, item.toString(), iv);
        }
    }
}
