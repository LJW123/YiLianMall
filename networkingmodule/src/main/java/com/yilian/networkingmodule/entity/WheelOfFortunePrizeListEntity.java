package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by  on 2017/4/27 0027.
 * 大转盘奖品列表
 */

public class WheelOfFortunePrizeListEntity extends BaseEntity {
    /**
     * data : {"list":[{"image":"merchant_image/20170421/20170421140002_3630480.png","prize_id":"2"},{"image":"merchant_image/20170421/20170421140002_3630480.png","prize_id":"1"},{"image":"merchant_image/20170421/20170421140002_3630480.png","prize_id":"4"},{"image":"merchant_image/20170421/20170421140002_3630480.png","prize_id":"3"},{"image":"merchant_image/20170415/20170415113351_9718889.png","prize_id":"5"},{"image":"merchant_image/20170415/20170415113351_9718889.png","prize_id":"6"},{"image":"merchant_image/20170415/20170415113351_9718889.png","prize_id":"8"},{"image":"merchant_image/20170415/20170415113351_9718889.png","prize_id":"7"}],"rules":["每次抽奖消耗50零购券","实物商品包邮","中奖物品不支持无理由退换货","中奖物品限一周内领取，过期未领取将失效","中奖物品涉及颜色以实物发货为准"]}
     */

    @SerializedName("version")
    public String version;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("desc")
        public String desc;
        @SerializedName("list")
        public ArrayList<WheelPrizeEntity> list;
        @SerializedName("rules")
        public ArrayList<String> rules;

    }

}
