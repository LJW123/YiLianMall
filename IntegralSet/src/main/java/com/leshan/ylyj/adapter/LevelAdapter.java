package com.leshan.ylyj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.bean.LevelBean;


import java.util.List;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class LevelAdapter extends BaseQuickAdapter<LevelBean, BaseViewHolder> {

    public LevelAdapter(List data) {
        super(R.layout.item_for_care_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LevelBean item) {
        helper.setText(R.id.title_tv, item.getName());
        helper.setText(R.id.level_tv, item.getLevel());
    }
}
