package com.yilian.mall.ctrip.util;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yilian.mall.ctrip.activity.ActivityFacilityDeail;
import com.yilian.mall.ctrip.activity.ActivityMapSearch;
import com.yilian.mall.ctrip.activity.CtripCommitOrderActivity;
import com.yilian.mall.ctrip.activity.CtripHomePageActivity;
import com.yilian.mall.ctrip.activity.CtripHotelDetailActivity;
import com.yilian.mall.ctrip.activity.CtripKeywordActivity;
import com.yilian.mall.ctrip.activity.CtripKeywordBySearchActivity;
import com.yilian.mall.ctrip.activity.CtripOrderCancelActivity;
import com.yilian.mall.ctrip.activity.CtripOrderCancelSuccessfulActivity;
import com.yilian.mall.ctrip.activity.CtripOrderDetailsActivity;
import com.yilian.mall.ctrip.activity.CtripOrderPaySuccessActivity;
import com.yilian.mall.ctrip.activity.CtripOrderPaymentActivity;
import com.yilian.mall.ctrip.activity.CtripOrderProgressActivity;
import com.yilian.mall.ctrip.activity.CtripOrderShareActivity;
import com.yilian.mall.ctrip.activity.CtripSiteCityActivity;
import com.yilian.mall.ctrip.activity.CtripSiteCityByDistrictsActivity;
import com.yilian.mall.ctrip.activity.CtripSiteCityBySearchActivity;
import com.yilian.mall.ctrip.activity.CtripVoucherActivity;
import com.yilian.mall.ctrip.activity.HotelQueryActivity;
import com.yilian.mall.ctrip.activity.MapSearchHotelInfoActivity;
import com.yilian.mall.ctrip.bean.MapHotelInfoBean;
import com.yilian.mall.entity.BookRoomInfo;
import com.yilian.networkingmodule.entity.SearchFilterBean;
import com.yilian.networkingmodule.entity.ctrip.CtripCommitOrderEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderDetailEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderShareEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripPayInfoEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripKeywordModel;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripPriceModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 用于 携程
 * Created by Zg on 2018/8/15activity跳转等
 */
public class JumpCtripActivityUtils {


    /**
     * 跳转到首页
     *
     * @param ctripHomeTab 当前fragment的tab角标
     */
    public static void toCtripHomePage(Context mContext, @CtripHomePageActivity.CtripHomeTab int ctripHomeTab) {
        Intent intent = new Intent(mContext, CtripHomePageActivity.class);
        intent.putExtra("tab", ctripHomeTab);
        mContext.startActivity(intent);
    }

    /**
     * 地图查看酒店位置信息
     * @param context
     * @param hotelId 酒店id
     */
    public static void toMapSearchHotelInfo(Context context, String  hotelId){
        Intent intent = new Intent(context, MapSearchHotelInfoActivity.class);
        intent.putExtra("hotelId",hotelId);
        context.startActivity(intent);
    }


    /**
     * 跳转 选择城市
     *
     * @param mContext
     */
    public static void toCtripSiteCity(Context mContext) {
        Intent intent = new Intent(mContext, CtripSiteCityActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 跳转酒店查询
     */
    public static void toHotelQuery(Context mContext, CtripKeywordModel mCtripKeyword, CtripPriceModel mCtripPrice,String checkIn,String checkOut) {
        Intent intent = new Intent(mContext, HotelQueryActivity.class);
        intent.putExtra("CtripKeywordModel", mCtripKeyword);
        intent.putExtra("CtripPriceModel", mCtripPrice);
        intent.putExtra("checkIn", checkIn);
        intent.putExtra("checkOut", checkOut);
        mContext.startActivity(intent);
    }

    //   跳转地图搜索
    public static void toActivity_Map_Search(Context context, @Nullable CtripKeywordModel mCtripKeyword,@Nullable CtripPriceModel mCtripPrice,@Nullable SearchFilterBean filterDataBean) {
        Intent intent = new Intent(context, ActivityMapSearch.class);
        intent.putExtra("CtripKeywordModel", mCtripKeyword);
        intent.putExtra("CtripPriceModel", mCtripPrice);
        intent.putExtra("FiltrateData",filterDataBean);
        context.startActivity(intent);
    }

    /**
     * 跳转设施详情
     * @param context
     */
    public static void toFacilityDetailActivity(Context context) {
        Intent intent = new Intent(context, ActivityFacilityDeail.class);
        context.startActivity(intent);
    }


    /**
     * 跳转 关键字
     *
     * @param mContext
     */
    public static void toCtripKeyword(Context mContext) {
        Intent intent = new Intent(mContext, CtripKeywordActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 关键字 搜索
     *
     * @param mContext
     * @param keyword  搜索关键字
     */
    public static void toCtripKeywordBySearch(Context mContext, @Nullable String keyword) {
        Intent intent = new Intent(mContext, CtripKeywordBySearchActivity.class);
        intent.putExtra("keyword", keyword);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 选择城市 搜索
     *
     * @param mContext
     */
    public static void toCtripSiteCityBySearch(Context mContext) {
        Intent intent = new Intent(mContext, CtripSiteCityBySearchActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 选择区县
     *
     * @param mContext
     */
    public static void toCtripSiteCityByDistricts(Context mContext, String cityId, String cityName) {
        Intent intent = new Intent(mContext, CtripSiteCityByDistrictsActivity.class);
        intent.putExtra("cityId", cityId);
        intent.putExtra("cityName", cityName);
        mContext.startActivity(intent);
    }

    /**
     * 酒店预订订单提交
     *
     * @param mContext
     * @param bookRoomInfo
     */
    public static void toCtripBookRoomActivity(Context mContext, BookRoomInfo bookRoomInfo) {
        Intent intent = new Intent(mContext, CtripCommitOrderActivity.class);
        intent.putExtra(CtripCommitOrderActivity.TAG_ROOM_INFO, bookRoomInfo);
        mContext.startActivity(intent);

    }

    /**
     * 携程 收银台
     *
     * @param mContext
     */
    public static void toCtripOrderPayment(Context mContext, CtripCommitOrderEntity ctripCommitOrderEntity) {
        Intent intent = new Intent(mContext, CtripOrderPaymentActivity.class);
        intent.putExtra(CtripOrderPaymentActivity.TAG, ctripCommitOrderEntity);
        mContext.startActivity(intent);
    }

    /**
     * 携程 付款成功结果
     *
     * @param mContext
     */
    public static void toCtripOrderPaySuccess(Context mContext, CtripPayInfoEntity ctripPayInfoEntity) {
        Intent intent = new Intent(mContext, CtripOrderPaySuccessActivity.class);
        intent.putExtra("ctripPayInfoEntity", ctripPayInfoEntity);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 订单详情
     *
     * @param mContext
     */
    public static void toCtripOrderDetails(Context mContext, String ResIDValue) {
        Intent intent = new Intent(mContext, CtripOrderDetailsActivity.class);
        intent.putExtra("ResIDValue", ResIDValue);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 订单进度
     *
     * @param mContext
     */
    public static void toCtripOrderProgress(Context mContext, List<CtripOrderDetailEntity.Process> processList) {
        Intent intent = new Intent(mContext, CtripOrderProgressActivity.class);
        intent.putExtra("processList", (Serializable) processList);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 入住凭证
     *
     * @param mContext
     */
    public static void toCtripVoucher(Context mContext, CtripOrderDetailEntity.DataBean orderData) {
        Intent intent = new Intent(mContext, CtripVoucherActivity.class);
        intent.putExtra("orderData", (Serializable) orderData);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 取消订单
     *
     * @param mContext
     */
    public static void toCtripOrderCancel(Context mContext, String InclusiveAmount, String ResIDValue, String hotelId,
                                          String CityCode, String gdLat, String gdLng, String dayInclusiveAmount) {
        Intent intent = new Intent(mContext, CtripOrderCancelActivity.class);
        intent.putExtra("InclusiveAmount", InclusiveAmount);
        intent.putExtra("ResIDValue", ResIDValue);
        intent.putExtra("hotelId", hotelId);

        intent.putExtra("CityCode", CityCode);
        intent.putExtra("gdLat", gdLat);
        intent.putExtra("gdLng", gdLng);
        intent.putExtra("dayInclusiveAmount", dayInclusiveAmount);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 取消订单 成功页面
     *
     * @param mContext
     */
    public static void toCtripOrderCancelSuccessful(Context mContext, String hotelId,
                                                    String CityCode, String gdLat, String gdLng, String dayInclusiveAmount) {
        Intent intent = new Intent(mContext, CtripOrderCancelSuccessfulActivity.class);
        intent.putExtra("hotelId", hotelId);

        intent.putExtra("CityCode", CityCode);
        intent.putExtra("gdLat", gdLat);
        intent.putExtra("gdLng", gdLng);
        intent.putExtra("dayInclusiveAmount", dayInclusiveAmount);
        mContext.startActivity(intent);
    }


    /**
     * 跳转 订单分享
     *
     * @param mContext
     */
    public static void toCtripOrderShare(Context mContext, CtripOrderShareEntity ctripOrderShare) {
        Intent intent = new Intent(mContext, CtripOrderShareActivity.class);
        intent.putExtra("shareData", ctripOrderShare);
        mContext.startActivity(intent);
    }

    /**
     * 跳转酒店详情
     *
     * @param mContext
     * @param hotelId
     * @param checkIn  入住时间 2018-09-09
     * @param checkOut 离店时间 2018-09-11
     */
    public static void toCtripHotelDetail(Context mContext, String hotelId, @Nullable String checkIn, @Nullable String checkOut) {
        Intent intent = new Intent(mContext, CtripHotelDetailActivity.class);
        intent.putExtra(CtripHotelDetailActivity.TAG_HOTEL_ID, hotelId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (TextUtils.isEmpty(checkIn)) {
            checkIn = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        }
        intent.putExtra("checkIn", checkIn);

        if (TextUtils.isEmpty(checkOut)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            checkOut = simpleDateFormat.format(calendar.getTime());
        }
        intent.putExtra("checkOut", checkOut);
        mContext.startActivity(intent);
    }
}
