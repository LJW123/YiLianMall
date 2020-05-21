package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2017/11/16 0016.
 */

public class LuckyWinPushPopWindowEntity extends HttpResultBean {

    /**
     * win_log : {"prize_goods_name":"格兰仕洗衣机XQG-60-Q7312","prize_goods_url":"https://img.lefenmall.net/uploads/goods/goods_info/20160405/20160405165053_87209.jpg","win_num":"100003","time":"1510718500","phone":"18237115864","user_name":"光远1012","user_id":"6533477898590904","photo":"app/images/head/20171109/6533477898590904_405417_headIcon.jpg","count":"10"}
     */

    @SerializedName("win_log")
    public WinLogBean winLog;

    public static class WinLogBean {
        /**
         * prize_goods_name : 格兰仕洗衣机XQG-60-Q7312
         * prize_goods_url : https://img.lefenmall.net/uploads/goods/goods_info/20160405/20160405165053_87209.jpg
         * win_num : 100003
         * time : 1510718500
         * phone : 18237115864
         * user_name : 光远1012
         * user_id : 6533477898590904
         * photo : app/images/head/20171109/6533477898590904_405417_headIcon.jpg
         * count : 10
         */

        @SerializedName("prize_goods_name")
        public String prizeGoodsName;
        @SerializedName("prize_goods_url")
        public String prizeGoodsUrl;
        @SerializedName("win_num")
        public String winNum;
        @SerializedName("time")
        public long time;
        @SerializedName("phone")
        public String phone;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("photo")
        public String photo;
        @SerializedName("count")
        public String count;
    }
}
