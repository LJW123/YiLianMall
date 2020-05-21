package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/15 0015.
 */

public class LuckyMemberJoinRecordEntity extends HttpResultBean {

    /**
     * code :
     * user_name :
     * phone :
     * photo :
     * list : [{"isSelf":"","type":"","activity_id":"","snatch_issue":"","snatch_name":"","snatch_announce_time":"","user_name":"","phone":"","win_num":"","photo":"","join_count":"","total_count":"","award_status":"","mycount":"","goods_url":"","awardcount":""},{}]
     */

    @SerializedName("user_name")
    public String userName;
    @SerializedName("phone")
    public String phone;
    @SerializedName("photo")
    public String photo;
    @SerializedName("list")
    public List<ListBean> list;


    public static class ListBean implements MultiItemEntity {
        /**
         * "isSelf":"",//是不是本人
         * "type":"",//1 进行中 2已开奖
         * "activity_id":"",//活动id
         * "snatch_issue":"",//活动期号
         * "snatch_name":"",//活动标题
         * "snatch_announce_time":""//揭晓时间
         * "user_name":"",//中奖者用户昵称
         * "phone":"",//手机号码
         * "win_num":"",//中奖号码
         * "photo":"",//中奖者头像
         * "join_count":"",//活动参与次数
         * "total_count":"",//活动需参与总人次数
         * "award_status":"",//奖励状态：0表示未开奖，1表示未设置收货地址，2待备货，3，已发货 5待发货
         * "mycount":"",//我/ta的参与次数
         * "goods_url":"",//商品图片
         * "awardcount":"",//中奖人参与次数20
         */

        @SerializedName("isSelf")
        public int isSelf;
        @SerializedName("type")
        public int type; //1 进行中 2已开奖
        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("phone")
        public String phone;
        @SerializedName("win_num")
        public String winNum;
        @SerializedName("photo")
        public String photo;
        @SerializedName("join_count")
        public int joinCount; //活动参与次数
        @SerializedName("total_count")
        public int totalCount;
        @SerializedName("award_status")
        public String awardStatus;
        @SerializedName("mycount")
        public int mycount; //我/ta的参与次数
        @SerializedName("goods_url")
        public String goodsUrl;
        @SerializedName("awardcount")
        public int awardcount;
        @SerializedName("award_self")
        public int awardSelf;//中奖者是不是被查看人 0不是  1是

        @Override
        public int getItemType() {
            if (0 == type) {
                return 2;
            }
            return type;
        }
    }
}
