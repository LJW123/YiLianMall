package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author xiaofo on 2018/7/24.
 */

public class SnSelectorAddressData extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 010
         * pId : 010
         * level : 1
         * name : 北京
         * snId : 1111
         */

        @SerializedName("id")
        public String id;
        @SerializedName("pId")
        public String pId;
        @SerializedName("level")
        public String level;
        @SerializedName("name")
        public String name;
        @SerializedName("snId")
        public String snId;
    }
}
