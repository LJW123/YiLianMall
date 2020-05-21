package com.yilian.networkingmodule.entity.shoppingcard;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @auther 马铁超 on 2018/11/19 11:23
 * 我的购物卡
 */

public class MyCardInfoEntity extends HttpResultBean{


    /**
     * data : {"card_id":"0001003","balance":"5000.00","limit":"416.67","created_at":"2018/11/19","name":"暂无昵称","isAddress":1,"address":""}
     */
   @SerializedName("data")
    public DataBean data;



    public static class DataBean {
        /**
         * card_id : 0001003
         * balance : 5000.00
         * limit : 416.67
         * created_at : 2018/11/19
         * name : 暂无昵称
         * isAddress : 1
         * address :
         */
        @SerializedName("card_id")
        public String card_id;
        @SerializedName("balance")
        public String balance;
        @SerializedName("limit")
        public String limit;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("name")
        public String name;
        @SerializedName("isAddress")
        public String isAddress;
        @SerializedName("address")
        public String address;
    }
}
