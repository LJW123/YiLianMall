package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/1/19.
 */

public class BankCardEntity extends HttpResultBean {

    /**
     * info : {"bank":"中国建设银行","type":"龙卡储蓄卡","nature":"借记卡","kefu":"95533","logo":"http://apiserver.qiniudn.com/jianshe.png","info":"河南省-郑州","service_phone":"","bank_id":""}
     */

    @SerializedName("info")
    public BankCardInfoBean info;

    public static class BankCardInfoBean {
        /**
         * bank : 中国建设银行
         * type : 龙卡储蓄卡
         * nature : 借记卡
         * kefu : 95533
         * logo : http://apiserver.qiniudn.com/jianshe.png
         * info : 河南省-郑州
         * service_phone :
         * bank_id :
         */

        @SerializedName("bank")
        public String bank;
        @SerializedName("type")
        public String type;
        @SerializedName("nature")
        public String nature;
        @SerializedName("kefu")
        public String kefu;
        @SerializedName("logo")
        public String logo;
        @SerializedName("info")
        public String info;
        @SerializedName("service_phone")
        public String servicePhone;
        @SerializedName("bank_id")
        public String bankId;
    }
}
