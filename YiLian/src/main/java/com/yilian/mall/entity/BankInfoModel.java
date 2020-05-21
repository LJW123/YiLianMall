package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2017/8/9 0009.
 */

public class BankInfoModel extends BaseEntity {

    /**
     * phone : 18203660536
     * card : 1
     * card_info : {"card_index":"4","user_id":"6292344924947408","bank_code":"","card_bank":"招商银行","card_holder":"李剑威","card_number":"123456","branch_bank":"详细地址东城区支行","card_time":"1500344878","upd_time":"0","province":"2","city":"52","county":"500","region":{"province":{"id":"2","name":"北京"},"city":{"id":"52","name":"北京"},"county":{"id":"500","name":"东城区"}},"logo":"/app/images/bank/20170617/招商银行.png"}
     * available_cash : 0
     */

    @SerializedName("phone")
    public String phone;
    @SerializedName("card")
    public int card;
    @SerializedName("card_info")
    public CardInfoBean cardInfo;
    @SerializedName("available_cash")
    public String availableCash;

    public static class CardInfoBean {
        /**
         * card_index : 4
         * user_id : 6292344924947408
         * bank_code :
         * card_bank : 招商银行
         * card_holder : 李剑威
         * card_number : 123456
         * branch_bank : 详细地址东城区支行
         * card_time : 1500344878
         * upd_time : 0
         * province : 2
         * city : 52
         * county : 500
         * region : {"province":{"id":"2","name":"北京"},"city":{"id":"52","name":"北京"},"county":{"id":"500","name":"东城区"}}
         * logo : /app/images/bank/20170617/招商银行.png
         */

        @SerializedName("card_index")
        public String cardIndex;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("bank_code")
        public String bankCode;
        @SerializedName("card_bank")
        public String cardBank;
        @SerializedName("card_holder")
        public String cardHolder;
        @SerializedName("card_number")
        public String cardNumber;
        @SerializedName("branch_bank")
        public String branchBank;
        @SerializedName("card_time")
        public String cardTime;
        @SerializedName("upd_time")
        public String updTime;
        @SerializedName("province")
        public String province;
        @SerializedName("city")
        public String city;
        @SerializedName("county")
        public String county;
        @SerializedName("region")
        public RegionBean region;
        @SerializedName("logo")
        public String logo;

        public static class RegionBean {
            /**
             * province : {"id":"2","name":"北京"}
             * city : {"id":"52","name":"北京"}
             * county : {"id":"500","name":"东城区"}
             */

            @SerializedName("province")
            public ProvinceBean province;
            @SerializedName("city")
            public CityBean city;
            @SerializedName("county")
            public CountyBean county;

            public static class ProvinceBean {
                /**
                 * id : 2
                 * name : 北京
                 */

                @SerializedName("id")
                public String id;
                @SerializedName("name")
                public String name;
            }

            public static class CityBean {
                /**
                 * id : 52
                 * name : 北京
                 */

                @SerializedName("id")
                public String id;
                @SerializedName("name")
                public String name;
            }

            public static class CountyBean {
                /**
                 * id : 500
                 * name : 东城区
                 */

                @SerializedName("id")
                public String id;
                @SerializedName("name")
                public String name;
            }
        }
    }
}
