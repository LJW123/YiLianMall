package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by  on 2017/6/16 0016.
 */

public class MerchantMoneyRecordEntity extends BaseEntity {
    public MerchantMoneyRecordEntity() {
    }

    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class DataBean {
        public boolean isVisible=false;
        /**
         * id :
         * order_id :
         * merchant_id :
         * merchant_type :
         * user_id :
         * merchant_cash :
         * cash :
         * status :
         * type :
         * deal_id :
         * time :
         * merchant_percent :
         * bonus :
         * agent_id :
         * type_name :
         * user_name :
         * user_phone :
         * user_integral :
         * merchant_integral :
         */

        @SerializedName("id")
        public String id;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_type")
        public String merchantType;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("merchant_cash")
        public String merchantCash;//实际领奖励
        @SerializedName("cash")
        public String cash;//交易金额
        @SerializedName("status")
        public String status;
        @SerializedName("type")
        public String type;
        @SerializedName("deal_id")
        public String dealId;
        @SerializedName("time")
        public String time;//交易时间
        @SerializedName("merchant_percent")
        public String merchantPercent;
        @SerializedName("bonus")
        public String bonus;//让利额
        @SerializedName("agent_id")
        public String agentId;
        @SerializedName("type_name")
        public String typeName;//支付方式
        @SerializedName("user_name")
        public String userName;//用户名
        @SerializedName("user_phone")
        public String userPhone;//手机号
        @SerializedName("user_integral")
        public String userIntegral;//用户奖券
        @SerializedName("merchant_integral")
        public String merchantIntegral;//商家奖券
        @SerializedName("remark")
        public String remark;
        @SerializedName("happyBean")
        public String happyBean;
        @SerializedName("mer_happyBean")
        public String merHappyBean;
        @SerializedName("blag")
        public int blag;
    }
}
