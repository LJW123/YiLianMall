package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/9/6 0006.
 */

public class OfflineTransferCardInfoDatailEntity extends HttpResultBean {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("trans_id")
        public String transId;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("reason")
        public String reason;
        @SerializedName("trans_bank")
        public String transBank;
        @SerializedName("trans_card")
        public String transCard;
        @SerializedName("trans_name")
        public String transName;
        @SerializedName("phone")
        public String phone;
        @SerializedName("amount")
        public String amount;
        @SerializedName("trans_time")
        public String transTime;
        @SerializedName("trans_status")
        public String transStatus;
        @SerializedName("sub_time")
        public String subTime;
        @SerializedName("trans_vouchar")
        public String transVouchar;
        @SerializedName("remark")
        public String remark;
    }

}
