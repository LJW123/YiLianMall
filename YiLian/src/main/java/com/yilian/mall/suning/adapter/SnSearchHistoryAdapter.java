package com.yilian.mall.suning.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;

/**
 * 京东搜索适配器
 *
 * @author Created by zhaiyaohua on 2018/5/26.
 */

public class SnSearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SnSearchHistoryAdapter() {
        super(R.layout.activity_find_record_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.find_record_item_tv, item);
    }
}
