package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 订单实体类
 *
 * @author Created by Zg on 2018/7/25.
 */

public class SnOrderListEntity extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;
    /**
     * 系统时间
     */
    @SerializedName("systemTime")
    public long systemTime;
    /**
     * 等待付款时长
     */
    @SerializedName("time")
    public long time;

    public static class DataBean {
        /**
         * id: "" 自增id 用于去收银台
         * order_id: "", 益联订单号
         * sn_order_id: "" 苏宁订单号,
         * payment: "",  订单总金额 (包含运费)
         * freight: "", 运费
         * order_status: "1",  订单状态 1:审核中; 2:待发货; 3:待收货; 4:已完成; 5:已取消; 6:已退货; 7:待处理; 8：审核不通过，订单已取消; 9：待支付
         * orderTime: "", 下单时间
         * settleStatus: "", 结算状态 0未结算  1已结算
         * goods_list: 商品信息
         * is7day: 是否大于等于7天 1 是 0 否 判断是否执行申请结算请求
         */
        @SerializedName("id")
        public String orderIndex;
        /**
         * 益联订单
         */
        @SerializedName("order_id")
        public String orderId;
        /**
         * 苏宁订单号
         */
        @SerializedName("sn_order_id")
        public String snOrderId;
        /**
         * 订单总金额 (包含运费)
         */
        @SerializedName("payment")
        public float payment;
        /**
         * 代购券
         */
        @SerializedName("coupon")
        public String coupon;
        /**
         * 订单运费
         */
        @SerializedName("freight")
        public float freight;
        /**
         * 订单状态 1:审核中; 2:待发货; 3:待收货; 4:已完成; 5:已取消; 6:已退货; 7:待处理; 8：审核不通过，订单已取消; 9：待支付
         */
        @SerializedName("order_status")
        public int orderStatus;
        /**
         * 下单时间戳
         */
        @SerializedName("orderTime")
        public long orderTime;
        /**
         * 结算状态 0未结算  1已结算
         */
        @SerializedName("settleStatus")
        public int settleStatus;
        /**
         * 订单下商品
         */
        @SerializedName("goods_list")
        public List<GoodsList> goodsist;

        /**
         * 0 不可申请结算 1 可申请结算
         */
        @SerializedName("applySettle")
        public int applySettle;
        /**
         * 是否大于等于7天 1 是 0 否 判断是否执行申请结算请求
         */
        @SerializedName("is7day")
        public int is7day;

        public long duration;
    }


    public static class GoodsList {

        /**
         * order_id: ""益联订单号,
         * child_order_id: ""订单行号
         * sn_order_id: ""苏宁订单号,
         * skuId: 商品id,
         * skuName: 商品名称,
         * skuPic: 商品图片,
         * skuNum: 商品数量,
         * snPrice: 商品价,
         * return_bean: 赠送的算力,
         * afterSaleStatus:申请售后状态 0未申请或者审核取消/拒绝(可申请) 1申请中(不可申请)
         */

        @SerializedName("order_id")
        private String orderId;
        @SerializedName("child_order_id")
        private String childOrderId;
        @SerializedName("sn_order_id")
        private String snOrderId;
        @SerializedName("skuId")
        private String skuId;
        @SerializedName("skuName")
        private String skuName;
        @SerializedName("skuPic")
        private String skuPic;
        @SerializedName("skuNum")
        private int skuNum;
        @SerializedName("snPrice")
        private String snPrice;
        @SerializedName("return_bean")
        private String returnBean;
        @SerializedName("afterSaleStatus")
        private String afterSaleStatus;

        public String getOrderId() {
            return orderId == null ? "" : orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getChildOrderId() {
            return childOrderId == null ? "" : childOrderId;
        }

        public void setChildOrderId(String childOrderId) {
            this.childOrderId = childOrderId;
        }

        public String getSnOrderId() {
            return snOrderId == null ? "" : snOrderId;
        }

        public void setSnOrderId(String snOrderId) {
            this.snOrderId = snOrderId;
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

        public int getSkuNum() {
            return skuNum;
        }

        public void setSkuNum(int skuNum) {
            this.skuNum = skuNum;
        }

        public String getSnPrice() {
            return snPrice == null ? "" : snPrice;
        }

        public void setSnPrice(String snPrice) {
            this.snPrice = snPrice;
        }

        public String getReturnBean() {
            return returnBean == null ? "" : returnBean;
        }

        public void setReturnBean(String returnBean) {
            this.returnBean = returnBean;
        }

        public String getAfterSaleStatus() {
            return afterSaleStatus == null ? "" : afterSaleStatus;
        }

        public void setAfterSaleStatus(String afterSaleStatus) {
            this.afterSaleStatus = afterSaleStatus;
        }
    }
}
