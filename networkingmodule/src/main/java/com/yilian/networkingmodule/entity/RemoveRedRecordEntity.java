package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/11/23 0023.
 */

public class RemoveRedRecordEntity extends HttpResultBean {

    @SerializedName("data")
    public ArrayList<DataBean> data;

    public class DataBean {
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;
        @SerializedName("amount")
        public String amount;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("apply_at")
        public String apply_at;
    }

}
