package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by @author zhaiyaohua on 2018/1/23 0023.
 */

public class CardPrivateDetailsInfo extends BaseEntity {


    /**
     * data : {"card_index":"106","user_id":"6533477898590904","bank_code":"102100099996","bank_full_name":"中国工商银行总行清算中心","card_bank":"中国工商银行","card_holder":"*光远","card_number":"6222021702033447285","branch_bank":"郑州东风路支行","card_type":"0","obligate_phone":"182****5864","account_type":"0","tax_code":"","province":"11","city":"149","company_address":"","cert_image":"","card_time":"1516700946","last_withdraw_time":"0","upd_time":"0","status":"0","card_type_cn":"储蓄卡","card_number_r4":"7285","province_cn":"河南","city_cn":"郑州","bank_logo":"/app/images/bank/20170617/工商银行.png"}
     */

    @SerializedName("data")
    public Data data;

    public static class Data {
        /**
         * card_index : 106
         * user_id : 6533477898590904
         * bank_code : 102100099996
         * bank_full_name : 中国工商银行总行清算中心
         * card_bank : 中国工商银行
         * card_holder : *光远
         * card_number : 6222021702033447285
         * branch_bank : 郑州东风路支行
         * card_type : 0
         * obligate_phone : 182****5864
         * account_type : 0
         * tax_code :
         * province : 11
         * city : 149
         * company_address :
         * cert_image :
         * card_time : 1516700946
         * last_withdraw_time : 0
         * upd_time : 0
         * status : 0
         * card_type_cn : 储蓄卡
         * card_number_r4 : 7285
         * province_cn : 河南
         * city_cn : 郑州
         * bank_logo : /app/images/bank/20170617/工商银行.png
         */

        @SerializedName("card_index")
        public String cardIndex;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("bank_code")
        public String bankCode;
        @SerializedName("bank_full_name")
        public String bankFullName;
        @SerializedName("card_bank")
        public String cardBank;
        @SerializedName("card_holder")
        public String cardHolder;
        @SerializedName("card_number")
        public String cardNumber;
        @SerializedName("branch_bank")
        public String branchBank;
        @SerializedName("card_type")
        public String cardType;
        @SerializedName("obligate_phone")
        public String obligatePhone;
        @SerializedName("account_type")
        public String accountType;
        @SerializedName("tax_code")
        public String taxCode;
        @SerializedName("province")
        public String province;
        @SerializedName("city")
        public String city;
        @SerializedName("company_address")
        public String companyAddress;
        @SerializedName("cert_image")
        public String certImage;
        @SerializedName("card_time")
        public String cardTime;
        @SerializedName("last_withdraw_time")
        public String lastWithdrawTime;
        @SerializedName("upd_time")
        public String updTime;
        @SerializedName("status")
        public String status;
        @SerializedName("card_type_cn")
        public String cardTypeCn;
        @SerializedName("card_number_r4")
        public String cardNumberR4;
        @SerializedName("province_cn")
        public String provinceCn;
        @SerializedName("city_cn")
        public String cityCn;
        @SerializedName("bank_logo")
        public String bankLogo;
    }
}
