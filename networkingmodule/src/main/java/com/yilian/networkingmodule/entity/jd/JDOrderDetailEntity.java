package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/5/22.
 */

public class JDOrderDetailEntity extends HttpResultBean {


    /**
     * data : {"id":"20","order_consumer":"6327215914088517","order_id":"2018051917223157793","pOrder":"0","jdOrderId":"74040715959","orderPrice":"30.45","orderNakedPrice":"26.25","orderTaxPrice":"4.20","orderJdPrice":"37.90","freight":"8.00","state":"1","orderState":"1","submitState":"1","orderType":"1","jdOrderState":"19","addressId":"1","address":"efb8bf0c8bf5d8c7e3cf8c536ff4551e76a83595984d9ec250e6bbaa1854b79f6bbb6ceb93c02129f6a59e76cc2aa5d5d0e82c7ade4dd1a2","name":"8c155597dad477b5","mobile":"c27a50580beebd0eff8d97ef13a6b796","skuId":"1184648","skuName":"南街村 北京方便面 麻辣味 65g*40袋","skuPic":"jfs/t1936/297/1193260445/1385514/6e4d472/564d9594N9678825f.jpg","skuNum":"1","skuType":"0","skuCategory":"2679","skuPrice":"30.45","skuJdPrice":"37.90","skuNakedPrice":"26.25","skuTaxPrice":"4.20","OrderSkuVO":"{\"category\":2679,\"num\":1,\"price\":30.449999999999999,\"tax\":16,\"oid\":0,\"name\":\"u5357u8857u6751 u5317u4eacu65b9u4fbfu9762 u9ebbu8fa3u5473 65g*40u888b\",\"taxPrice\":4.2000000000000002,\"skuId\":1184648,\"nakedPrice\":26.25,\"type\":0}","orderTime":"1526721752","paymentTime":"1526722161","paymentIndex":"0","paymentType":"0","cancelTime":"0","cancelReason":"","confirmTime":"0","returnBean":"7.45","isDel":"0","delTime":"0","settleStatus":"1","settleTime":"1526959652","serviceStatus":"0"}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * 0普通京东商品  1购物卡京东商品
         */
        @SerializedName("jd_type")
        public int jdType;
        /**
         * type:0只显示再次购买按钮 原则上不存在 ,1待付款 ,2 待收货, 3已完成, 4已取消
         * id : 20
         * order_consumer : 6327215914088517
         * order_id : 2018051917223157793
         * pOrder : 0
         * jdOrderId : 74040715959
         * orderPrice : 30.45
         * orderNakedPrice : 26.25
         * orderTaxPrice : 4.20
         * orderJdPrice : 37.90
         * freight : 8.00
         * state : 1
         * orderState : 1
         * submitState : 1
         * orderType : 1
         * jdOrderState : 19
         * addressId : 1
         * address : efb8bf0c8bf5d8c7e3cf8c536ff4551e76a83595984d9ec250e6bbaa1854b79f6bbb6ceb93c02129f6a59e76cc2aa5d5d0e82c7ade4dd1a2
         * name : 8c155597dad477b5
         * mobile : c27a50580beebd0eff8d97ef13a6b796
         * skuId : 1184648
         * skuName : 南街村 北京方便面 麻辣味 65g*40袋
         * skuPic : jfs/t1936/297/1193260445/1385514/6e4d472/564d9594N9678825f.jpg
         * skuNum : 1
         * skuType : 0
         * skuCategory : 2679
         * skuPrice : 30.45
         * skuJdPrice : 37.90
         * skuNakedPrice : 26.25
         * skuTaxPrice : 4.20
         * OrderSkuVO : {"category":2679,"num":1,"price":30.449999999999999,"tax":16,"oid":0,"name":"u5357u8857u6751 u5317u4eacu65b9u4fbfu9762 u9ebbu8fa3u5473 65g*40u888b","taxPrice":4.2000000000000002,"skuId":1184648,"nakedPrice":26.25,"type":0}
         * orderTime : 1526721752
         * paymentTime : 1526722161
         * paymentIndex : 0
         * paymentType : 0
         * cancelTime : 0
         * cancelReason :
         * confirmTime : 0
         * returnBean : 7.45
         * isDel : 0
         * delTime : 0
         * settleStatus : 1
         * settleTime : 1526959652
         * serviceStatus : 0
         * paymentTypeStr
         *  count_down
         */
        /**
         * 订单代购券数量
         */
        @SerializedName("order_quan_price")
        public String orderDaiGouQuanMoney;

        /**
         * 内部订单状态
         */
        @SerializedName("type")
        public int type;

        @SerializedName("id")
        public String orderIndex;
        /**
         * 订单用户uid
         */
        @SerializedName("order_consumer")
        public String orderConsumer;
        /**
         * 益联订单号
         */
        @SerializedName("order_id")
        public String orderId;
        /**
         * 父订单号
         */
        @SerializedName("pOrder")
        public String pOrder;
        /**
         * 京东子订单号
         */
        @SerializedName("jdOrderId")
        public String jdOrderId;
        /**
         * 订单价格
         */
        @SerializedName("orderPrice")
        public float orderPrice;
        /**
         * 订单裸价
         */
        @SerializedName("orderNakedPrice")
        public float orderNakedPrice;
        /**
         * 订单税费
         */
        @SerializedName("orderTaxPrice")
        public float orderTaxPrice;
        @SerializedName("orderJdPrice")
        public float orderJdPrice;
        /**
         * 订单运费
         */
        @SerializedName("freight")
        public float freight;
        /**
         * 物流状态  0是新建 1是妥投 2是拒收
         */
        @SerializedName("state")
        public int logisticsState;
        /**
         * 订单状态 0 为取消订单 1 为有效
         */
        @SerializedName("orderState")
        public int orderState;
        /**
         * 0未确认下单订单 1确认下单订单
         */
        @SerializedName("submitState")
        public int submitState;
        /**
         * 订单类别  1.普通商品2.大家电 类似
         */
        @SerializedName("orderType")
        public int orderType;
        /**
         * 京东状态，1.新单 2.等待支付 3.等待支付确认 4.延迟付款确认 5.订单暂停 6.店长最终审核 7.等待打印 8.等待出库 9.等待打包 10.等待发货 11.自提途中 12.上门提货 13.自提退货
         * 14.确认自提 16.等待确认收货 17.配送退货 18.货到付款确认 19.已完成 21.收款确认 22.锁定 29.等待三方出库 30.等待三方发货 31.等待三方发货完成
         */
        @SerializedName("jdOrderState")
        public int jdOrderState;
        /**
         * 地址对应的地址表的id
         */
        @SerializedName("addressId")
        public String addressId;
        /**
         * 地址
         */
        @SerializedName("address")
        public String address;
        /**
         * 联系人名称
         */
        @SerializedName("name")
        public String linkName;
        /**
         * 联系方式
         */
        @SerializedName("mobile")
        public String linkMobile;

        @SerializedName("OrderSkuVO")
        public String OrderSkuVO;
        /**
         * 订单生成时间 秒级时间戳
         */
        @SerializedName("orderTime")
        public long orderTime;
        /**
         * 订单支付时间 秒级时间戳
         */
        @SerializedName("paymentTime")
        public long paymentTime;
        /**
         * recharge_order的id
         */
        @SerializedName("paymentIndex")
        public String paymentIndex;
        /**
         * 支付方式  0余额  1三方  2混合
         */
        @SerializedName("paymentType")
        public String paymentType;
        /**
         * 取消时间 秒级时间戳 如果没有取消则为0
         */
        @SerializedName("cancelTime")
        public long cancelTime;
        /**
         * 取消原因
         */
        @SerializedName("cancelReason")
        public String cancelReason;
        /**
         * 确认收货时间 秒级时间戳 没有确认收货则为0
         */
        @SerializedName("confirmTime")
        public long confirmTime;
        /**
         * 赠送益豆
         */
        @SerializedName("returnBean")
        public float returnBean;
        /**
         * 删除状态 0未删除 1已删除
         */
        @SerializedName("isDel")
        public int isDel;
        /**
         * 删除时间 秒级时间戳  没有删除则为0
         */
        @SerializedName("delTime")
        public long delTime;
        /**
         * // 结算状态  0未结算  1已结算 （根据订单状态 结算状态 申请退货状态判断是否显示申请结算按钮）
         */
        @SerializedName("settleStatus")
        public int settleStatus;
        /**
         * //结算时间 秒级时间戳 未结算为0
         */
        @SerializedName("settleTime")
        public long settleTime;
        /**
         * //退货状态  1正在退货 0可以申请退货
         */
        @SerializedName("serviceStatus")
        public int serviceStatus;

        /**
         * //退货状态  1正在退货 0可以申请退货
         */
        @SerializedName("paymentTypeStr")
        public String paymentTypeStr;

        /**
         * 等待付款时长
         */
        @SerializedName("count_down")
        public long countDown;

        /**
         * 系统时间
         */
        @SerializedName("sysTime")
        public long sysTime;

        /**
         * 是否可申请结算 0 不可申请 1可申请 结合订单状态 比如订单完成的情况下 使用这个字段判断
         */
        @SerializedName("applySettle")
        public long applySettle;

        /**
         * 商品列表
         */
        @SerializedName("goods_list")
        public List<GoodsList> goodsList;

    }

    public static class GoodsList {
        /**
         * 商品id
         */
        @SerializedName("id")
        public String id;
        /**
         * 益联订单号
         */
        @SerializedName("order_id")
        public String orderId;
        /**
         * 京东子订单号
         */
        @SerializedName("jdOrderId")
        public String jdOrderId;
        /**
         * 商品skuid
         */
        @SerializedName("skuId")
        public String skuId;
        /**
         * 商品名称
         */
        @SerializedName("skuName")
        public String skuName;
        /**
         * 商品主图
         */
        @SerializedName("skuPic")
        public String skuPic;
        /**
         * 商品数量
         */
        @SerializedName("skuNum")
        public int skuNum;
        /**
         * ,商品类型   0普通 1附件 2赠品
         */
        @SerializedName("skuType")
        public int skuType;
        /**
         * 商品类目
         */
        @SerializedName("skuCategory")
        public String skuCategory;
        /**
         * 商品的价格
         */
        @SerializedName("skuPrice")
        public float skuPrice;

        @SerializedName("skuJdPrice")
        public float skuJdPrice;
        /**
         * 商品裸价
         */
        @SerializedName("skuNakedPrice")
        public float skuNakedPrice;
        /**
         * 商品税价
         */
        @SerializedName("skuTaxPrice")
        public float skuTaxPrice;

        @SerializedName("return_bean")
        public float return_bean;

        @SerializedName("allReturnBean")
        public float allReturnBean;
        /**
         * 申请售后状态 0未申请或者审核取消/拒绝(可申请) 1申请中(不可申请)
         */
        @SerializedName("afterSaleStatus")
        public float afterSaleStatus;

    }
}
