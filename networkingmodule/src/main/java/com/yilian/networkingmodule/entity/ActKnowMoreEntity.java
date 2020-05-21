package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Created by  on 2017/11/22 0022.
 */

public class ActKnowMoreEntity extends HttpResultBean implements Serializable {

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean implements Serializable {
        /**
         * title : 签到是什么？
         * content : 每天可签到1次，获得1奖券奖励。连续签到7天，累计的7奖券一次性到账并激活，参与每天的分红。您可坚持连续签到，持续获得奖券奖励哦！
         */

        @SerializedName("title")
        public String title;
        @SerializedName("content")
        public String content;

        @Override
        public String toString() {
            return "ListBean{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
