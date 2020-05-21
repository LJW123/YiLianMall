package com.yilian.networkingmodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/14 0014.
 *         话题
 */

public class TopicEntity extends HttpResultBean implements Parcelable {

    /**
     * link_url 押话题活动主页链接
     * id : 4
     * title : 是好人吗
     * topic_a_title : 不是
     * topic_a_desc : 吃喝嫖赌
     * topic_b_title : 肯定不是
     * topic_b_desc : 坑蒙拐骗
     * bet_consume : 22200
     * peep_result_consume : 11100
     * bet_limit : 333
     * description : 押不对你就傻
     * status : 1
     * kai_time : 12
     * tenMinNum : 0
     * lastInfo : [{"id":"2","title":"你丑么？","topic_a_title":"不丑","topic_a_desc":"玉树临风","topic_b_title":"丑","topic_b_desc":"丑的不能看","bet_consume":"1000","peep_result_consume":"500","bet_limit":"2","description":"押话题规描述","status":"3","redNum":2,"blueNum":0,"user_name":"","integrals":0,"myRedNum":"2","myBlueNum":0}]
     */
    @SerializedName("link_url")
    public String topicHomePageLinkUrl;
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("topic_a_title")
    public String topicATitle;
    @SerializedName("topic_a_desc")
    public String topicADesc;
    @SerializedName("topic_b_title")
    public String topicBTitle;
    @SerializedName("topic_b_desc")
    public String topicBDesc;
    @SerializedName("bet_consume")
    public int betConsume;
    @SerializedName("peep_result_consume")
    public String peepResultConsume;
    @SerializedName("bet_limit")
    public int betLimit;//每次押注上限
    @SerializedName("description")
    public String description;
    @SerializedName("status")
    public String status;
    @SerializedName("kai_time")
    public int kaiTime;
    @SerializedName("tenMinNum")
    public int tenMinNum;
    @SerializedName("lastInfo")
    public List<LastInfoBean> lastInfo;
    @SerializedName("myIntegral")
    public String myIntegral;

    protected TopicEntity(Parcel in) {
        topicHomePageLinkUrl = in.readString();
        id = in.readString();
        title = in.readString();
        topicATitle = in.readString();
        topicADesc = in.readString();
        topicBTitle = in.readString();
        topicBDesc = in.readString();
        betConsume = in.readInt();
        peepResultConsume = in.readString();
        betLimit = in.readInt();
        description = in.readString();
        status = in.readString();
        kaiTime = in.readInt();
        tenMinNum = in.readInt();
        myIntegral = in.readString();
    }

    public static final Creator<TopicEntity> CREATOR = new Creator<TopicEntity>() {
        @Override
        public TopicEntity createFromParcel(Parcel in) {
            return new TopicEntity(in);
        }

        @Override
        public TopicEntity[] newArray(int size) {
            return new TopicEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(topicHomePageLinkUrl);
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(topicATitle);
        parcel.writeString(topicADesc);
        parcel.writeString(topicBTitle);
        parcel.writeString(topicBDesc);
        parcel.writeInt(betConsume);
        parcel.writeString(peepResultConsume);
        parcel.writeInt(betLimit);
        parcel.writeString(description);
        parcel.writeString(status);
        parcel.writeInt(kaiTime);
        parcel.writeInt(tenMinNum);
        parcel.writeString(myIntegral);
    }

    public static class LastInfoBean {
        /**
         * id : 2
         * title : 你丑么？
         * topic_a_title : 不丑
         * topic_a_desc : 玉树临风
         * topic_b_title : 丑
         * topic_b_desc : 丑的不能看
         * bet_consume : 1000
         * peep_result_consume : 500
         * bet_limit : 2
         * description : 押话题规描述
         * status : 3
         * redNum : 2
         * blueNum : 0
         * user_name :
         * integrals : 0
         * myRedNum : 2
         * myBlueNum : 0
         */

        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("topic_a_title")
        public String topicATitle;
        @SerializedName("topic_a_desc")
        public String topicADesc;
        @SerializedName("topic_b_title")
        public String topicBTitle;
        @SerializedName("topic_b_desc")
        public String topicBDesc;
        @SerializedName("bet_consume")
        public String betConsume;
        @SerializedName("peep_result_consume")
        public String peepResultConsume;
        @SerializedName("bet_limit")
        public String betLimit;
        @SerializedName("description")
        public String description;
        @SerializedName("status")
        public String status;
        @SerializedName("redNum")
        public int redNum;
        @SerializedName("blueNum")
        public int blueNum;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("integrals")
        public int integrals;
        @SerializedName("myRedNum")
        public String myRedNum;
        @SerializedName("myBlueNum")
        public int myBlueNum;
    }
}
