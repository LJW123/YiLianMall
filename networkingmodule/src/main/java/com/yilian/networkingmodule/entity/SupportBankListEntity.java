package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/1/21.
 */

public class SupportBankListEntity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * bank_name : 中国银行
         * bank_logo :
         */

        @SerializedName("bank_name")
        public String bankName;
        @SerializedName("bank_logo")
        public String bankLogo;
    }
}
