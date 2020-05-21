package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class PublicCardDetailsEntityV2 extends BaseEntity {
    /**
     * data : {"card_index":"187","user_id":"7612281891453117","bank_code":"103100000026","bank_full_name":"中国农业银行资金清算中心","card_bank":"中国农业银行","card_holder":"河南乐善网络科技有限公司","card_number":"6228480738556860273","branch_bank":"洛阳西工区","card_type":"0","obligate_phone":"13460228771","account_type":"1","tax_code":"152362697786646464","province":"11","city":"150","company_address":"洛阳西工区","cert_image":"app/mall/qualifications/20180706/7612281891453117_2863013_1530850817161environment.jpg","card_time":"1530850836","last_withdraw_time":"1530859144","upd_time":"0","status":"0","bank_logo":"/app/images/bank/20170617/农业银行.png","card_type_cn":"储蓄卡","card_number_r4":"0273","bank_id":"5","province_cn":"河南","city_cn":"洛阳"}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * card_index : 187
         * user_id : 7612281891453117
         * bank_code : 103100000026
         * bank_full_name : 中国农业银行资金清算中心
         * card_bank : 中国农业银行
         * card_holder : 河南乐善网络科技有限公司
         * card_number : 6228480738556860273
         * branch_bank : 洛阳西工区
         * card_type : 0
         * obligate_phone : 13460228771
         * account_type : 1
         * tax_code : 152362697786646464
         * province : 11
         * city : 150
         * company_address : 洛阳西工区
         * cert_image : app/mall/qualifications/20180706/7612281891453117_2863013_1530850817161environment.jpg
         * card_time : 1530850836
         * last_withdraw_time : 1530859144
         * upd_time : 0
         * status : 0
         * bank_logo : /app/images/bank/20170617/农业银行.png
         * card_type_cn : 储蓄卡
         * card_number_r4 : 0273
         * bank_id : 5
         * province_cn : 河南
         * city_cn : 洛阳
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
        @SerializedName("bank_logo")
        public String bankLogo;
        @SerializedName("card_type_cn")
        public String cardTypeCn;
        @SerializedName("card_number_r4")
        public String cardNumberR4;
        @SerializedName("bank_id")
        public String bankId;
        @SerializedName("province_cn")
        public String provinceCn;
        @SerializedName("city_cn")
        public String cityCn;
    }


//    @SerializedName("data")
//    public PublicCardDetailsEntityV2.DataBean data;
//
//    public class DataBean {
//        @SerializedName("card_index")
//        public String card_index;
//
//        @SerializedName("card_bank")
//        public String card_bank;
//
//        @SerializedName("card_number")
//        public String card_number;
//
//        @SerializedName("card_number_r4")
//        public String card_number_r4;
//
//        @SerializedName("card_type")
//        public String card_type;
//
//        @SerializedName("card_type_cn")
//        public String card_type_cn;
//
//        @SerializedName("bank_logo")
//        public String bank_logo;
//
//        @SerializedName("account_type")
//        public String account_type;
//
//        @SerializedName("card_holder")
//        public String card_holder;
//
//        @SerializedName("obligate_phone")
//        public String obligate_phone;
//
//        @SerializedName("tax_code")
//        public String tax_code;
//
//        @SerializedName("branch_bank")
//        public String branch_bank;
//
//        @SerializedName("company_address")
//        public String company_address;
//
//        @SerializedName("cert_image")
//        public String cert_image;
//
//        @SerializedName("province_cn")
//        public String province_cn;
//
//        @SerializedName("city_cn")
//        public String city_cn;
//        @SerializedName("card_holder")
//        public String cardHolder;
//
//    }


}
