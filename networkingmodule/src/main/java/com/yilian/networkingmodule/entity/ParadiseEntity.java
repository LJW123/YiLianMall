package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/10/23 0023.
 */

public class ParadiseEntity extends HttpResultBean {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("list")
        public ArrayList<ListBean> list;

        public class ListBean {
            @SerializedName("name")
            public String name;
            @SerializedName("type")
            public String type;
            @SerializedName("pic")
            public String pic;
            @SerializedName("urls")
            public String urls;
        }
    }

}
