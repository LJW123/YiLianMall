package com.yilian.mall.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.RecordDetailsEntity;

/**
 * @author Created by Zg on 2018/9/17.
 */

public class RecordDetailsAdapter extends BaseQuickAdapter<RecordDetailsEntity.DataBean, BaseViewHolder> {
    public RecordDetailsAdapter() {
        super(R.layout.item_v3_money_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordDetailsEntity.DataBean item) {
        helper.setText(R.id.tv_title, item.title);
        if (item.lineType == 1) {
            helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.view_line).setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_content, item.content);
    }
}
