package com.yilian.mylibrary.widget.jdaddressseletor;


import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCity;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCounty;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDProvince;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDStreet;

/**
 * @author Created by  on 2018/5/25.
 */

public interface OnAddressSelectedListener {
    void onAddressSelected(JDProvince province, JDCity city, JDCounty county, JDStreet street);
}
