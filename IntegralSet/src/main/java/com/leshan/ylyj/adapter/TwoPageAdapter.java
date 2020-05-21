package com.leshan.ylyj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.bean.TwoPageBean;


import java.util.List;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class TwoPageAdapter extends BaseQuickAdapter<TwoPageBean, BaseViewHolder> {
    public TwoPageAdapter(List data) {
        super(R.layout.item_for_two_page_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TwoPageBean item) {
        helper.setImageResource(R.id.icon_iv, item.getImg());
        helper.setText(R.id.title_tv, item.getTitle());
    }
}
