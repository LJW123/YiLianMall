package com.yilian.mall.entity;/**
 * Created by  on 2017/3/27 0027.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/3/27 0027.
 */
public class FlashSalePayResult extends BaseEntity {

    /**
     * data : {"paytime":"","endtime":"","activity":"","goods_name":"","address":"","tel":"","code_secret":"","voucher_index":"","filiale_name":""}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * paytime :
         * endtime :
         * activity :
         * goods_name :
         * address :
         * tel :
         * code_secret :
         * voucher_index :
         * filiale_name :
         */

        @SerializedName("paytime")
        public String paytime;
        @SerializedName("endtime")
        public String endtime;
        @SerializedName("activity")
        public String activity;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("address")
        public String address;
        @SerializedName("tel")
        public String tel;
        @SerializedName("code_secret")
        public String codeSecret;
        @SerializedName("voucher_index")
        public String voucherIndex;
        @SerializedName("filiale_name")
        public String filialeName;
    }
}
