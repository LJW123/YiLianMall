package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by ${zhaiyaohua} on 2017/12/28 0028.
 * @author zhaiyaohua
 */

public class ActRewardRecorderEntity extends HttpResultBean {


    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        /**
         * record_id : 1
         * idname : 哈哈哈
         * photo :
         * award_integral : 10600
         * status : 0
         */

        @SerializedName("record_id")
        public String recordId;
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;
        @SerializedName("award_integral")
        public double awardIntegral;
        @SerializedName("status")
        public int status;
        @SerializedName("create_at")
        public long createAt;
        @SerializedName("seek_reward_limit")
        public int seekRewardLimit;
    }
}
