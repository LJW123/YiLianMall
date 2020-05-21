package com.yilian.mylibrary.widget.jdaddressseletor;

import android.content.Context;

import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCity;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCounty;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDProvince;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDStreet;

import java.util.List;

/**
 * @author Created by  on 2018/5/25.
 */

public class AddressDictManager {
    public AddressDictManager(Context context) {

    }

    public List<JDProvince> getProvinceList() {
        return null;
    }

    public List<JDCity> getCityList(int provinceId) {
        return null;
    }

    public List<JDCounty> getCountyList(int cityId) {
        return null;
    }

    public List<JDStreet> getStreetList(int countyId) {
        return null;
    }

    public JDProvince getProvinceBean(String provinceCode) {
        return null;
    }

    public JDCity getCityBean(String cityCode) {
        return null;
    }

    public JDCounty getCountyBean(String countyCode) {
        return null;
    }

    public JDStreet getStreetBean(String streetCode) {
        return null;
    }
}
