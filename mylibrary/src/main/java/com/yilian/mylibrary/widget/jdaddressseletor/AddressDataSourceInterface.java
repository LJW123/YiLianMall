package com.yilian.mylibrary.widget.jdaddressseletor;

import android.content.Context;

import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCity;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCounty;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDProvince;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDStreet;

import java.util.ArrayList;

/**
 * @author Created by  on 2018/5/26.
 */

public interface AddressDataSourceInterface {
    /**
     * 获取区域列表
     * @param context
     * @param callback
     */
//    void getAreaList(Context context, Callback callback);
    /**
     * 获取省份列表
     * @return
     */
    void getProvinceList(Context context,ProvinceCallback callback);

    /**
     * 根据省ID获取市区列表
     * @param provinceId
     * @return
     */
    void getCityList(Context context,String provinceId,CityCallback cityCallback);

    /**
     * 根据市区ID获取城镇列表
     * @param cityId
     * @return
     */
    void getCountyList(Context context,String cityId,CountyCallback callback);

    /**
     * 根据城镇ID获取街道列表
     * @param countyId
     * @return
     */
    void getStreetList(Context context,String countyId,StreetCallback countyCallback);

    /**
     * 获取列表成功回调
     */
    interface ProvinceCallback {
        void provinceCallback(ArrayList<JDProvince> jdArea);
        void failedBack();
    }
    /**
     * 获取列表成功回调
     */
    interface CityCallback {
        void cityCallback(ArrayList<JDCity> jdCities);
        void failedBack();
    }
    /**
     * 获取列表成功回调
     */
    interface CountyCallback {
        void countyCallback(ArrayList<JDCounty> jdCounties);
        void failedBack();
    }
    /**
     * 获取列表成功回调
     */
    interface StreetCallback {
        void streetCallback(ArrayList<JDStreet> jdStreets);
        void failedBack();
    }
}
