package com.yilian.mall.ctrip.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    protected BaseRecyclerViewHolder(View view) {
        super(view);
        initView(view);
    }

    protected abstract void initView(View view);


}
