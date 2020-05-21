package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lefenandroid on 16/6/4.
 */
public class OneBuyParticipation extends BaseEntity {

    /**
     * activity_id : 活动id
     * snatch_issue : 活动期号
     * snatch_num : 夺宝号码，逗号间隔
     * snatch_man_time : 人次
     * snatch_name : 活动名称
     * snatch_goods_url : 活动商品图片地址
     */

    @SerializedName("activity_id")
    public String activityId;
    @SerializedName("snatch_issue")
    public String snatchIssue;
    @SerializedName("snatch_num")
    public String snatchNum;
    @SerializedName("snatch_man_time")
    public String snatchManTime;
    @SerializedName("snatch_name")
    public String snatchName;
    @SerializedName("snatch_goods_url")
    public String snatchGoodsUrl;

    @SerializedName("lebi")
    public String lebi;

}
