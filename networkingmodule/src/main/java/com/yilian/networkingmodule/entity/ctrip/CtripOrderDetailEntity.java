package com.yilian.networkingmodule.entity.ctrip;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Created by Zg on 2018/10/13.
 */

public class CtripOrderDetailEntity extends HttpResultBean {


    @SerializedName("info")
    public DataBean data;


    public static class DataBean implements Serializable {
        /**
         * {
         * id: "14",//订单这边唯一标识
         * hotelId: "374778",//酒店ID
         * RoomID: "96844496",//售卖房型ID
         * InclusiveAmount: "8836.00",//税后订单总价格
         * CancelAmount: "8836.00",//取消订单罚金
         * CancelStart: "2016-01-17 12:00:00",//Start 定义了最晚免费取消时间。Start 时间之前，客人可免费取消；Start 之后，客人需承担相应罚金。
         * CancelEnd: "2016-01-17 12:00:00",
         * ResID_Value: "3064671493",//携程订单号
         * CreateDateTime: "1539273600",//创建订单时间
         * LateArrivalTime: "1539273600",//最晚到店时间
         * ReturnBean: "30",//赠送益豆数量
         * status: "Uncommitted",//当前订单的状态
         * user_id: "6300277240877444",//会员uid
         * users: "卡萨诺,缺钱",//入住人姓名
         * phone: "13525661721",//联系人电话
         * CancelReason: ‘’,//如果已取消 取消原因字段
         * cancelTime: ‘’,//取消时间
         * TimeSpanStart: "1539273600",//入住开始时间
         * TimeSpanEnd: "1539446400",//入住结束时间
         * NumberOfUnits: "2",//预订房间数
         * nightNum: "2",//入住晚数
         * confirmTime: "1539273600",//确认时间
         * paymentTime:"1539273600",//支付时间
         * checkInTime: "1539273600",//入住时间
         * hotelName: "北京富力万丽酒店",//酒店名称
         * hotelAddress: "东三环中路61号(近广渠路)",//酒店地址
         * hotelEmail: "",//酒店邮箱
         * gdLat: "39.89797",//纬度
         * gdLng: "116.460733",//经度
         * Telephone: "010-58638888",//电话
         * RoomName: "豪华双床房(双人入住)",//房型名称
         * RoomBedName: "多张床",//床名称
         * RoomInfo: {//售卖房型详情展示
         * AreaRange: "40-50",//面积
         * FloorRange: "5-20",//楼层
         * MaxOccupancy: "2",//入住人数
         * netMsg: "全部房间WI-FI(收费) 全部房间有线宽带(收费)",//宽带信息
         * bedName: "多张床1.3米"//床信息
         * },
         * process: [//支付状态详情
         * {
         * msg: "预订已成功，您可安心入住",
         * time: "1539273600"
         * },
         * {
         * msg: "您的酒店入住信息正发送至酒店，请等待酒店确认信息",
         * time: 1539273610
         * },
         * {
         * msg: "订单已提交，请稍后刷新订单状态",
         * time: "1539273600"
         * }
         * ],
         * cancel: 0,//0不可取消 1可取消
         * cancelMsg:"不可取消",//详情中显示取消政策
         * cancelMsgDetail:"不可取消", //弹窗显示取消政
         * orderType:"0",//订单类型 0普通房型订单 1钟点房订单
         * Breakfast: 1,//0 无早餐 1单份早餐 2双份早餐
         * dayInclusiveAmount: 2209,//每日售卖房型税后价
         * stayTimeLength: 30,//保留30分钟
         * serverTime: 1539400102,//当前服务器时间
         * deadTime: 1539312676,//未提交订单失效截止时间
         * isEffective:0 ,//是否有效 0有效 1无效 （携程未提交订单 系统保留30分钟之后置位无效）
         * returnMoney:'0'//取消已支付订单 退款金额
         * returnTime:'0'//退款时间
         * Descriptions:酒店描述
         * CtripUserRating:用户评分
         * cancelTime:"",//取消时间
         * CityCode:"",//城市
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
        @SerializedName("LateArrivalTime")
        public long LateArrivalTime;
        @SerializedName("ReturnBean")
        public String ReturnBean;
        @SerializedName("status")
        public String status;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("users")
        public String users;
        @SerializedName("ContactName")
        public String ContactName;
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
        public long TimeSpanStart;
        @SerializedName("TimeSpanEnd")
        public long TimeSpanEnd;
        @SerializedName("NumberOfUnits")
        public String NumberOfUnits;
        @SerializedName("invoiceState")
        public String invoiceState;
        @SerializedName("nightNum")
        public String nightNum;
        @SerializedName("confirmTime")
        public long confirmTime;
        @SerializedName("checkInTime")
        public long checkInTime;
        @SerializedName("isEffective")
        public String isEffective;
        @SerializedName("cancelTime")
        public long cancelTime;
        @SerializedName("CtripUserRating")
        public String CtripUserRating;
        @SerializedName("orderType")
        public String orderType;
        @SerializedName("hotelName")
        public String hotelName;
        @SerializedName("hotelAddress")
        public String hotelAddress;
        @SerializedName("hotelEmail")
        public String hotelEmail;
        @SerializedName("gdLat")
        public String gdLat;
        @SerializedName("gdLng")
        public String gdLng;
        @SerializedName("CityCode")
        public String CityCode;

        @SerializedName("Telephone")
        public String Telephone;
        @SerializedName("stayTimeLength")
        public int stayTimeLength;
        @SerializedName("serverTime")
        public int serverTime;
        @SerializedName("deadTime")
        public long deadTime;
        @SerializedName("RoomName")
        public String RoomName;
        @SerializedName("RoomBedName")
        public String RoomBedName;
        @SerializedName("RoomInfo")
        public RoomInfoBean RoomInfo;
        @SerializedName("cancel")
        public int cancel;
        @SerializedName("cancelMsg")
        public String cancelMsg;
        @SerializedName("cancelMsgDetail")
        public String cancelMsgDetail;
        @SerializedName("returnMoney")
        public String returnMoney;
        @SerializedName("returnTime")
        public long returnTime;

        @SerializedName("Breakfast")
        public int Breakfast;
        @SerializedName("dayInclusiveAmount")
        public String dayInclusiveAmount;
        @SerializedName("process")
        public List<Process> process;


    }

    public static class RoomInfoBean implements Serializable{
        /**
         * AreaRange : 38
         * FloorRange : 35-43
         * MaxOccupancy : 2
         * netMsg : 全部房间WI-FI(收费) 全部房间有线宽带(收费)
         * bedName : 大床1.8米
         */

        @SerializedName("AreaRange")
        public String AreaRange;
        @SerializedName("FloorRange")
        public String FloorRange;
        @SerializedName("MaxOccupancy")
        public String MaxOccupancy;
        @SerializedName("netMsg")
        public String netMsg;
        @SerializedName("bedName")
        public String bedName;
    }

    public static class Process implements Serializable {
        /**
         * msg : 预订已成功，您可安心入住
         * time : 1539273600
         */

        @SerializedName("msg")
        public String msg;
        @SerializedName("time")
        public long time;
    }
}
