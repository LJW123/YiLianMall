package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2017/5/19 0019.
 */
public class SpellGroupShareListEntity extends BaseEntity {

    /**
     * data : {"activity_id":"1","confirm_time":0,"desc_tags":["精选品牌","品质保障","官方售后","48小时发货"],"done_group_time":"0","end_time":1496378958,"express_company":0,"express_info":0,"express_num":0,"express_time":0,"goods_name":"测试拼团","group_condition":"2","group_status":"1","num":1,"order_id":"2017060211491743860","parcel_index":0,"parcel_status":0,"time":1496384929,"time_fail":1496378958,"user_icon":["http://wx.qlogo.cn/mmopen/Q3auHgzwzM5hibicg6icKQ7A5EyCKlNcrX7k2Gxa1YxrhnkRG8EibtficMkwLUmFRFKYdPPsKQdORmFtFnkWjh7UeODCicb8pXtrntVoRJh0YicLXo/0",""]}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * activity_id : 1
         * confirm_time : 0
         * desc_tags : ["精选品牌","品质保障","官方售后","48小时发货"]
         * done_group_time : 0
         * end_time : 1496378958
         * express_company : 0
         * express_info : 0
         * express_num : 0
         * express_time : 0
         * goods_name : 测试拼团
         * group_condition : 2
         * group_status : 1
         * num : 1
         * order_id : 2017060211491743860
         * parcel_index : 0
         * parcel_status : 0
         * time : 1496384929
         * time_fail : 1496378958
         * user_icon : ["http://wx.qlogo.cn/mmopen/Q3auHgzwzM5hibicg6icKQ7A5EyCKlNcrX7k2Gxa1YxrhnkRG8EibtficMkwLUmFRFKYdPPsKQdORmFtFnkWjh7UeODCicb8pXtrntVoRJh0YicLXo/0",""]
         */

        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("confirm_time")
        public int confirmTime;
        @SerializedName("done_group_time")
        public String doneGroupTime;
        @SerializedName("end_time")
        public String endTime;
        @SerializedName("express_company")
        public String expressCompany;
        @SerializedName("express_info")
        public String expressInfo;
        @SerializedName("express_num")
        public String expressNum;
        @SerializedName("express_time")
        public String expressTime;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("group_condition")
        public String groupCondition;
        @SerializedName("group_status")
        public String groupStatus;
        @SerializedName("num")
        public String num;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("parcel_index")
        public int parcelIndex;
        @SerializedName("parcel_status")
        public int parcelStatus;
        @SerializedName("time")
        public String time;
        @SerializedName("time_fail")
        public String timeFail;
        @SerializedName("desc_tags")
        public List<String> descTags;
        @SerializedName("user_icon")
        public List<String> userIcon;
        @SerializedName("open_group_time")
        public String openGroupTime;
    }
}
