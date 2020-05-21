package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaofo on 2018/7/18.
 */

public class SnShippingAddressInfoEntity extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean implements Serializable {
        public static final int DEFAULT_ADDRESS=1;
        public boolean isChecked;
        /**
         * id : 36
         * provinceId : 16
         * province : 福建
         * cityId : 1303
         * city : 福州市
         * countyId : 48714
         * county : 马尾区
         * townId : 48753
         * town : 琅岐镇
         * name : 123
         * mobile : 18237234567
         * address : dfdfgfgfdgdg
         * time : 1527490776
         * full_address : 福建福州市马尾区琅岐镇dfdfgfgfdgdg
         * consumer : 7381187197860307
         * default_address : 1
         * isDel : 0
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
        public String address;
        @SerializedName("time")
        public String time;
        @SerializedName("full_address")
        public String fullAddress;
        @SerializedName("consumer")
        public String consumer;
        @SerializedName("default_address")
        public int defaultAddress;
        @SerializedName("isDel")
        public String isDel;
    }
}
