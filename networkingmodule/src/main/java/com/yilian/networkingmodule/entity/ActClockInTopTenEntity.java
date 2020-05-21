package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/23 0023.
 */

public class ActClockInTopTenEntity extends HttpResultBean {

    /**
     * clockin_success: "4",//多少人
     * guafen_integral: "103000",//瓜分多少奖券
     * date_no: 1510156800//哪一天的打卡活动   2017年10月19日
     * seek_reward_limit：1//求赏最大限制   用于客户端随机生成求赏金额
     */

    @SerializedName("login_status")
    public int loginStatus;
    @SerializedName("clockin_success")
    public String clockinSuccess;
    @SerializedName("guafen_integral")
    public String guafenIntegral;
    @SerializedName("date_no")
    public Long dateNo;
    @SerializedName("seek_reward_limit")
    public int seekRewardLimit;
    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
        /**
         * name: "昵称1",//昵称
         * photo: "https://img.yilian.lefenmall.com/robot/1.png",//头像
         * record_id: "4",//当前记录的id  用于点赞或者评论用
         * given_integral: "3500",//获得奖券
         * praise: "10",//本次记录的点赞数量
         * comment: "50",//本次记录评价数量
         * signin_at: "1509664400",//打卡时间
         * consumer_id:''//对应的用户的id 也包含机器人的id 用于打开查看当前的打卡记录信息
         * is_praise://是否已点赞  0未点赞  1 已点赞
         */

        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;
        @SerializedName("consumer_id")
        public String consumerId;
        @SerializedName("record_id")
        public String recordId;
        @SerializedName("given_integral")
        public String givenIntegral;
        @SerializedName("praise")
        public int praise;
        @SerializedName("comment")
        public int comment;
        @SerializedName("signin_at")
        public long signinAt;
        @SerializedName("is_praise")
        public int isPraise;
    }
}
