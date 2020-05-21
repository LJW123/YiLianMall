package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;


/**
 * Created by ZYH on 2017/12/4 0004.
 */

public class StealMoreRedpackgs extends HttpResultBean {
    /**
     * data : {"list":[{"merchant_id":"","merchant_name":"","merchant_latitude":"","merchant_longitude":"","merchant_image":"","count":""},{}]}
     */
    @SerializedName("data")
    public List<RedPackgeDetails> data;

    public static class RedPackgeDetails {
        /**
         * merchant_id :
         * merchant_name :
         * merchant_latitude :
         * merchant_longitude :
         * merchant_image :
         * count :
         */
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("merchant_latitude")
        public String merchantLatitude;
        @SerializedName("merchant_longitude")
        public String merchantLongitude;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("count")
        public String count;
        @SerializedName("distance")
        public String distance;
        @SerializedName("merchant_address")
        public String  merchantAddress;
        @SerializedName("merchant_city")
        public String merchantCity;
    }

}
