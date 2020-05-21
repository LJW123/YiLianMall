package com.yilian.mall.adapter;

import android.support.annotation.LayoutRes;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mylibrary.ScreenUtils;

/**
 * Created by Ray_L_Pain on 2018/2/24 0024.
 */

public class V5NavigationAdapter extends BaseQuickAdapter<String, com.chad.library.adapter.base.BaseViewHolder> {

    public V5NavigationAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.item_layout);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) helper.getView(R.id.tv).getLayoutParams();
        params.width = ScreenUtils.getScreenWidth(mContext) / 3 * 2;
        helper.setText(R.id.tv, item);
    }

}
