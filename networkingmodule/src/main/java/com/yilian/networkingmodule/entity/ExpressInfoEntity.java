package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * 获取快递信息实体类
 *
 * @author Ray_L_Pain
 * @date 2017/12/4 0004
 */

public class ExpressInfoEntity extends HttpResultBean {


    @SerializedName("info")
    public InfoBean info;

    public class InfoBean {
        @SerializedName("deliv")
        public String deliv;
        @SerializedName("express_name")
        public String express_name;
    }

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        @SerializedName("time")
        public String time;
        @SerializedName("status")
        public String status;
    }

}
