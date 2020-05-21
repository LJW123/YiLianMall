package com.yilian.mall.ctrip.model;

import android.content.Context;

import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observable;

/**
 * @author xiaofo on 2018/8/31.
 */

public class CtripHotelDetailMode {
    //  获取酒店详情
    public Observable getCtritHotelDetai(Context context, String hotelId, String startDate, String endDate) {
        return RetrofitUtils3.getRetrofitService(context)
                .getCtripHotelDetail("xc_hotel_detail", hotelId, startDate, endDate);
    }

    /**
     * 获取酒店房型筛选条件
     *
     * @param context
     * @return
     */
    public Observable getCtripHotelFilter(Context context) {
        return RetrofitUtils3.getRetrofitService(context)
                .getCtripHotelFilter("xc_goods/xc_hotel_operations");
    }

    //  获取携程酒店内房型筛选数据
    public Observable getHotelFilterData(Context context, String HotelID, String Start, String End, String RoomBedInfos, String NumberOfBreakfast, String WirelessBroadnet, String WiredBroadnet, String minPrice, String maxPrice, String IsInstantConfirm, String CancelPolicyInfos) {
        return RetrofitUtils3.getRetrofitService(context)
                .getHotel_Rooms_FiltrateData("xc_hotel_rooms", HotelID, Start, End, RoomBedInfos, NumberOfBreakfast, WirelessBroadnet, WiredBroadnet, minPrice, maxPrice, IsInstantConfirm, CancelPolicyInfos);
    }

}
