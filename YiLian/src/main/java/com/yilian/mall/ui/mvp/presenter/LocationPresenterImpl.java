package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.ILocationModel;
import com.yilian.mall.ui.mvp.model.LocationModelImpl;
import com.yilian.mall.ui.mvp.view.ILocationView;
import com.yilian.networkingmodule.entity.RegionEntity2;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/16.
 */

public class LocationPresenterImpl implements ILocationPresenter {
    private ILocationView iLocationView;
    private final ILocationModel iLocationModel;

    public LocationPresenterImpl(ILocationView iLocationView) {
        this.iLocationView = iLocationView;
        iLocationModel = new LocationModelImpl();
    }

    @Override
    public void onDestory() {

    }

    @Override
    public Subscription getAddressList(Context context) {

        iLocationView.startMyDialog();
        return iLocationModel.getAddressList(context, new Observer<RegionEntity2>() {
            @Override
            public void onCompleted() {
                iLocationView.stopMyDialog();
            }

            @Override
            public void onError(Throwable e) {
                iLocationView.stopMyDialog();
                iLocationView.showToast(e.getMessage());
            }

            @Override
            public void onNext(RegionEntity2 regionEntity) {
                ArrayList<RegionEntity2.ProvincesBean> provinceList = new ArrayList<>();
                ArrayList<ArrayList<RegionEntity2.ProvincesBean.CitysBean>> cityList = new ArrayList<>();
                ArrayList<ArrayList<ArrayList<RegionEntity2.ProvincesBean.CitysBean.CountysBean>>> countyList = new ArrayList<>();
                ArrayList<RegionEntity2.ProvincesBean> list = regionEntity.list;
                for (int i = 0, count = list.size(); i < count; i++) {
                    RegionEntity2.ProvincesBean provinceBean = list.get(i);
                    //省份列表
                    provinceList.add(provinceBean);
                    //市列表
                    cityList.add(provinceBean.citys);
                    ArrayList<ArrayList<RegionEntity2.ProvincesBean.CitysBean.CountysBean>> countysBeen = new ArrayList<>();
                    for (int j = 0; j < provinceBean.citys.size(); j++) {
                        countysBeen.add(provinceBean.citys.get(j).countys);
                    }
//                    县级列表
                    countyList.add(countysBeen);
                }
                iLocationView.showArea(provinceList, cityList, countyList);
            }
        });
    }

    @Override
    public Subscription getLocation(Context context) {
        iLocationView.startMyDialog();
        return iLocationModel.getLocation(context, new Observer<com.yilian.networkingmodule.entity.Location>() {
            @Override
            public void onCompleted() {
                iLocationView.stopMyDialog();
            }

            @Override
            public void onError(Throwable e) {
                iLocationView.stopMyDialog();
                iLocationView.showToast(e.getMessage());
            }

            @Override
            public void onNext(com.yilian.networkingmodule.entity.Location location) {
                iLocationView.setLocation(location);
            }
        });
    }
}
