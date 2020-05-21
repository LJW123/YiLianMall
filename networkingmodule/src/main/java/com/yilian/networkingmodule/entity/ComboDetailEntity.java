package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by  on 2017/8/17 0017.
 */

public class ComboDetailEntity extends HttpResultBean {

    /**
     * name : 卷馒头
     * status : 1
     * codes : [{"employee":"0","employee_type":"0","merchant_id":"1403","status":"1","code":"1****3"}]
     * price : 1000
     * return_integral : 4000
     * full_minus_fee : 100
     * delivery_amount : 300
     * package_id : 7
     * end_time : 1534386729
     * package_info : [{"package_name":"食品","content":[{"name":"卷馒头","number":"1","cost":"10"}]}]
     * order_info : {"type":"2","order_number":"2017081616400376544","phone":"15238664340","pay_type":"0","pay_time":"1502872806","count":1,"amount":1000,"delivery_cost":"0","server_time":1502939390,"verify_date":"0"}
     * order_id : 2017081616400376544
     * address_info : {"linkAddress":"河南省郑州市管城回族区世正商·建正东方中心顺流逆流弄","linkMan":"暨南","linkPhone":"15238664345"}
     */

    @SerializedName("name")
    public String name;
    //套餐订单状态 0待支付 1未使用待接单 2待配送 3配送中 4已使用已送达 5已退款 6取消订单
    @SerializedName("status")
    public int status;
    @SerializedName("price")
    public String price;
    @SerializedName("return_integral")
    public int returnIntegral;
    @SerializedName("full_minus_fee")
    public String fullMinusFee;
    @SerializedName("delivery_amount")
    public String deliveryAmount;
    @SerializedName("package_id")
    public String packageId;
    @SerializedName("end_time")
    public long endTime;
    @SerializedName("order_info")
    public OrderInfoBean orderInfo;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("address_info")
    public AddressInfoBean addressInfo;
    @SerializedName("codes")
    public List<CodesBean> codes;
    @SerializedName("package_info")
    public List<PackageInfoBean> packageInfo;

    public static class OrderInfoBean {
        /**
         * type : 2
         * order_number : 2017081616400376544
         * phone : 15238664340
         * pay_type : 0
         * pay_time : 1502872806
         * count : 1
         * amount : 1000
         * delivery_cost : 0
         * server_time : 1502939390
         * verify_date : 0
         */

        @SerializedName("type")
        public String type;
        @SerializedName("order_number")
        public String orderNumber;
        @SerializedName("phone")
        public String phone;
        @SerializedName("pay_type")
        public String payType;
        @SerializedName("pay_time")
        public long payTime;
        @SerializedName("count")
        public int count;
        @SerializedName("amount")
        public int amount;
        @SerializedName("delivery_cost")
        public String deliveryCost;
        @SerializedName("server_time")
        public int serverTime;
        @SerializedName("verify_date")
        public String verifyDate;

    }

    public static class AddressInfoBean {
        /**
         * linkAddress : 河南省郑州市管城回族区世正商·建正东方中心顺流逆流弄
         * linkMan : 暨南
         * linkPhone : 15238664345
         */

        @SerializedName("linkAddress")
        public String linkAddress;
        @SerializedName("linkMan")
        public String linkMan;
        @SerializedName("linkPhone")
        public String linkPhone;
    }

    public static class CodesBean implements MultiItemEntity{
        /**
         * employee : 0
         * employee_type : 0
         * merchant_id : 1403
         * status : 1
         * code : 1****3
         */

        @SerializedName("employee")
        public String employee;
        @SerializedName("employee_type")
        public String employeeType;
        @SerializedName("merchant_id")
        public String merchantId;
        //1未兑换 2已兑换 3待退款 4已退款
        @SerializedName("status")
        public int status;
        @SerializedName("code")
        public String codeX;

        @Override
        public int getItemType() {
            return ComboOrderBaseEntity.ITEM2;
        }
    }

    public static class PackageInfoBean {
        /**
         * package_name : 食品
         * content : [{"name":"卷馒头","number":"1","cost":"10"}]
         */

        @SerializedName("package_name")
        public String packageName;
        @SerializedName("content")
        public List<ContentBean> content;

        public static class ContentBean implements MultiItemEntity{
            /**
             * name : 卷馒头
             * number : 1
             * cost : 10
             */

            @SerializedName("name")
            public String name;
            @SerializedName("number")
            public String number;
            @SerializedName("cost")
            public String cost;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "name='" + name + '\'' +
                        ", number='" + number + '\'' +
                        ", cost='" + cost + '\'' +
                        '}';
            }

            @Override
            public int getItemType() {
                return ComboOrderBaseEntity.ITEM3;
            }
        }
    }
}
