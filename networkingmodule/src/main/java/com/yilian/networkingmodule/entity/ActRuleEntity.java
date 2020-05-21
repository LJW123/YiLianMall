package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/11/20 0020.
 */

public class ActRuleEntity extends HttpResultBean {

    @SerializedName("info")
    public InfoBean info;

    public class InfoBean {
        @SerializedName("rule")
        public String rule;
        @SerializedName("considerations")
        public String considerations;
        @SerializedName("word_limit")
        public int word_limit;
        @SerializedName("photo_limit")
        public int photo_limit;

        @Override
        public String toString() {
            return "InfoBean{" +
                    "rule='" + rule + '\'' +
                    ", considerations='" + considerations + '\'' +
                    ", word_limit='" + word_limit + '\'' +
                    ", photo_limit='" + photo_limit + '\'' +
                    '}';
        }
    }

}
