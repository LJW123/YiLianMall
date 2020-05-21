package com.yilian.mall.ctrip.adapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityEntity;

/**
 * 携程 选择城市 适配器
 *
 * @author Created by Zg on 2018/8/17.
 */

public class CtripSiteCityAdapter extends BaseQuickAdapter<CtripSiteCityEntity.City, BaseViewHolder> {


    public CtripSiteCityAdapter() {
        super(R.layout.ctrip_item_site_city);
    }

    @Override
    protected void convert(BaseViewHolder helper, CtripSiteCityEntity.City item) {
        helper.setText(R.id.tv_name, item.cityname);
    }
}