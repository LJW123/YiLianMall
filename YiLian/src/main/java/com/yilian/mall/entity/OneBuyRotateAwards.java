package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by lefenandroid on 16/5/25.
 */
public class OneBuyRotateAwards extends BaseEntity{


    /**
     * code : 1
     * list : [{"user_name":0,"time":"","goods_name":"","snatch_index":""},{}]
     */

    /**
     * user_name : 0
     * time :
     * goods_name :
     * snatch_index :
     */

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        @SerializedName("user_name") // 用户名称
        public String userName;
        @SerializedName("time") // 中奖时间
        public String time;
        @SerializedName("goods_name") // 中奖名称
        public String goodsName;
        @SerializedName("snatch_index") // 活动id
        public String snatchIndex;
    }
}
