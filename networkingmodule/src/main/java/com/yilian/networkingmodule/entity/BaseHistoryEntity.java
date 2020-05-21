package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by  on 2017/8/25 0025.
 */

public class BaseHistoryEntity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * user_id :
         * game_id :
         * game_name :
         * expend_integral :
         * gain_integral :
         * result_integral :
         * time :
         */

        @SerializedName("user_id")
        public String userId;
        @SerializedName("game_id")
        public String gameId;
        @SerializedName("game_name")
        public String gameName;
        @SerializedName("expend_integral")
        public long expendIntegral;//消耗奖券
        @SerializedName("gain_integral")
        public long gainIntegral;//获得奖券
        @SerializedName("result_integral")
        public long resultIntegral;//最终战果
        @SerializedName("time")
        public long time;
    }
}
