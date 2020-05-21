package com.yilian.mall.entity.imentity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * author XiuRenLi on 2016/8/24  PRAY NO BUG
 * 群成员信息（昵称、头像、UID）
 */

public class IMGroupMembersInfoEntity  {

    public int code;
    /**
     * message : 群用户信息列表
     * count : 4
     */

    @SerializedName("message")
    public MessageBean message;
    /**
     * message : {"message":"群用户信息列表","count":4}
     * data : [{"user_id":"3579684699619719","name":"比可","photo":""},{"user_id":"3579702140685301","name":"鲁光远","photo":"app/image/card/20160824/3579702140685301_8217655_headIcon"},{"user_id":"3579727966844818","name":"卷福","photo":"image/head/20160822/3579727966844818_27977408_userPhoto.png"},{"user_id":"3579762909077815","name":"","photo":""}]
     * request : POST /app/im.php?c=get_group_members
     */

    @SerializedName("request")
    public String request;
    /**
     * user_id : 3579684699619719
     * name : 比可
     * photo :
     */

    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class MessageBean {
        @SerializedName("message")
        public String message;
        @SerializedName("count")
        public int count;
    }

    public static class DataBean {
        @SerializedName("user_id")
        public String userId;
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;
    }
}
