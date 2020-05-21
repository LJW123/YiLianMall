package com.yilian.mall.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by liuyuqi on 2017/9/2 0002.
 */

public class InformationAdapter extends BaseQuickAdapter<String, com.chad.library.adapter.base.BaseViewHolder> {

    public InformationAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
