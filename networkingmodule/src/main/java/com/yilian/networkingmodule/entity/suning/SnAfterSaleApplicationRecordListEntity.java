package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 售后申请记录列表
 *
 * @author Created by Zg on 2018/5/26.
 */

public class SnAfterSaleApplicationRecordListEntity extends HttpResultBean {


    @SerializedName("list")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 90
         * orderId : 100002061566
         * applyTime : 1532774471
         * isRefund : 0
         * apply_status : 1
         * skuId : 146853944
         * skuName : 顺清柔厨房纸巾4卷装
         * skuPic : http://image4.suning.cn/uimg/b2c/newcatentries/0000000000-000000000146853944_1.jpg_400w_400h_4e
         */

        @SerializedName("id")
        private String id;
        @SerializedName("orderId")
        private String orderId;
        @SerializedName("applyTime")
        private long applyTime;
        @SerializedName("isRefund")
        private String isRefund;
        @SerializedName("apply_status")
        private String applyStatus;
        @SerializedName("skuId")
        private String skuId;
        @SerializedName("skuName")
        private String skuName;
        @SerializedName("skuPic")
        private String skuPic;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId == null ? "" : orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public long getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(long applyTime) {
            this.applyTime = applyTime;
        }

        public String getIsRefund() {
            return isRefund == null ? "" : isRefund;
        }

        public void setIsRefund(String isRefund) {
            this.isRefund = isRefund;
        }

        public String getApplyStatus() {
            return applyStatus == null ? "" : applyStatus;
        }

        public void setApplyStatus(String applyStatus) {
            this.applyStatus = applyStatus;
        }

        public String getSkuId() {
            return skuId == null ? "" : skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getSkuName() {
            return skuName == null ? "" : skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getSkuPic() {
            return skuPic == null ? "" : skuPic;
        }

        public void setSkuPic(String skuPic) {
            this.skuPic = skuPic;
        }
    }
}
