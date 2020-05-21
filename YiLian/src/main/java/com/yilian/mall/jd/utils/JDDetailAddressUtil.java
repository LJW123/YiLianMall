package com.yilian.mall.jd.utils;

import android.text.TextUtils;

/**
 * @author Created by  on 2018/5/27.
 */

public class JDDetailAddressUtil {
    /**
     * 拼接JD详细收货地址
     * @param provincecName
     * @param cityName
     * @param countyName
     * @param townName
     * @return
     */
    public static String getJDShippingDetailAddressStr(String provincecName,String cityName,String countyName,String townName,String detailAddress){
        StringBuilder addressBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(provincecName)) {
            addressBuilder.append(provincecName);
        }
        if (!TextUtils.isEmpty(cityName)) {
            addressBuilder.append(cityName);
        }
        if (!TextUtils.isEmpty(countyName)) {
            addressBuilder.append(countyName);
        }
        if (!TextUtils.isEmpty(townName)) {
            addressBuilder.append(townName);
        }
        if (!TextUtils.isEmpty(detailAddress)) {
            addressBuilder.append("  ").append(detailAddress);
        }
        String string = addressBuilder.toString();
        if (TextUtils.isEmpty(string)) {
            string = "请选择地址";
        }
        return string;

    }
}
