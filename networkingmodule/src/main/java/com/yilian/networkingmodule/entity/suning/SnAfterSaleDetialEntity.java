package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * 苏宁售后记录详情
 *
 * @author Created by Zg on 2018/5/29.
 */

public class SnAfterSaleDetialEntity extends HttpResultBean {


    @SerializedName("info")
    public DataBean data;

    public static class DataBean {

        /**
         * {
         * "type":"",//'服务类型名称: 0退货',
         * "orderId":"",//'苏宁父订单号',
         * "skuId":"",//'商品ID',
         * "skuName":"",//商品名称
         * "skuPic":"",//商品图片
         * "price":"",//商品价格
         * "skuNum":"",//'售后商品数量',
         * "applyTime":"",//'服务单申请时间',
         * "isDel":"",//'是否删除了 逻辑删除',
         * "delTime":"",//'删除时间',
         * "isRefund":"",//'是否已退款 0未退款  1已退款',
         * "refundTime":"",//'退款时间   没有退款则为0',
         * "pickwareName":"",//'取件方式: 上门取件 客户送货 客户发货',
         * "returnQuan":"",//'退回的抵扣券金额',
         * "apply_status":"",//'是否申请成功 0失败 1成功',
         * "reason":"",//'失败原因',
         * "name":"",//收货人
         * "mobile":"",//手机号
         * "address":"",//收货地址
         * "freight":"",//运费
         * "snPrice":"",//商品单价
         * }
         */


        @SerializedName("id")
        private String id;
        @SerializedName("order_consumer")
        private String orderConsumer;
        @SerializedName("type")
        private String type;
        @SerializedName("orderId")
        private String orderId;
        @SerializedName("skuId")
        private String skuId;
        @SerializedName("num")
        private String num;
        @SerializedName("applyTime")
        private long applyTime;
        @SerializedName("isDel")
        private String isDel;
        @SerializedName("delTime")
        private String delTime;
        @SerializedName("isRefund")
        private String isRefund;
        @SerializedName("refundTime")
        private long refundTime;
        @SerializedName("returnQuan")
        private String returnQuan;
        @SerializedName("apply_status")
        private String applyStatus;
        @SerializedName("reason")
        private String reason;
        @SerializedName("child_order_id")
        private String childOrderId;
        @SerializedName("skuName")
        private String skuName;
        @SerializedName("skuPic")
        private String skuPic;
        @SerializedName("name")
        private String name;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("freight")
        private String freight;
        @SerializedName("address")
        private String address;
        @SerializedName("snPrice")
        private String snPrice;
        @SerializedName("price")
        private String price;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderConsumer() {
            return orderConsumer == null ? "" : orderConsumer;
        }

        public void setOrderConsumer(String orderConsumer) {
            this.orderConsumer = orderConsumer;
        }

        public String getType() {
            return type == null ? "" : type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOrderId() {
            return orderId == null ? "" : orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getSkuId() {
            return skuId == null ? "" : skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getNum() {
            return num == null ? "" : num;
        }

        public void setNum(String num) {
            this.num = num;
        }


        public String getIsDel() {
            return isDel == null ? "" : isDel;
        }

        public void setIsDel(String isDel) {
            this.isDel = isDel;
        }

        public String getDelTime() {
            return delTime == null ? "" : delTime;
        }

        public void setDelTime(String delTime) {
            this.delTime = delTime;
        }

        public String getIsRefund() {
            return isRefund == null ? "" : isRefund;
        }

        public void setIsRefund(String isRefund) {
            this.isRefund = isRefund;
        }

        public long getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(long applyTime) {
            this.applyTime = applyTime;
        }

        public long getRefundTime() {
            return refundTime;
        }

        public void setRefundTime(long refundTime) {
            this.refundTime = refundTime;
        }

        public String getReturnQuan() {
            return returnQuan == null ? "0" : returnQuan;
        }

        public void setReturnQuan(String returnQuan) {
            this.returnQuan = returnQuan;
        }

        public String getApplyStatus() {
            return applyStatus == null ? "" : applyStatus;
        }

        public void setApplyStatus(String applyStatus) {
            this.applyStatus = applyStatus;
        }

        public String getReason() {
            return reason == null ? "" : reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getChildOrderId() {
            return childOrderId == null ? "" : childOrderId;
        }

        public void setChildOrderId(String childOrderId) {
            this.childOrderId = childOrderId;
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

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile == null ? "" : mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFreight() {
            return freight == null ? "" : freight;
        }

        public void setFreight(String freight) {
            this.freight = freight;
        }

        public String getAddress() {
            return address == null ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSnPrice() {
            return snPrice == null ? "" : snPrice;
        }

        public void setSnPrice(String snPrice) {
            this.snPrice = snPrice;
        }

        public String getPrice() {
            return price == null ? "" : price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
