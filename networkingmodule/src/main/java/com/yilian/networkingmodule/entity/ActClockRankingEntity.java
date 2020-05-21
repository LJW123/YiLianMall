package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by ASUS on 2017/12/14 0014.
 */

public class ActClockRankingEntity extends HttpResultBean {
    @SerializedName("list")
    public java.util.List<Data> list;

    public static class Data {
        /**
         * name : 暂无昵称
         * photo : app/images/head/20171109/7435908194499807_9657332_.jpg
         * given_integral : 2000
         */
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;
        @SerializedName("given_integral")
        public String givenIntegral;
    }
}
