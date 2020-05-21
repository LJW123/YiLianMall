package com.leshan.ylyj.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;


import java.util.List;

/**
 * 赚取佣金
 */

public class MakeMoneyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public MakeMoneyAdapter(List data) {
        super(R.layout.item_make_money, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        helper.setText(R.id.title_tv, item);
    }
}
