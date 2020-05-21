package com.yilian.mall.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.V3MoneyDetailEntity;

/**
 * @author Created by  on 2017/12/13.
 */

public class V3MoneyDetailAdapter extends BaseQuickAdapter<V3MoneyDetailEntity.DataBean, com.chad.library.adapter.base.BaseViewHolder> {
    public V3MoneyDetailAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, V3MoneyDetailEntity.DataBean item) {
        helper.setText(R.id.tv_title, item.title);
        if (item.lineType == 1) {
            helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.view_line).setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_content, item.content);
    }
}
