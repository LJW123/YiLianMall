package com.yilian.mall.shoppingcard;

import android.content.Context;

import com.yilian.mylibrary.widget.jdaddressseletor.AddressDataSourceInterface;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCity;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCounty;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDProvince;
import com.yilian.networkingmodule.entity.jd.JDAreaStringData;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/5/26.
 */

public class CardAddressDataSourceImpl implements AddressDataSourceInterface {

//    @Override
//    public void getAreaList(Context context, Callback callback) {
//
//    }

    /**
     * 获取省份数据
     * @param context
     * @param provinceCallback
     */
    @Override
    public void getProvinceList(Context context, ProvinceCallback provinceCallback) {
        RetrofitUtils3.getRetrofitService(context)
                .getShppingCardAreaStringData("card/yilian_address", "1", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDAreaStringData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JDAreaStringData o) {
                        ArrayList<JDProvince> jdProvinces = new ArrayList<>();
                        for (Map.Entry<String, String> stringStringEntry : o.data.entrySet()) {
                            JDProvince jdProvince = new JDProvince(stringStringEntry.getKey(), stringStringEntry.getValue());
                            jdProvinces.add(jdProvince);
                        }
                        provinceCallback.provinceCallback(jdProvinces);
                    }
                });
    }

    /**
     * 获取市级数据
     * @param context
     * @param provinceId
     * @param cityCallback
     */
    @Override
    public void getCityList(Context context, String provinceId, CityCallback cityCallback) {
        RetrofitUtils3.getRetrofitService(context)
                .getShppingCardAreaStringData("card/yilian_address", "2", provinceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDAreaStringData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JDAreaStringData o) {
                        ArrayList<JDCity> jdCities = new ArrayList<>();
                        for (Map.Entry<String, String> stringStringEntry : o.data.entrySet()) {
                            JDCity jdProvince = new JDCity(stringStringEntry.getKey(), stringStringEntry.getValue());
                            jdCities.add(jdProvince);
                        }
                        cityCallback.cityCallback(jdCities);
                    }
                });
    }

    @Override
    public void getCountyList(Context context, String cityId, CountyCallback countyCallback) {
        RetrofitUtils3.getRetrofitService(context)
                .getShppingCardAreaStringData("card/yilian_address", "3", cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDAreaStringData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JDAreaStringData o) {
                        ArrayList<JDCounty> jdCounties = new ArrayList<>();
                        for (Map.Entry<String, String> stringStringEntry : o.data.entrySet()) {
                            JDCounty jdProvince = new JDCounty(stringStringEntry.getKey(), stringStringEntry.getValue());
                            jdCounties.add(jdProvince);
                        }
                        countyCallback.countyCallback(jdCounties);
                    }
                });
    }

    @Override
    public void getStreetList(Context context, String countyId, StreetCallback streetCallback) {
        streetCallback.streetCallback(new ArrayList<>());
    }
}
