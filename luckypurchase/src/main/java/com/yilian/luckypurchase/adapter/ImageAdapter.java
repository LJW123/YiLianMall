package com.yilian.luckypurchase.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

/**
 * Created by LYQ on 2017/10/28.
 */

class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    String index;

    public ImageAdapter(int layoutId, String index, ArrayList<String> data) {
        super(layoutId,data);
        this.index = index;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) ivIcon.getLayoutParams();
        params.setMargins(0, 0, DPXUnitUtil.dp2px(mContext,28f), 0);
        ivIcon.setLayoutParams(params);
        Logger.i("ray-" + item.toString());
        GlideUtil.showImageWithSuffix(mContext, item, ivIcon);
    }
}
