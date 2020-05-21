package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class PublicCardDetailsEntity extends BaseEntity {


    @SerializedName("data")
    public PublicCardDetailsEntity.DataBean data;

    public class DataBean {
        @SerializedName("card_index")
        public String card_index;

        @SerializedName("card_bank")
        public String card_bank;

        @SerializedName("card_number")
        public String card_number;

        @SerializedName("card_number_r4")
        public String card_number_r4;

        @SerializedName("card_type")
        public String card_type;

        @SerializedName("card_type_cn")
        public String card_type_cn;

        @SerializedName("bank_logo")
        public String bank_logo;

        @SerializedName("account_type")
        public String account_type;

        @SerializedName("card_holder")
        public String card_holder;

        @SerializedName("obligate_phone")
        public String obligate_phone;

        @SerializedName("tax_code")
        public String tax_code;

        @SerializedName("branch_bank")
        public String branch_bank;

        @SerializedName("company_address")
        public String company_address;

        @SerializedName("cert_image")
        public String cert_image;

        @SerializedName("province_cn")
        public String province_cn;

        @SerializedName("city_cn")
        public String city_cn;

    }
}
