package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/2/21.
 * 订单信息实体类
 */
public class MallOrderInfoEntity extends BaseEntity {
    @SerializedName("order_quan_price")
    public String orderDaiGouQuanPrice;
    @SerializedName("comment_deposit")
    public String comment_deposit;
    @SerializedName("profit")
    public int profitStatus;//发奖励状态 0未发奖励  1已送奖券
    @SerializedName("supply_status")
    public int supplyStatus;//1代表可申请立即发奖励
    public String order_index;//序列号自动生成
    public String order_id;//订单编号
    public int order_status;//订单状态：0已取消，1待支付2待出库3正在出库4已部分发货5已全部发货&待收货6已完成7换货维修 8.退款
    public String order_remark;//订单备注
    public String order_time;//下单时间
    @SerializedName("flag")
    public int flag;//0不可申请发奖励订单，1可以申请发奖励订单
    public String order_payment;//支付方式
    public String payment_deal_id;//支付交易id
    public String payment_time;//支付时间
    public String order_goods_cost;
    //    public String order_coupon;//需要支付抵扣券总额
    public String order_goods_price;//商品乐享币总价
    public String order_freight_price;//运费乐享币价格
    public String order_total_price;//订单商品乐享币和运费乐享币总价
    @SerializedName("return_integral")
    public String returnIntegra;
    @SerializedName("all_given_bean")
    public String allGivenBean;
    @SerializedName("all_subsidy")
    public String allSubsidy;

    public String order_phone;//联系方式
    public String order_contacts;//收货人
    public String order_address;//收货地址
    public String filiale_id;
    public String region_id;
    public String supplier_id;
    public String order_name;
    @SerializedName("order_integral_price")
    public String orderIntegralPrice;//总共的奖券

    @SerializedName("confirm_time") //确认收货时间
    public String confirmTime;
    @SerializedName("server_time")//服务器时间戳
    public String serverTime;
    @SerializedName("delivery_time")//发货时间戳
    public String deliveryTime;
    public String order_left_extra;//由于抵扣券不足，用户使用乐享币垫付的抵扣券金额
    //送给用户的零购券 0 表示不赠送
    @SerializedName("order_give_voucher")
    public String orderGiveVoucher;
    @SerializedName("order_turns")
    public String orderTurns;//0普通商品 1中奖商品
    @SerializedName("unuse_time")//大转盘订单中，兑奖过期时间
    public long unUseTime;
    public List<Goods> goods;//订单商品明细
    public List<Parcels> parcels;
    public String tel;//商家联系方式

    /**
     *0默认益联订单 1乐分订单 2大转盘中奖订单 3刮刮乐 4大家猜 5猜价格 6碰运气
     */
    @SerializedName("order_type")
    public String orderType;

    @SerializedName("goods_integral_v2")
    public String goodsIntegralV2;

    public class Goods {

        public String goods_id;//商品id
        public String order_goods_index;//商品订单自增编号
        public int goods_status; //商品名称 0未发货  1已发货 2已完成待评价 3 已评价
        public int goods_after_sale; //是否已经申请过售后 0 未申请  1申请过
        public int after_sale_type;//可以申请那种售后 0原始售后的状态，默认值。1可退，可换，可修。2可换 可修。3可修，4不可售后
        public String goods_name;//商品名称
        public String goods_icon;//商品缩略图
        public int goods_count;//商品数量
        public String goods_unit;//商品计量单位
        public String goods_norms;//商品sku描述
        public String goods_price;//商品单价
        public String goods_discount;//10或20，代表服务费百分比
        //        public int goods_type;//3乐购区（送区）4买区 5限时购
        public String parcel_index;//包裹序列
        public String goods_sku;
        public String goods_cost;
        @SerializedName("return_integral")
        public String returnIntegral;//销售奖券
        @SerializedName("given_bean")
        public String givenBean;
        @SerializedName("goods_retail")
        public String goodsRetail;
        @SerializedName("goods_integral")
        public String goodsIntegral;
        /**
         * 3区块试验区 5线上商城
         */
        @SerializedName("type")
        public String type;
        @SerializedName("act_id")
        public String actId;

        @SerializedName("subsidy")
        public String subsidy;
    }

    public class Parcels {
        public String parcel_index;//包裹序列号
        public int parcel_status;//包裹状态：0待收货1已收货
        public String express_time;//发货时间
        public String express_company;//物流名称
        public String express_num;//物流编号
        public String express_info;//物流信息
        public String confirm_time;//确认收货时间
    }


}
