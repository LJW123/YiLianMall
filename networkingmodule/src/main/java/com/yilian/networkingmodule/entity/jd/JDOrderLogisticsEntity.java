package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by Zg on 2018/5/27.
 */

public class JDOrderLogisticsEntity extends HttpResultBean {




    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {


        /**
         * content : 您提交了订单，请等待系统确认
         * msgTime : 2018-05-18 11:48:15
         * operator : 客户
         */

        @SerializedName("content")
        private String content;
        @SerializedName("msgTime")
        private String msgTime;
        @SerializedName("operator")
        private String operator;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMsgTime() {
            return msgTime;
        }

        public void setMsgTime(String msgTime) {
            this.msgTime = msgTime;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }
    }
}
