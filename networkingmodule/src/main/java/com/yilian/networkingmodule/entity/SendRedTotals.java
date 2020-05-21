package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 *
 * @author ZYH
 * @date 2017/12/6 0006
 */

public class SendRedTotals extends HttpResultBean {


    /**
     * code : 1
     * data : [{"id":"","created_at":"","amount":"","unlock_amount":"","quantity":"","is_overdue":"","merchant_name":"","merchant_image":"","over_amount":""},{}]
     */

    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        /**
         * id :
         * created_at :
         * amount :
         * unlock_amount :
         * quantity :
         * is_overdue :
         * merchant_name :
         * merchant_image :
         * over_amount :
         */
        @SerializedName("id")
        public String id;
        @SerializedName("created_at")
        public String createdAt;
        @SerializedName("amount")
        public String amount;
        @SerializedName("unlock_amount")
        public String unlockAmount;
        @SerializedName("quantity")
        public String quantity;
        @SerializedName("is_overdue")
        public boolean isOverdue;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("over_amount")
        public String overAmount;
    }
}
