package com.yilian.mall.ui.mvp.view;

import com.yilian.networkingmodule.entity.RegionEntity2;

import java.util.ArrayList;

/**
 * @author Created by  on 2018/1/16.
 */

public interface ILocationView extends IBaseView {
    void showArea(ArrayList<RegionEntity2.ProvincesBean> provinceList, ArrayList<ArrayList<RegionEntity2.ProvincesBean.CitysBean>> cityList
            , ArrayList<ArrayList<ArrayList<RegionEntity2.ProvincesBean.CitysBean.CountysBean>>> countyList);

    void setLocation(com.yilian.networkingmodule.entity.Location location);
}
