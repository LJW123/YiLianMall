package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by Zg on 2018/7/31.
 */

public class SnOrderLogisticsEntity extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {


        /**
         * operateTime : 2016-07-08 10:05:33
         * operateState : 您的订单已生成，等待账期扣减
         */

        @SerializedName("operateTime")
        private String operateTime;
        @SerializedName("operateState")
        private String operateState;

        public String getOperateTime() {
            return operateTime == null ? "" : operateTime;
        }

        public void setOperateTime(String operateTime) {
            this.operateTime = operateTime;
        }

        public String getOperateState() {
            return operateState == null ? "" : operateState;
        }

        public void setOperateState(String operateState) {
            this.operateState = operateState;
        }
    }
}
