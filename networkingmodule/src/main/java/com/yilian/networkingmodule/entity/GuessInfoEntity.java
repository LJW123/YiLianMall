package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/10/17 0017.
 */

public class GuessInfoEntity extends HttpResultBean {

    @SerializedName("info")
    public InfoBean info;

    public class InfoBean {
        @SerializedName("start_at")
        public String start_at;
        @SerializedName("lottery_at")
        public String lottery_at;
        @SerializedName("total_should")
        public String total_should;
        @SerializedName("participants")
        public String participants;
        @SerializedName("goods_name")
        public String goods_name;
        @SerializedName("goods_icon")
        public String goods_icon;
        @SerializedName("user_name")
        public String user_name;
        @SerializedName("status")
        public String status;
        @SerializedName("user_photo")
        public String user_photo;
        @SerializedName("prize_number")
        public String prize_number;
        @SerializedName("guess_number")
        public ArrayList<GuessNumberBean> guess_number;

        public class GuessNumberBean {
            @SerializedName("guess_number")
            public String guess_number;
            @SerializedName("apply_at")
            public String apply_at;
            @SerializedName("number_count")
            public String number_count;
        }
    }

}
