package com.yilian.networkingmodule.entity.ctrip;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.entity.ctrip.ctripMultiItem.CtripOrderListLayoutType;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 订单实体类
 *
 * @author Created by Zg on 2018/7/25.
 */

public class CtripOrderListEntity extends HttpResultBean {


    @SerializedName("list")
    public List<OrderList> list;


    public static class HeadBean implements MultiItemEntity {
        public String headDate;

        public HeadBean(String headDate) {
            this.headDate = headDate;
        }

        @Override
        public int getItemType() {
            return CtripOrderListLayoutType.header;
        }
    }


    public static class OrderList implements MultiItemEntity {

        /**
         * {
         * "id": "2",
         * "hotelId": "374778", 酒店id
         * "RoomID": "96844496", 售卖房型id
         * "InclusiveAmount": "2209.00", 税后价
         * "CancelAmount": "2209.00", 取消罚金
         * "CancelStart": "2015-12-31 12:00:00", 最晚免费取 消时间
         * "CancelEnd": "2015-12-31 12:00:00", 取消政策的失效时间
         * "ResID_Value": "3064577902", 携程订单号
         * "CreateDateTime": "2018-09-25T16:54:21.1823019+08:00", 订单生成时间
         * "ReturnBean": "30", 赠送乐豆
         * "status": "Uncommitted", 订单状态: Uncommitted-订单未提交; Process-确认中; Confirm-已确认; Cancel-已取消; Success-成交;
         * "user_id": "6300277240877444", 订单所属用户
         * "users": "哈哈哈", 入住人 多个逗号分隔
         * "phone": "13525661721", 联系电话
         * "paymentTime": "0", 支付时间
         * "paymentIndex": "0", recharge_order的id
         * "paymentType": "0", 支付方式  0余额  1三方  2混合
         * "CancelReason": null, 订单取消原因
         * "TimeSpanStart": "2018-09-25", 入住开始时间
         * "TimeSpanEnd": "2018-09-26", 入住结束时间
         * "NumberOfUnits": "1", 预定房间数
         * "nightNum": "1", 入住晚数
         * "HotelName": "北京富力万丽酒店", 酒店名字
         * "Address": "东三环中路61号" 酒店地址
         * "image": "", 酒店主图
         * "MinPrice": "", 酒店房型起价
         * "CtripUserRating": "", 用户评分
         * }
         */

        @SerializedName("id")
        public String id;
        @SerializedName("hotelId")
        public String hotelId;
        @SerializedName("RoomID")
        public String RoomID;
        @SerializedName("InclusiveAmount")
        public String InclusiveAmount;
        @SerializedName("CancelAmount")
        public String CancelAmount;
        @SerializedName("CancelStart")
        public String CancelStart;
        @SerializedName("CancelEnd")
        public String CancelEnd;
        @SerializedName("ResID_Value")
        public String ResIDValue;
        @SerializedName("CreateDateTime")
        public long CreateDateTime;
        @SerializedName("ReturnBean")
        public String ReturnBean;
        @SerializedName("status")
        public String status;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("users")
        public String users;
        @SerializedName("ContactName")
        public String contactName;
        @SerializedName("RoomName")
        public String roomName;
        @SerializedName("bedName")
        public String bedName;

        @SerializedName("netMsg")
        public String netMsg;
        @SerializedName("Breakfast")
        public int breakfast;

        @SerializedName("phone")
        public String phone;
        @SerializedName("paymentTime")
        public String paymentTime;
        @SerializedName("paymentIndex")
        public String paymentIndex;
        @SerializedName("paymentType")
        public String paymentType;
        @SerializedName("CancelReason")
        public Object CancelReason;
        @SerializedName("TimeSpanStart")
        public String TimeSpanStart;
        @SerializedName("TimeSpanEnd")
        public String TimeSpanEnd;
        @SerializedName("NumberOfUnits")
        public String NumberOfUnits;
        @SerializedName("nightNum")
        public String nightNum;
        @SerializedName("HotelName")
        public String HotelName;
        @SerializedName("Address")
        public String Address;
        @SerializedName("image")
        public String image;
        @SerializedName("MinPrice")
        public String MinPrice;
        @SerializedName("CtripUserRating")
        public String CtripUserRating;

        @Override
        public int getItemType() {
            return CtripOrderListLayoutType.content;
        }


    }
}
