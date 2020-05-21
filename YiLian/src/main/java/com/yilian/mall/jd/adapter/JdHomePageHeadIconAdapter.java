package com.yilian.mall.jd.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.jd.JDIconsEntity;

/**
 * 京东首页头部图标适配器
 *
 * @author Created by zhaiyaohua on 2018/5/23.
 */

public class JdHomePageHeadIconAdapter extends BaseQuickAdapter<JDIconsEntity, BaseViewHolder> {
    public JdHomePageHeadIconAdapter() {
        super(R.layout.mt_home_page_item_gridview);
    }

    @Override
    protected void convert(BaseViewHolder helper, JDIconsEntity item) {
        ImageView imageView = helper.getView(R.id.imageView);
        GlideUtil.showImage(mContext,  item.img, imageView);
        TextView textView = helper.getView(R.id.textView);
        textView.setText(item.title);
    }
}
