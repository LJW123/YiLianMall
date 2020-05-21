package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

public class BankInfoEntity extends BaseEntity{


    /**
     * info : {"bank":"交通银行","bank_id":"301290000007","info":"","kefu":"95559","logo":"http://apiserver.qiniudn.com/jiaotong.png","nature":"借记卡","service_phone":"95559","type":"太平洋薪金卡"}
     */

    @SerializedName("info")
    public InfoBean info;

    public static class InfoBean {
        /**
         * bank : 交通银行
         * bank_id : 301290000007
         * info :
         * kefu : 95559
         * logo : http://apiserver.qiniudn.com/jiaotong.png
         * nature : 借记卡
         * service_phone : 95559
         * type : 太平洋薪金卡
         */

        @SerializedName("bank")
        public String bank;
        @SerializedName("bank_id")
        public String bankId;
        @SerializedName("info")
        public String info;
        @SerializedName("kefu")
        public String kefu;
        @SerializedName("logo")
        public String logo;
        @SerializedName("nature")
        public String nature;
        @SerializedName("service_phone")
        public String servicePhone;
        @SerializedName("type")
        public String type;
    }
}
