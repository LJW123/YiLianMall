package com.yilian.mall.suning.address;

import android.content.Context;

import com.yilian.mylibrary.widget.jdaddressseletor.AddressDataSourceInterface;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCity;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCounty;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDProvince;
import com.yilian.networkingmodule.entity.SnSelectorAddressData;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/5/26.
 */

public class SnAddressDataSourceImpl implements AddressDataSourceInterface {

//    @Override
//    public void getAreaList(Context context, Callback callback) {
//
//    }

    @Override
    public void getProvinceList(Context context, ProvinceCallback provinceCallback) {
        RetrofitUtils3.getRetrofitService(context)
                .selectorSnAddress("suning_address/suning_area_list", "1", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnSelectorAddressData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SnSelectorAddressData o) {
                        ArrayList<JDProvince> jdProvinces = new ArrayList<>();
                        for (SnSelectorAddressData.DataBean datum : o.data) {
                            JDProvince jdProvince = new JDProvince(datum.name, datum.id);
                            jdProvinces.add(jdProvince);
                        }
                        provinceCallback.provinceCallback(jdProvinces);
                    }
                });
    }

    @Override
    public void getCityList(Context context, String provinceId, CityCallback cityCallback) {
        RetrofitUtils3.getRetrofitService(context)
                .selectorSnAddress("suning_address/suning_area_list", "2", provinceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnSelectorAddressData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SnSelectorAddressData o) {
                        ArrayList<JDCity> jdCities = new ArrayList<>();
                        for (SnSelectorAddressData.DataBean datum : o.data) {
                            JDCity jdCity = new JDCity(datum.name, datum.id);
                            jdCities.add(jdCity);
                        }
                        cityCallback.cityCallback(jdCities);
                    }
                });
    }

    @Override
    public void getCountyList(Context context, String cityId, CountyCallback countyCallback) {
        RetrofitUtils3.getRetrofitService(context)
                .selectorSnAddress("suning_address/suning_area_list", "3", cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnSelectorAddressData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SnSelectorAddressData o) {

                        ArrayList<JDCounty> jdCounties = new ArrayList<>();
                        for (SnSelectorAddressData.DataBean datum : o.data) {
                            JDCounty jdCounty = new JDCounty(datum.name, datum.id);
                            jdCounties.add(jdCounty);
                        }
                        countyCallback.countyCallback(jdCounties);
                    }
                });
    }

    @Override
    public void getStreetList(Context context, String countyId, StreetCallback streetCallback) {
        streetCallback.streetCallback(null);
    }
}
