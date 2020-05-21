package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 *大家猜列表实体类
 * @author Ray_L_Pain
 * @date 2017/10/16 0016
 */

public class GuessListEntity extends HttpResultBean {

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        @SerializedName("id")
        public String id;
        @SerializedName("prizes_id")
        public String prizes_id;
        @SerializedName("integral_price")
        public String integral_price;
        @SerializedName("integral_consume")
        public String integral_consume;
        @SerializedName("total_should")
        public String total_should;
        @SerializedName("participants")
        public String participants;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("start_at")
        public String start_at;
        @SerializedName("lottery_at")
        public String lottery_at;
        @SerializedName("status")
        public String status;
        @SerializedName("goods_name")
        public String goods_name;
        @SerializedName("goods_icon")
        public String goods_icon;
        @SerializedName("user_name")
        public String user_name;
        @SerializedName("prize_number")
        public String prize_number;
        @SerializedName("user_photo")
        public String user_photo;
        @SerializedName("join")
        public String join;
    }

}
