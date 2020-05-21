package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请售后页面 数据
 *
 * @author Created by Zg on 2018/5/28.
 */

public class JDAfterSaleApplyForDataEntity extends HttpResultBean {


    @SerializedName("info")
    public DataBean data;

    public static class DataBean {

        /**
         * "id": "",//主键ID
         * "order_id": "",//本地订单号
         * "jdOrderId":""//京东订单ID
         * "orderTime": "8621",//下单时间
         * "skuPic": "6353827",//商品图片
         * "skuName": "",//商品名称
         * "skuJdPrice":"",//商品价格
         * "skuId": "82.00",//商品ID
         * "skuNum": "",//数量
         * "name":"",//收货人
         * "address:"",//地址
         * "provinceId:"",//省ID  用于提交售后申请用
         * "mobile":"",//手机号
         * "service_type":[{code:"10",name:退货},{code:"20",name:换货},{code:"30",name:维修}]
         * "return_type":[{code:"7",name:客户送货},{code:"40",name:客户发货},{code:"4",name:上门取件}]
         * "goods_info":商品信息
         */

        @SerializedName("id")
        private String id;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("jdOrderId")
        private String jdOrderId;
        @SerializedName("orderTime")
        private String orderTime;
        @SerializedName("skuPic")
        private String skuPic;
        @SerializedName("skuName")
        private String skuName;
        @SerializedName("skuJdPrice")
        private String skuJdPrice;
        @SerializedName("skuId")
        private String skuId;
        @SerializedName("skuNum")
        private String skuNum;
        @SerializedName("name")
        private String name;
        @SerializedName("address")
        private String address;
        @SerializedName("provinceId")
        private String provinceId;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("service_type")
        private List<TypeBean> serviceType;
        @SerializedName("return_type")
        private List<TypeBean> returnType;

        @SerializedName("goods_info")
        private GoodsInfo goodsInfo;

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

        public String getJdOrderId() {
            return jdOrderId == null ? "" : jdOrderId;
        }

        public void setJdOrderId(String jdOrderId) {
            this.jdOrderId = jdOrderId;
        }

        public String getOrderTime() {
            return orderTime == null ? "" : orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getSkuPic() {
            return skuPic == null ? "" : skuPic;
        }

        public void setSkuPic(String skuPic) {
            this.skuPic = skuPic;
        }

        public String getSkuName() {
            return skuName == null ? "" : skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getSkuJdPrice() {
            return skuJdPrice == null ? "" : skuJdPrice;
        }

        public void setSkuJdPrice(String skuJdPrice) {
            this.skuJdPrice = skuJdPrice;
        }

        public String getSkuId() {
            return skuId == null ? "" : skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getSkuNum() {
            return skuNum == null ? "" : skuNum;
        }

        public void setSkuNum(String skuNum) {
            this.skuNum = skuNum;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address == null ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProvinceId() {
            return provinceId == null ? "" : provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getMobile() {
            return mobile == null ? "" : mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public List<TypeBean> getServiceType() {
            if (serviceType == null) {
                return new ArrayList<>();
            }
            return serviceType;
        }

        public void setServiceType(List<TypeBean> serviceType) {
            this.serviceType = serviceType;
        }

        public List<TypeBean> getReturnType() {
            if (returnType == null) {
                return new ArrayList<>();
            }
            return returnType;
        }

        public void setReturnType(List<TypeBean> returnType) {
            this.returnType = returnType;
        }

        public GoodsInfo getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(GoodsInfo goodsInfo) {
            this.goodsInfo = goodsInfo;
        }
    }


    public static class TypeBean {

        /**
         * code : 10
         * name : 退货
         */

        @SerializedName("code")
        private String code;
        @SerializedName("name")
        private String name;

        /**
         * 是否被选择
         */
        private boolean isSelected = false;

        public String getCode() {
            return code == null ? "" : code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    public static class GoodsInfo {

        /**
         * id : 1
         * order_id : 2018062612043360580
         * jdOrderId : 77133276046
         * skuId : 1280261
         * skuName : 雕牌 加香透明皂/洗衣皂206g*2块 肥皂（新老包装随机发货）
         * skuPic : jfs/t19294/184/266930215/251303/884d3a0/5a6830a5Nfdd2fd4e.jpg
         * skuNum : 1
         * skuType : 0
         * skuCategory : 15926
         * skuPrice : 5.00
         * skuJdPrice : 11.00
         * skuNakedPrice : 4.31
         * skuTaxPrice : 0.69
         * return_bean : 3.91
         * allReturnBean : 6.00
         * afterSaleStatus : 0
         */

        @SerializedName("id")
        private String id;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("jdOrderId")
        private String jdOrderId;
        @SerializedName("skuId")
        private String skuId;
        @SerializedName("skuName")
        private String skuName;
        @SerializedName("skuPic")
        private String skuPic;
        @SerializedName("skuNum")
        private String skuNum;
        @SerializedName("skuType")
        private String skuType;
        @SerializedName("skuCategory")
        private String skuCategory;
        @SerializedName("skuPrice")
        private String skuPrice;
        @SerializedName("skuJdPrice")
        private String skuJdPrice;
        @SerializedName("skuNakedPrice")
        private String skuNakedPrice;
        @SerializedName("skuTaxPrice")
        private String skuTaxPrice;
        @SerializedName("return_bean")
        private String returnBean;
        @SerializedName("allReturnBean")
        private String allReturnBean;
        @SerializedName("afterSaleStatus")
        private String afterSaleStatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getJdOrderId() {
            return jdOrderId;
        }

        public void setJdOrderId(String jdOrderId) {
            this.jdOrderId = jdOrderId;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getSkuPic() {
            return skuPic;
        }

        public void setSkuPic(String skuPic) {
            this.skuPic = skuPic;
        }

        public String getSkuNum() {
            return skuNum;
        }

        public void setSkuNum(String skuNum) {
            this.skuNum = skuNum;
        }

        public String getSkuType() {
            return skuType;
        }

        public void setSkuType(String skuType) {
            this.skuType = skuType;
        }

        public String getSkuCategory() {
            return skuCategory;
        }

        public void setSkuCategory(String skuCategory) {
            this.skuCategory = skuCategory;
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

        public String getSkuNakedPrice() {
            return skuNakedPrice;
        }

        public void setSkuNakedPrice(String skuNakedPrice) {
            this.skuNakedPrice = skuNakedPrice;
        }

        public String getSkuTaxPrice() {
            return skuTaxPrice;
        }

        public void setSkuTaxPrice(String skuTaxPrice) {
            this.skuTaxPrice = skuTaxPrice;
        }

        public String getReturnBean() {
            return returnBean;
        }

        public void setReturnBean(String returnBean) {
            this.returnBean = returnBean;
        }

        public String getAllReturnBean() {
            return allReturnBean;
        }

        public void setAllReturnBean(String allReturnBean) {
            this.allReturnBean = allReturnBean;
        }

        public String getAfterSaleStatus() {
            return afterSaleStatus;
        }

        public void setAfterSaleStatus(String afterSaleStatus) {
            this.afterSaleStatus = afterSaleStatus;
        }
    }
}
