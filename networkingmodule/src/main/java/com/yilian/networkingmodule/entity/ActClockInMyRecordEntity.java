package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/23 0023.
 */

public class ActClockInMyRecordEntity extends HttpResultBean {

    /**
     * record : {"joinNum":"2","punchNum":"1","giveIntegral":0}
     * recordList : [{"punch_str":"11-09","status":"1","apply_integral":"10600","given_integral":"30"}]
     * friendGuafen:''//上一期的推荐好友瓜分金额
     * friendClockinId:''//好友瓜分金额对应的活动的id
     */

    @SerializedName("record")
    public RecordBean record;
    @SerializedName("friendGuafen")
    public String friendGuafen;
    @SerializedName("friendClockinId")
    public int friendClockinId;
    @SerializedName("recordList")
    public List<RecordListBean> recordList;

    public static class RecordBean {
        /**
         * joinNum: "2",//参与人次
         * punchNum: "1",//打卡人次
         * giveIntegral: "30"//收益
         */

        @SerializedName("joinNum")
        public String joinNum;
        @SerializedName("punchNum")
        public String punchNum;
        @SerializedName("giveIntegral")
        public String giveIntegral;
    }

    public static class RecordListBean {
        public RecordListBean(String punchStr, int status, String applyIntegral, String givenIntegral) {
            this.punchStr = punchStr;
            this.status = status;
            this.applyIntegral = applyIntegral;
            this.givenIntegral = givenIntegral;
        }

        /**
         * punch_str: "11-09",//打卡活动日期
         * status: "1",// 1 0打卡失败   1打卡成功
         * apply_integral: "10600",//消耗奖券
         * given_integral: "30"//获得奖券
         */

        @SerializedName("punch_str")
        public String punchStr;
        @SerializedName("status")
        public int status;
        @SerializedName("apply_integral")
        public String applyIntegral;
        @SerializedName("given_integral")
        public String givenIntegral;

        @Override
        public String toString() {
            return "RecordListBean{" +
                    "punchStr='" + punchStr + '\'' +
                    ", status=" + status +
                    ", applyIntegral='" + applyIntegral + '\'' +
                    ", givenIntegral='" + givenIntegral + '\'' +
                    '}';
        }
    }
}
