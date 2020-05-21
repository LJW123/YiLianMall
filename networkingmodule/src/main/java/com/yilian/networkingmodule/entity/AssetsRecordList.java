package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/6/8 0008.
 */
public class AssetsRecordList extends BaseEntity {
   @SerializedName("list")
    public ArrayList<AssetsList> lists;
    @SerializedName("income")
    public String income;//获得的数据
    @SerializedName("expend")
    public String expend;//扣除

    public class AssetsList {
        @SerializedName("id")
        public String id;//记录编号
        @SerializedName("type")
        public String type;//变更类型(以数据库变更类型json文件为准)
        @SerializedName("pay_count")
        public String payCount;//变更数额
        @SerializedName("pay_time")
        public String payTime;//变更时间
        @SerializedName("merchant_uid")
        public String merchantUid;//
        @SerializedName("type_msg")
        public String typeMsg; //变更类型对应的msg》以数据库变更类型json文件为准
        @SerializedName("year")
        public String year;//年
        @SerializedName("month")
        public String month;//月
        @SerializedName("balance_status")
        public String balanceStatus;//领奖励的状态
        @SerializedName("balance_text")
        public String balanceText;//状态对应的文本
        @SerializedName("refuse_text")
        public String refuseText;//如果已驳回 驳回的原因
    }
}
