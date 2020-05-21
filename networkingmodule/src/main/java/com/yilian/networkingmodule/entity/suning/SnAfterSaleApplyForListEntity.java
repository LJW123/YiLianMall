package com.yilian.networkingmodule.entity.suning;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.entity.jd.jdMultiItem.JdAfterSaleListLayoutType;
import com.yilian.networkingmodule.entity.suning.snMultiItem.SnAfterSaleListLayoutType;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 售后申请列表
 *
 * @author Created by Zg on 2018/7/31.
 */

public class SnAfterSaleApplyForListEntity extends HttpResultBean {


    @SerializedName("list")
    public List<DataBean> data;

    public class DataBean implements MultiItemEntity {


        /**
         * "id":"",//
         * "sn_order_id":"",//苏宁订单号
         * "orderTime":"",//下单时间
         * "coupon":"",//用的券
         * "orderSnPrice":"",//订单总额 不含运费
         * "freight":"",//运费
         * "order_goods": 订单商品
         */

        @SerializedName("id")
        private String id;
        @SerializedName("sn_order_id")
        private String snOrderId;
        @SerializedName("orderTime")
        private long orderTime;
        @SerializedName("coupon")
        private String coupon;
        @SerializedName("orderSnPrice")
        private String orderSnPrice;
        @SerializedName("freight")
        private String freight;
        @SerializedName("order_goods")
        private List<GoodsList> orderGoods;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSnOrderId() {
            return snOrderId == null ? "" : snOrderId;
        }

        public void setSnOrderId(String snOrderId) {
            this.snOrderId = snOrderId;
        }

        public long getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(long orderTime) {
            this.orderTime = orderTime;
        }

        public String getCoupon() {
            return coupon == null ? "" : coupon;
        }

        public void setCoupon(String coupon) {
            this.coupon = coupon;
        }

        public String getOrderSnPrice() {
            return orderSnPrice == null ? "" : orderSnPrice;
        }

        public void setOrderSnPrice(String orderSnPrice) {
            this.orderSnPrice = orderSnPrice;
        }

        public String getFreight() {
            return freight == null ? "" : freight;
        }

        public void setFreight(String freight) {
            this.freight = freight;
        }

        public List<GoodsList> getOrderGoods() {
            if (orderGoods == null) {
                return new ArrayList<>();
            }
            return orderGoods;
        }

        public void setOrderGoods(List<GoodsList> orderGoods) {
            this.orderGoods = orderGoods;
        }

        @Override
        public int getItemType() {
            return SnAfterSaleListLayoutType.header;
        }


    }

    public class GoodsList implements MultiItemEntity {


        /**
         * "id":"",
         * "sn_order_id":"",//苏宁订单号
         * "child_order_id":"",//苏宁订单行号
         * "skuId":"",//商品ID
         * "skuName":"",//商品名称
         * "skuPic":"",//商品图片
         * "skuNum":"",//售后数量
         * "snPrice":"",//价格
         * "return_bean:"",//乐豆
         * "apply_status":"",//是否支持退货 0不支持 1支持
         * "return_status":"",//是否可申请退款 0否 1是
         * isFactorySend: 是否厂送商品 01-是；02-否
         */

        @SerializedName("id")
        private String id;
        @SerializedName("sn_order_id")
        private String snOrderId;
        @SerializedName("child_order_id")
        private String childOrderId;
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
        @SerializedName("apply_status")
        private String applyStatus;
        @SerializedName("return_status")
        private String returnStatus;
        @SerializedName("isFactorySend")
        private String isFactorySend;
        //订单总额 不含运费
        private String orderSnPrice;
        //用的券
        public String coupon;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSnOrderId() {
            return snOrderId == null ? "" : snOrderId;
        }

        public void setSnOrderId(String snOrderId) {
            this.snOrderId = snOrderId;
        }

        public String getChildOrderId() {
            return childOrderId == null ? "" : childOrderId;
        }

        public void setChildOrderId(String childOrderId) {
            this.childOrderId = childOrderId;
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

        public String getApplyStatus() {
            return applyStatus == null ? "" : applyStatus;
        }

        public void setApplyStatus(String applyStatus) {
            this.applyStatus = applyStatus;
        }

        public String getReturnStatus() {
            return returnStatus == null ? "" : returnStatus;
        }

        public void setReturnStatus(String returnStatus) {
            this.returnStatus = returnStatus;
        }

        @Override
        public int getItemType() {
            return JdAfterSaleListLayoutType.goods;
        }

        public String getOrderSnPrice() {
            return orderSnPrice == null ? "" : orderSnPrice;
        }

        public void setOrderSnPrice(String orderSnPrice) {
            this.orderSnPrice = orderSnPrice;
        }

        public String getCoupon() {
            return coupon == null ? "" : coupon;
        }

        public void setCoupon(String coupon) {
            this.coupon = coupon;
        }

        public String getIsFactorySend() {
            return isFactorySend == null ? "" : isFactorySend;
        }

        public void setIsFactorySend(String isFactorySend) {
            this.isFactorySend = isFactorySend;
        }
    }

}
