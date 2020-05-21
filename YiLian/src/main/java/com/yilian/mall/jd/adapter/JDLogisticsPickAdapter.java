package com.yilian.mall.jd.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;


public class JDLogisticsPickAdapter extends BaseQuickAdapter<String, BaseViewHolder> {



    public JDLogisticsPickAdapter() {
        super(R.layout.jd_item_logistics_pick);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        //绑定数据
        helper.setText(R.id.tv_name, item);
    }


}
