package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/3/31.
 */
public class PaymentIndexEntity extends BaseEntity {

    /**
     * data : {"paytime":"","endtime":"","activity":"","goods_name":"","address":"","tel":"","code_secret":"","voucher_index":"","filiale_name":"","order_index":"","order_id":"","pay_type":"","lebi":"","voucher":""}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * paytime :
         * endtime :
         * activity :
         * goods_name :
         * address :
         * tel :
         * code_secret :
         * voucher_index :
         * filiale_name :
         * order_index :
         * order_id :
         * pay_type :
         * lebi :
         * voucher :
         */

        @SerializedName("paytime")
        public String paytime;  //购买时间
        @SerializedName("endtime")
        public String endtime; //有效期
        @SerializedName("activity")
        public String activity; //活动类型
        @SerializedName("goods_name")
        public String goodsName; //商品名字
        @SerializedName("address")
        public String address; //收货地址
        @SerializedName("tel")
        public String tel;  //联系电话
        @SerializedName("code_secret")
        public String codeSecret;  //码值
        @SerializedName("voucher_index")
        public String voucherIndex; //兑奖凭证Id
        @SerializedName("filiale_name")
        public String filialeName;  //兑奖中心名字
        @SerializedName("order_index")
        public String orderIndex; //支付成功订单自增ID
        @SerializedName("order_id")
        public String orderId; //支付的订单号
        @SerializedName("pay_type")
        public String payType; //奖励支付
        @SerializedName("lebi")
        public String lebi; //支付的金额
        @SerializedName("voucher")
        public String voucher; //支付的零购券
    }
}
