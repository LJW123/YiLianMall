package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.nostra13.universalimageloader.utils.L;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by Zg on 2018/7/28.
 */

public class SnOrderDetailEntity extends HttpResultBean {


    @SerializedName("data")
    public DataBean data;

    @SerializedName("systemTime")
    public long systemTime;
    @SerializedName("time")
    public long time;

    public static class DataBean {


        /**
         * 商品列表
         */
        @SerializedName("goods_list")
        public List<GoodsList> goodsList;
        /**
         * id: "4",
         * sn_order_id: "100000000001",
         * consumer: "1111",
         * orderPrice: "1016.56",
         * orderSnPrice: "1048.00", 不含运费
         * payment: "0.00",
         * coupon: "0.00",//代购券
         * freight: "0.00",
         * order_status: "1",
         * orderTime: "1531821732",
         * paymentTime: "0",
         * paymentIndex: "0",
         * paymentType: "0",
         * cancelTime: "0",
         * cancelReason: "",
         * confirmTime: "0",
         * returnBean: "20.45",
         * isDel: "0",
         * delTime: "0",
         * settleStatus: "0", 结算状态 0未结算  1已结算
         * settleTime: "0",
         * allReturnBean: "31.44",
         * provinceId: "10",
         * province: "河南省",
         * cityId: "10",
         * city: "郑州市",
         * countyId: "1",
         * county: "金水区",
         * name: "测试",
         * mobile: "18237111819",
         * address: "啊啊啊啊",
         * order_id: "2018071718021243225",
         * remark: null,
         * applySettle: 是否可申请结算 0 不可申请 1可申请 结合订单状态 比如订单完成的情况下 使用这个字段判断
         * systemTime: 1532395670,
         * time: 86400
         * goods_list: 订单商品列表
         * is7day: 是否大于等于7天 1 是 0 否 判断是否执行申请结算请求
         */

        @SerializedName("id")
        public String orderIndex;
        @SerializedName("sn_order_id")
        public String snOrderId;
        @SerializedName("consumer")
        public String consumer;
        @SerializedName("orderPrice")
        public String orderPrice;
        @SerializedName("orderSnPrice")
        public String orderSnPrice;
        @SerializedName("payment")
        public String payment;
        @SerializedName("coupon")
        public String coupon;
        @SerializedName("freight")
        public String freight;
        @SerializedName("order_status")
        public int orderStatus;
        @SerializedName("orderTime")
        public long orderTime;
        @SerializedName("paymentTime")
        public String paymentTime;
        @SerializedName("paymentIndex")
        public String paymentIndex;
        @SerializedName("paymentType")
        public String paymentType;
        @SerializedName("cancelTime")
        public String cancelTime;
        @SerializedName("cancelReason")
        public String cancelReason;
        @SerializedName("confirmTime")
        public String confirmTime;
        @SerializedName("returnBean")
        public String returnBean;
        @SerializedName("isDel")
        public String isDel;
        @SerializedName("delTime")
        public String delTime;
        @SerializedName("settleStatus")
        public int settleStatus;
        @SerializedName("settleTime")
        public long settleTime;
        @SerializedName("allReturnBean")
        public String allReturnBean;
        @SerializedName("provinceId")
        public String provinceId;
        @SerializedName("province")
        public String province;
        @SerializedName("cityId")
        public String cityId;
        @SerializedName("city")
        public String city;
        @SerializedName("countyId")
        public String countyId;
        @SerializedName("county")
        public String county;
        @SerializedName("name")
        public String name;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("address")
        public String address;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("remark")
        public String remark;

        @SerializedName("applySettle")
        public int applySettle;
        /**
         * 是否大于等于7天 1 是 0 否 判断是否执行申请结算请求
         */
        @SerializedName("is7day")
        public int is7day;
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
         * orderStatus 订单行状态 1:审核中; 2:待发货; 3:待收货; 4:已完成; 5:已取消; 6:已退货; 7:待处理; 8：审核不通过，订单已取消; 9：待支付
         * isFactorySend: 是否厂送商品 01-是；02-否
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
        private String skuNum;
        @SerializedName("snPrice")
        private String snPrice;
        @SerializedName("return_bean")
        private String returnBean;
        @SerializedName("afterSaleStatus")
        private int afterSaleStatus;
        @SerializedName("orderStatus")
        private int orderStatus;
        @SerializedName("isFactorySend")
        private String isFactorySend;

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

        public String getSkuNum() {
            return skuNum == null ? "" : skuNum;
        }

        public void setSkuNum(String skuNum) {
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

        public int getAfterSaleStatus() {
            return afterSaleStatus;
        }

        public void setAfterSaleStatus(int afterSaleStatus) {
            this.afterSaleStatus = afterSaleStatus;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getIsFactorySend() {
            return isFactorySend == null ? "" : isFactorySend;
        }

        public void setIsFactorySend(String isFactorySend) {
            this.isFactorySend = isFactorySend;
        }
    }
}
