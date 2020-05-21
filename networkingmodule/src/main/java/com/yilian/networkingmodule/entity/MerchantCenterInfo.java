package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by  on 2017/6/14 0014.
 */

public class MerchantCenterInfo extends BaseEntity {

    /**
     * is_expire : 1
     * merchantInfo : {"merchantTrade":{"tradAmount":100000,"discountAmount":200000,"salesBonus":300000},"icon":[{"name":"收款码","image":"app/merchant/20170613/dazhaxie.jpg","status":2},{"name":"现金交易","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"折扣设置","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"收款记录","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"门店资料","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"门店地址","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"申请领奖励","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"年费缴存","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"}]}
     * supplierInfo : {"isExist":1,"supplierTrade":{"tradeAmount":0,"discountAmount":0,"salesBonus":0,"readySend":"0","readyReceive":"0","service":0}}
     */

    @SerializedName("is_expire")//是否过期
    public int isExpire;
    @SerializedName("merchantInfo")
    public MerchantInfoBean merchantInfo;
    @SerializedName("supplierInfo")
    public SupplierInfoBean supplierInfo;
    @SerializedName("supplierUrl")
    public String supplierUrl;
    @SerializedName("merchantNotice")
    public String merchantNotice;
    @SerializedName("supplierNotice")
    public String supplierNotice;

    /**
     * 提示信息
     */
    @SerializedName("intro")
    public String intro;
    /**
     * 到期剩余天数
     */
    @SerializedName("last_days")
    public long last_days;


    public static class MerchantInfoBean {
        public static final int OPEN_POWER = 1;
        public static final int UN_OPEN_POWER = 0;
        //        is_happy_bean 商家有没有开启送益豆权限  0未开启1开启
        @SerializedName("is_happy_bean")
        public int isOpenPower;
        @SerializedName("merchant_status")
        public int merchantStatus;//商家线下门店资料审核状态 0表示待提交，1表示待审核，2表示审核通过，3表示审核拒绝
        @SerializedName("refund_reason")
        public String refundReason;//拒绝原因
        /**
         * merchantTrade : {"tradAmount":100000,"discountAmount":200000,"salesBonus":300000}
         * icon : [{"name":"收款码","image":"app/merchant/20170613/dazhaxie.jpg","status":2},{"name":"现金交易","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"折扣设置","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"收款记录","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"门店资料","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"门店地址","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"申请领奖励","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"},{"name":"年费缴存","image":"app/merchant/20170613/dazhaxie.jpg","status":"1"}]
         */

        @SerializedName("merchant_deadLine")
        public long merchantDueTime;//商家过期时间
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_type")
        public String merchantType;//0个体上家  1实体商家
        @SerializedName("merchantTrade")
        public MerchantTradeBean merchantTrade;
        @SerializedName("icon")
        public List<IconBean> icon;

        public static class MerchantTradeBean {
            /**
             * tradAmount : 100000
             * discountAmount : 200000
             * salesBonus : 300000
             * beanAmount:益豆
             */

            @SerializedName("tradAmount")
            public String tradAmount;
            @SerializedName("discountAmount")
            public String discountAmount;
            @SerializedName("salesBonus")
            public String salesBonus;
            @SerializedName("beanAmount")
            public String beanAmount;
        }

        public static class IconBean {
            /**
             * name : 收款码
             * image : app/merchant/20170613/dazhaxie.jpg
             * status : 2
             */

            @SerializedName("name")
            public String name;
            @SerializedName("image")
            public String image;
            @SerializedName("status")
            public int status;
            @SerializedName("type")
            public int type;
        }
    }

    public static class SupplierInfoBean {
        /**
         * isExist : 1
         * supplierTrade : {"tradeAmount":0,"discountAmount":0,"salesBonus":0,"readySend":"0","readyReceive":"0","service":0}
         */

        @SerializedName("isExist")
        public int isExist;
        @SerializedName("supplierTrade")
        public SupplierTradeBean supplierTrade;

        public static class SupplierTradeBean {
            /**
             * tradeAmount: 0,//当日销售额
             * discountAmount: 0,//当日让利额
             * salesBonus: 0,//当日发奖励
             * readySend: "0",//待发货数量
             * readyReceive: "0",//待收货数量
             * service: 0//售后数量
             * goods_num:""//商品管理数量
             */

            @SerializedName("tradeAmount")
            public String tradeAmount;
            @SerializedName("discountAmount")
            public String discountAmount;
            @SerializedName("salesBonus")
            public String salesBonus;
            @SerializedName("readySend")
            public String readySend;
            @SerializedName("readyReceive")
            public String readyReceive;
            @SerializedName("service")
            public String service;
            @SerializedName("goods_num")
            public String goodsNum;
        }
    }
}
