package com.yilian.mall.ctrip.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityEntity;

/**
 * 携程 热门城市 适配器
 *
 * @author Created by Zg on 2018/8/17.
 */

public class CtripSiteCityHotAdapter extends BaseQuickAdapter<CtripSiteCityEntity.HotCity, BaseViewHolder> {


    public CtripSiteCityHotAdapter() {
        super(R.layout.ctrip_item_site_city_hot);
    }

    @Override
    protected void convert(BaseViewHolder helper, CtripSiteCityEntity.HotCity item) {
        helper.setText(R.id.tv_name, item.cityname);
    }
}
