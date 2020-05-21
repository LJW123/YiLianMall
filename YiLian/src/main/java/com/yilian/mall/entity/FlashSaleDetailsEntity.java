package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuyuqi on 2017/3/24 0024.
 */
public class FlashSaleDetailsEntity extends BaseEntity implements Serializable {

    /**
     * data : {"info":{"goods_id":"","goods_name":"","albums":["","",""],"price":"","introduction":"","goods_num":"","albums_details":["","",""],"filiale_id":"","merchant_id":"","merchant_latitude":"","merchant_longitude":"","merchant_address":"","merchant_name":"","merchant_tel":"","notice":"","buy_time":"","time":""}}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean implements Serializable {
        /**
         * info : {"goods_id":"","goods_name":"","albums":["","",""],"price":"","introduction":"","goods_num":"","albums_details":["","",""],"filiale_id":"","merchant_id":"","merchant_latitude":"","merchant_longitude":"","merchant_address":"","merchant_name":"","merchant_tel":"","notice":"","buy_time":"","time":""}
         */

        @SerializedName("info")
        public InfoBean info;

        public static class InfoBean implements Serializable {
            /**
             * goods_id :
             * goods_name :
             * albums : ["","",""]
             * price :
             * introduction :
             * goods_num :
             * albums_details : ["","",""]
             * filiale_id :
             * merchant_id :
             * merchant_latitude :
             * merchant_longitude :
             * merchant_address :
             * merchant_name :
             * merchant_tel :
             * notice :
             * buy_time :
             * time :
             */

            @SerializedName("goods_id")
            public String goodsId;
            @SerializedName("goods_name")
            public String goodsName;
            @SerializedName("goods_type")
            public String goodsType;//商品的类型 1快递配送，2店内消费
            @SerializedName("fregiht_price")//运费
            public String fregihtPrice;
            @SerializedName("price")
            public String price;
            @SerializedName("introduction")
            public String introduction;
            @SerializedName("goods_num")
            public String goodsNum;
            @SerializedName("filiale_id")
            public String filialeId;
            @SerializedName("merchant_id")
            public String merchantId;
            @SerializedName("merchant_latitude")
            public String merchantLatitude;
            @SerializedName("merchant_longitude")
            public String merchantLongitude;
            @SerializedName("merchant_address")
            public String merchantAddress;
            @SerializedName("merchant_name")
            public String merchantName;
            @SerializedName("merchant_tel")
            public String merchantTel;
            @SerializedName("notice")
            public String notice;
            @SerializedName("buy_time")
            public String buyTime;
            @SerializedName("time")
            public String time;
            @SerializedName("albums")
            public List<String> albums;
            @SerializedName("albums_details")
            public List<String> albumsDetails;
        }
    }
}
