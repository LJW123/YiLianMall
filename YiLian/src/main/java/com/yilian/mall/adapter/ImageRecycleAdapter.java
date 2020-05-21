package com.yilian.mall.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.R;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

/**
 * Created by LYQ on 2017/10/28.
 */

public class ImageRecycleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ImageRecycleAdapter(int layoutId, ArrayList<String> data) {
        super(layoutId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) ivIcon.getLayoutParams();
        params.setMargins(0, 0, DPXUnitUtil.dp2px(mContext, 10f), 0);
        ivIcon.setLayoutParams(params);
        GlideUtil.showImageWithSuffix(mContext, item, ivIcon);
    }
}
