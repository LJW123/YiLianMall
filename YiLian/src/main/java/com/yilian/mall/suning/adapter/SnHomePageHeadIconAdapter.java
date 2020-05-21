package com.yilian.mall.suning.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.suning.SnHomePageIcon;

/**
 * 苏宁首页头部图标适配器
 *
 * @author Created by zhaiyaohua on 2018/5/23.
 */

public class SnHomePageHeadIconAdapter extends BaseQuickAdapter<SnHomePageIcon, BaseViewHolder> {
    public SnHomePageHeadIconAdapter() {
        super(R.layout.mt_home_page_item_gridview);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnHomePageIcon item) {
        ImageView imageView = helper.getView(R.id.imageView);
        GlideUtil.showImage(mContext, item.img, imageView);
        TextView textView = helper.getView(R.id.textView);
        textView.setText(item.title);
    }
}
