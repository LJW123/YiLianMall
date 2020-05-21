package com.yilian.mall.ctrip.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityByDistrictsEntity;

/**
 * 携程 选择城市 搜索 适配器
 *
 * @author Created by Zg on 2018/8/17.
 */

public class CtripSiteCityBySearchAdapter extends BaseQuickAdapter<CtripSiteCityByDistrictsEntity.DataBean, BaseViewHolder> {


    public CtripSiteCityBySearchAdapter() {
        super(R.layout.ctrip_item_site_city_by_search);
    }

    @Override
    protected void convert(BaseViewHolder helper, CtripSiteCityByDistrictsEntity.DataBean item) {
        helper.setText(R.id.tv_name, item.cityname + "-" + item.locationname);
    }
}
