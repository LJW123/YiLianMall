package com.yilian.mall.entity;

import android.support.annotation.IntDef;

import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * @author xiaofo on 2018/9/25.
 */

public class BookRoomInfo implements Serializable {
    /**
     * 钟点房
     */
    public static final int ROOM_HOURLY = 1;
    /**
     * 普通 房型
     */
    public static final int ROOM_ORDINARY = 0;


    public String hotelCode;
    public String hotelId;
    public String roomId;
    public String hotelName;

    /**
     * 房型
     */
    public String roomType;
    /**
     * 每间最多入住人数
     */
    public int maxPeoplePerRoom;
    public String startDate;
    public String endDate;
    /**
     * 入住天数
     */
    public int dateArea;


    public String bedName;
    // 0 无wifi  1、2 全部房间WIFI  3、4 部分房间WIFI
    public int wirelessBroadnet;
    public int breakfast;

    public int maxRoomCount;
    public String hotelPhone;
    public String roomDailyPrice;
    public String returnDailyBean;
    /**
     * 最晚到店时间
     */
    public String latestTime;
    /**
     * 是否必须遵循 到店时间
     */
    public String isMustBe;



    /**
     * 0普通 1钟点房
     */
    private int isHourlyRoom;

    public int getIsHourlyRoom() {
        return isHourlyRoom;
    }

    public void setIsHourlyRoom(@isHourlyRoom int isHourlyRoom) {
        this.isHourlyRoom = isHourlyRoom;
    }

    /**
     * 可选预计到店时间
     */
//    public  ArrayList<CtripHotelDetailEntity.DataBean.ArrivalOperationsBean> arrivalOperationsBeans;



    @IntDef({ROOM_ORDINARY, ROOM_HOURLY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface isHourlyRoom {}
}