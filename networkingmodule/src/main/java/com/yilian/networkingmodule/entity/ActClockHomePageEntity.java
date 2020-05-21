package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/21 0021.
 */

public class ActClockHomePageEntity extends HttpResultBean {

    /**
     * {
     * code: 1,
     * login_status: 0,
     * history_check:''//0不用提醒过时  1上期参与挑战 未打卡提醒过时
     * info: {
     * id: "1",//本次打卡活动的标识
     * apply_integral: "10600",//每次打卡需要消耗奖券数
     * rules: "规则就是规则第二版",//规则
     * date_no: "20171109"//期号
     * },
     * join_num: 4,//已经多少参与挑战 格式化好的
     * join_integral://参与了多少奖券格式化好的
     * apply_integral ://参与消耗的奖券
     * photo_arr: [//上一期参与排行榜头像
     * "https://img.yilian.lefenmall.com/robot/16.png",
     * "app/images/head/20170805/6300277240877414_9444561_1501923578936environment.jpg",
     * "app/images/head/20170717/6293052682787577_6024742_headIcon.jpg",
     * "app/images/head/20170704/6337951144236713_6905401_1499182224429environment.jpg"
     * ],
     * photo_id:''//用于点击上一期参与人数的头像进入排行榜用 （详见活动首页 - 图片进入排行榜文档）
     * button_type: 0, //三种类型 0参与  1倒计时  2未打卡   3已打卡
     * title_dec: "明早打卡可随机瓜分奖券",//顶部消息头
     * server_time: 1510136027,//当前服务器时间
     * daka_time: 1510174800,//开始打卡时间
     * daka_str1: "明日",//如果打卡倒计时开始   上部显示字符串1
     * daka_str2: "5:00 - 8:00",//如果打卡倒计时开始   上部显示字符串1
     * dayResult: {
     * first_user_photo: "app/images/head/20170705/6292664143430408_9521000_headIcon.jpg",//今日之星头像
     * first_user_name: "哈哈",//今日之星昵称
     * first_user_id: "6292664143430408",//今日之星user_id
     * first_user_time_str: "6292664143430408",//今日之星str
     * most_user_photo: "app/images/head/20170805/6300277240877414_9444561_1501923578936environment.jpg",//手气之星头像
     * most_user_name: "星星",//手气之星头像
     * most_user_id: "6300277240877414",//手气之星user_id
     * most_user_integral: "6300277240877414",//手气之星str
     * yili_user_photo: "app/images/head/20170805/6300277240877414_9444561_1501923578936environment.jpg",//手气之星头像
     * yili_user_name: "星星",//手气之星头像
     * yili_user_id: "6300277240877414",//手气之星user_id
     * yili_user_num: "6300277240877414",//手气之星str
     * colckin_success: "4",//成功打卡人数
     * colckin_fail: "2"//失败人数
     * }
     * }
     */

    @SerializedName("login_status")
    public int loginStatus;
    @SerializedName("info")
    public InfoBean info;
    @SerializedName("join_num")
    public String joinNum;
    @SerializedName("join_integral")
    public String joinIntegral;
    @SerializedName("apply_integral")
    public String applyIntegral;
    @SerializedName("photo_id")
    public String photoId;
    @SerializedName("history_integral")
    public String historyIntegral;
    @SerializedName("history_check")
    public int historyCheck;
    @SerializedName("button_type")
    public int buttonType;
    @SerializedName("title_dec")
    public String titleDec;
    @SerializedName("server_time")
    public long serverTime;
    @SerializedName("daka_time")
    public long dakaTime;
    @SerializedName("daka_str1")
    public String dakaStr1;
    @SerializedName("daka_str2")
    public String dakaStr2;
    @SerializedName("dayResult")
    public DayResultBean dayResult;
    @SerializedName("photo_arr")
    public List<String> photoArr;

    public static class InfoBean {
        /**
         * id : 14
         * cover : admin/images/ffda3d6015f9ea8bee2579eb8721803b3aac11ca_foot_right_bg.png
         * apply_integral : 10600
         * rules : 规则就是规则第二版
         * date_no : 20171122
         * created_at : 1511226566
         * status : 1
         */

        @SerializedName("id")
        public String id;
        @SerializedName("cover")
        public String cover;
        @SerializedName("apply_integral")
        public String applyIntegral;
        @SerializedName("rules")
        public String rules;
        @SerializedName("date_no")
        public String dateNo;
        @SerializedName("created_at")
        public String createdAt;
        @SerializedName("status")
        public String status;
    }

    public static class DayResultBean {
        /**
         * first_user_photo : app/images/head/20170705/6292664143430408_9521000_headIcon.jpg
         * first_user_name : 哈哈
         * first_user_id : 6292664143430408
         * first_user_time : 0
         * first_user_time_str : 08:00:00打卡
         * colckin_success : 4
         * colckin_fail : 2
         * most_user_photo : app/images/head/20170805/6300277240877414_9444561_1501923578936environment.jpg
         * most_user_name : 星星
         * most_user_id : 6300277240877414
         * most_user_integral : 0.00奖券
         * yili_user_photo : false
         * yili_user_name : false
         * yili_user_id : 0
         * yili_user_num : 连续1509688254次
         */

        @SerializedName("first_user_photo")
        public String firstUserPhoto;
        @SerializedName("first_user_name")
        public String firstUserName;
        @SerializedName("first_user_id")
        public String firstUserId;
        @SerializedName("first_user_time")
        public String firstUserTime;
        @SerializedName("first_user_time_str")
        public String firstUserTimeStr;
        @SerializedName("colckin_success")
        public String colckinSuccess;
        @SerializedName("colckin_fail")
        public String colckinFail;
        @SerializedName("most_user_photo")
        public String mostUserPhoto;
        @SerializedName("most_user_name")
        public String mostUserName;
        @SerializedName("most_user_id")
        public String mostUserId;
        @SerializedName("most_user_integral")
        public String mostUserIntegral;
        @SerializedName("yili_user_photo")
        public String yiliUserPhoto;
        @SerializedName("yili_user_name")
        public String yiliUserName;
        @SerializedName("yili_user_id")
        public String yiliUserId;
        @SerializedName("yili_user_num")
        public String yiliUserNum;
    }
}
