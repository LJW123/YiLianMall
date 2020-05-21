package com.yilian.mall.ctrip.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityByDistrictsEntity;

/**
 * 创建订单 价格明细 适配器
 *
 * @author Created by Zg on 2018/8/17.
 */

public class CtripCommitOrderPriceParticularsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int size;
    private int number;
    private String price;


    public CtripCommitOrderPriceParticularsAdapter() {
        super(R.layout.ctrip_item_commit_order_price_particulars);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_date, item);
        if (helper.getLayoutPosition() != size - 1) {
            helper.setText(R.id.tv_price, String.format("%s x ¥%s", number, price));
        }

    }

    public void setParameter(int size, int number, String price) {
        this.size = size;
        this.number = number;
        this.price = price;
    }
}
