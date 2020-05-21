package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by liuyuqi on 2017/8/17 0017.
 * 商家认证资料entity
 */
public class MerchantAuthInfoEntity extends HttpResultBean {

    /**
     * code :
     * data : {"card_index":"","user_id":"","logo":"","license":"","type":"","card_id":"","card_name":"","card_front":"","card_reverse":"","card_person":"","card_status":"","card_submit_time":"","card_pass_time":"","employee_id":"","refund_reason":"","update_time":"","update_employee":""}
     */
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * card_index :
         * user_id :
         * logo :
         * license :
         * type :
         * card_id :
         * card_name :
         * card_front :
         * card_reverse :
         * card_person :
         * card_status :
         * card_submit_time :
         * card_pass_time :
         * employee_id :
         * refund_reason :
         * update_time :
         * update_employee :
         */

        @SerializedName("card_index")
        public String cardIndex;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("logo")
        public String logo;
        @SerializedName("license")
        public String license;
        @SerializedName("type")
        public String type;
        @SerializedName("card_id")
        public String cardId;
        @SerializedName("card_name")
        public String cardName;
        @SerializedName("card_front")
        public String cardFront;
        @SerializedName("card_reverse")
        public String cardReverse;
        @SerializedName("card_person")
        public String cardPerson;
        @SerializedName("card_status")
        public String cardStatus;
        @SerializedName("card_submit_time")
        public String cardSubmitTime;
        @SerializedName("card_pass_time")
        public String cardPassTime;
        @SerializedName("employee_id")
        public String employeeId;
        @SerializedName("refund_reason")
        public String refundReason;
        @SerializedName("update_time")
        public String updateTime;
        @SerializedName("update_employee")
        public String updateEmployee;
    }
}
