package com.yilian.mall.ctrip.mvp;


import android.content.Context;
import android.support.annotation.Nullable;

import com.yilian.networkingmodule.entity.Hotel_Rooms_FiltrateBean;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelFilterEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripRoomTypeInfo;

import java.util.ArrayList;

import rx.Subscription;

/**
 * @author xiaofo on 2018/8/31.
 */

public class CtripHotelDetailContract {
    public interface Presenter {
        Subscription getCtripHotelDetailData(Context context, String hotelId, String startDate, String endDate);
        Subscription getCtripHotelFilter(Context context);
        Subscription getFiltrateData(Context context,String HotelID,String Start,String End,String RoomBedInfos,String NumberOfBreakfast,String WirelessBroadnet,String WiredBroadnet,String minPrice,String maxPrice,String IsInstantConfirm,String CancelPolicyInfos );
    }

    public interface View extends BaseView {
        void showCtripHotelDetail(CtripHotelDetailEntity ctripHotelDetailEntity);
        void showCtripFilter(CtripHotelFilterEntity ctripHotelFilterEntity);
        void resetFilterList(Hotel_Rooms_FiltrateBean ctripRoomTypeInfo);
    }
}
