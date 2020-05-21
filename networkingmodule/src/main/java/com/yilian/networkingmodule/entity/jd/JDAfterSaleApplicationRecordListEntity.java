package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 售后申请记录列表
 *
 * @author Created by Zg on 2018/5/26.
 */

public class JDAfterSaleApplicationRecordListEntity extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * 0普通京东商品  1购物卡京东商品
         */
        @SerializedName("jd_type")
        public int jdType;
        /**
         *  "skuPic": "6353827",//商品图片
         * "skuName": "",//商品名称
         * "skuId": "82.00",//商品ID
         * "skuNum": "",//数量
         * "afsServiceId":"",//售后服务id
         * "customerExpectName":"",//服务类型类型: 10 退货  20 换货  30维修
         * "afsServiceStep":"",//服务单环节: 申请阶段(10),审核不通过 (20),客服审核(21),商家审核(22),京东收货 (31),商家收货(32), 京东处理(33),商家处理(34), 用户确认(40),完成(50), 取消(60)
         * "afsServiceStepName":"",//服务单环节名称: 申请阶段,审核不通过 ,客服审核,商家审核,京东收货,商家收货, 京东处理,商家处理, 用户确认,完成, 取消
         * "orderId":""京东订单号
         * "dec": 描述
         */

        /**
         * 商品图片
         */
        @SerializedName("skuPic")
        private String skuPic;
        /**
         * 商品名称
         */
        @SerializedName("skuName")
        private String skuName;
        /**
         * 商品ID
         */
        @SerializedName("skuId")
        private String skuId;
        /**
         * 数量
         */
        @SerializedName("skuNum")
        private String skuNum;
        /**
         * 售后服务id
         */
        @SerializedName("afsServiceId")
        private String afsServiceId;
        /**
         * 服务类型类型: 10 退货  20 换货  30维修
         */
        @SerializedName("customerExpectName")
        private String customerExpectName;
        /**
         * 服务单环节: 申请阶段(10),审核不通过 (20),客服审核(21),商家审核(22),京东收货 (31),商家收货(32), 京东处理(33),商家处理(34), 用户确认(40),完成(50), 取消(60)
         */
        @SerializedName("afsServiceStep")
        private String afsServiceStep;
        /**
         * 服务单环节名称: 申请阶段,审核不通过 ,客服审核,商家审核,京东收货,商家收货, 京东处理,商家处理, 用户确认,完成, 取消
         */
        @SerializedName("afsServiceStepName")
        private String afsServiceStepName;
        /**
         * 京东订单号
         */
        @SerializedName("orderId")
        private String orderId;
        /**
         * 描述
         */
        @SerializedName("dec")
        private String dec;

        public String getSkuPic() {
            return skuPic;
        }

        public void setSkuPic(String skuPic) {
            this.skuPic = skuPic;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getSkuNum() {
            return skuNum;
        }

        public void setSkuNum(String skuNum) {
            this.skuNum = skuNum;
        }

        public String getAfsServiceId() {
            return afsServiceId;
        }

        public void setAfsServiceId(String afsServiceId) {
            this.afsServiceId = afsServiceId;
        }

        public String getCustomerExpectName() {
            return customerExpectName;
        }

        public void setCustomerExpectName(String customerExpectName) {
            this.customerExpectName = customerExpectName;
        }

        public String getAfsServiceStep() {
            return afsServiceStep;
        }

        public void setAfsServiceStep(String afsServiceStep) {
            this.afsServiceStep = afsServiceStep;
        }

        public String getAfsServiceStepName() {
            return afsServiceStepName;
        }

        public void setAfsServiceStepName(String afsServiceStepName) {
            this.afsServiceStepName = afsServiceStepName;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getDec() {
            return dec;
        }

        public void setDec(String dec) {
            this.dec = dec;
        }
    }
}
