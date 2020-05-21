package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;

/**
 * @author Created by  on 2018/5/25.
 *         收货地址信息  地址+联系人 信息
 */

public class JDShippingAddressInfoEntity extends HttpResultBean {

    /**
     * data : {"id":"3","provinceId":"11","province":"河南","cityId":"149","city":"郑州","countyId":"1254","county":"管城","townId":"1213","town":"城区","name":"李楠","mobile":"156543121","address":"是大法师说的","time":"0","full_address":"省电费阿士大夫省电费","consumer":"6533477898590904","default_address":"0"}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean implements Serializable{
        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", provinceId='" + provinceId + '\'' +
                    ", province='" + province + '\'' +
                    ", cityId='" + cityId + '\'' +
                    ", city='" + city + '\'' +
                    ", countyId='" + countyId + '\'' +
                    ", county='" + county + '\'' +
                    ", townId='" + townId + '\'' +
                    ", town='" + town + '\'' +
                    ", name='" + name + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", detailAddress='" + detailAddress + '\'' +
                    ", time='" + time + '\'' +
                    ", fullAddress='" + fullAddress + '\'' +
                    ", consumer='" + consumer + '\'' +
                    ", defaultAddress=" + defaultAddress +
                    '}';
        }

        /**
         * id : 3
         * provinceId : 11
         * province : 河南
         * cityId : 149
         * city : 郑州
         * countyId : 1254
         * county : 管城
         * townId : 1213
         * town : 城区
         * name : 李楠
         * mobile : 156543121
         * address : 是大法师说的
         * time : 0
         * full_address : 省电费阿士大夫省电费
         * consumer : 6533477898590904
         * default_address : 0
         */

        @SerializedName("id")
        public String id;
        @SerializedName("provinceId")
        public String provinceId;
        @SerializedName("province")
        public String province;
        @SerializedName("cityId")
        public String cityId;
        @SerializedName("city")
        public String city;
        @SerializedName("countyId")
        public String countyId;
        @SerializedName("county")
        public String county;
        @SerializedName("townId")
        public String townId;
        @SerializedName("town")
        public String town;
        @SerializedName("name")
        public String name;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("address")
        public String detailAddress;
        @SerializedName("time")
        public String time;
        @SerializedName("full_address")
        public String fullAddress;
        @SerializedName("consumer")
        public String consumer;
        /**
         * 1默认地址 0不是默认地址
         */
        @SerializedName("default_address")
        public int  defaultAddress;
        public static final int DEFAULT_JD_ADDRESS=1;
        public static final int UN_DEFAULT_JD_ADDRESS=0;
    }
}
