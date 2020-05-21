package com.yilian.mall.ctrip.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityByDistrictsEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityEntity;

/**
 * 携程 热门城市 县区 适配器
 *
 * @author Created by Zg on 2018/8/17.
 */

public class CtripSiteCityByDistrictsHotAdapter extends BaseQuickAdapter<CtripSiteCityByDistrictsEntity.DataBean, BaseViewHolder> {


    public CtripSiteCityByDistrictsHotAdapter() {
        super(R.layout.ctrip_item_site_city_by_districts);
    }

    @Override
    protected void convert(BaseViewHolder helper, CtripSiteCityByDistrictsEntity.DataBean item) {
        if (!TextUtils.isEmpty(item.locationname)) {
            helper.setText(R.id.tv_name, item.locationname);
        } else {
            helper.setText(R.id.tv_name, item.cityname);
        }

    }
}
