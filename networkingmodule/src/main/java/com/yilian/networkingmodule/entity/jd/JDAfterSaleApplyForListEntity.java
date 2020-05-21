package com.yilian.networkingmodule.entity.jd;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import com.yilian.networkingmodule.entity.jd.jdMultiItem.JdAfterSaleListLayoutType;
import com.yilian.networkingmodule.httpresult.HttpResultBean;


import java.util.ArrayList;
import java.util.List;

/**
 * 售后申请列表
 *
 * @author Created by Zg on 2018/5/26.
 */

public class JDAfterSaleApplyForListEntity extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;

    public class DataBean implements MultiItemEntity {
        /**
         * 主键ID
         */
        @SerializedName("id")
        private String id;
        /**
         *  "id": "",//主键ID
         * "order_id": "",//本地订单号
         * "jdOrderId":""//京东订单ID
         * "orderTime": "8621",//下单时间
         */
        /**
         * 本地订单号
         */
        @SerializedName("order_id")
        private String orderId;
        /**
         * 京东订单ID
         */
        @SerializedName("jdOrderId")
        private String jdOrderId;
        /**
         * 下单时间 时间戳 秒
         */
        @SerializedName("orderTime")
        private long orderTime;
        /**
         * 商品信息
         */
        @SerializedName("goods_list")
        private List<GoodsList> goodsList;

        public List<GoodsList> getGoodsList() {
            if (goodsList == null) {
                return new ArrayList<>();
            }
            return goodsList;
        }

        public void setGoodsList(List<GoodsList> goodsList) {
            this.goodsList = goodsList;
        }

        public String getId() {
            return id == null ? "" : id;
        }        @Override
        public int getItemType() {
            return JdAfterSaleListLayoutType.header;
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

        public String getJdOrderId() {
            return jdOrderId == null ? "" : jdOrderId;
        }

        public void setJdOrderId(String jdOrderId) {
            this.jdOrderId = jdOrderId;
        }

        public long getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(long orderTime) {
            this.orderTime = orderTime;
        }




    }

    public class GoodsList implements MultiItemEntity {
        /**
         * 0普通京东商品  1购物卡京东商品
         */
        @SerializedName("jd_type")
        public int jdType;
        /**
         * "skuPic": "6353827",//商品图片
         * "skuName": "",//商品名称
         * "skuId": "82.00",//商品ID
         * "skuNum": "",//数量
         * "skuPrice":"",// 协议价
         * "skuJdPrice":"",// 京东价
         * "return_bean":"",// 赠送益豆
         * "afterSaleStatus":"",// 申请状态
         * "jdOrderId":"",// 京东订单ID
         */

        @SerializedName("skuPic")
        private String skuPic;
        @SerializedName("skuName")
        private String skuName;
        @SerializedName("skuId")
        private String skuId;
        @SerializedName("skuNum")
        private String skuNum;
        @SerializedName("skuPrice")
        private String skuPrice;
        @SerializedName("skuJdPrice")
        private String skuJdPrice;
        @SerializedName("return_bean")
        private String returnBean;
        @SerializedName("afterSaleStatus")
        private String afterSaleStatus;
        @SerializedName("jdOrderId")
        private String jdOrderId;


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

        public String getSkuPrice() {
            return skuPrice;
        }

        public void setSkuPrice(String skuPrice) {
            this.skuPrice = skuPrice;
        }

        public String getSkuJdPrice() {
            return skuJdPrice;
        }

        public void setSkuJdPrice(String skuJdPrice) {
            this.skuJdPrice = skuJdPrice;
        }

        public String getReturnBean() {
            return returnBean;
        }

        public void setReturnBean(String returnBean) {
            this.returnBean = returnBean;
        }

        public String getAfterSaleStatus() {
            return afterSaleStatus;
        }

        public void setAfterSaleStatus(String afterSaleStatus) {
            this.afterSaleStatus = afterSaleStatus;
        }

        public String getJdOrderId() {
            return jdOrderId == null ? "" : jdOrderId;
        }

        public void setJdOrderId(String jdOrderId) {
            this.jdOrderId = jdOrderId;
        }

        @Override
        public int getItemType() {
            return JdAfterSaleListLayoutType.goods;
        }
    }

}
