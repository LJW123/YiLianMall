package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by liuyuqi on 2017/6/21 0021.
 */
public class TransferIntegralEntity extends HttpResultBean {

    /**
     * message : 转增成功
     * data : {"order_id":"2017062108552656620","user_id":"6220847148743406","be_userid":"6179219566404401","amount":95,"fee":5}
     * request : GET /app/service.php?c=transfer_integral&be_phone=18613717571&amount=100&platform=1
     */

    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;
    @SerializedName("rquest")
    public String request;

    public static class DataBean {
        /**
         * order_id : 2017062108552656620
         * user_id : 6220847148743406
         * be_userid : 6179219566404401
         * amount : 95
         * fee : 5
         */

        @SerializedName("order_id")
        public String orderId;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("be_userid")
        public String beUserid;
        @SerializedName("amount")
        public String amount;
        @SerializedName("fee")
        public String fee;

    }
}
