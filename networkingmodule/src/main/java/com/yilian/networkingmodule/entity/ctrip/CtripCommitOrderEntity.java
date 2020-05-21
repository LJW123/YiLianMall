package com.yilian.networkingmodule.entity.ctrip;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Zg on 2018/10/26.
 */

public class CtripCommitOrderEntity implements Serializable {


    /**
     * 订单自增id
     */
    public String orderIndex;

    /**
     * 益豆
     */
    public String returnBean;
    /**
     * 订单金额
     */
    public String orderPrice;
    /**
     * 携程订单号
     */
    public String ResID_Value;


    public String hotelName;
    public String hotelId;
    public String roomType;
    public String roomId;
    public  String bedName;
    public  String netMsg;
    public  int breakfast;

    /**
     * 入住时间 yyyy-MM-dd
     */
    public String checkIn;
    /**
     * 离店时间 yyyy-MM-dd
     */
    public String checkOut;
    public String duration;
    public String number;
    public String checkInPerson;
    public String linkman;
    public String phone;

}
