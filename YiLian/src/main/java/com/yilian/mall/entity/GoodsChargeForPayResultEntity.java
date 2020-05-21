package com.yilian.mall.entity;/**
 * Created by  on 2016/12/27 0027.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 支付结果
 * 购买商品时，奖励不足时，充值并直接支付后，在充值回调跳转到支付成功界面时，在支付成功界面调用pay_info接口 根据参数type返回值不同实体类
 * 3商城订单 4.付款给商家支付 5兑换中心扫码支付 6商家套餐支付
 * Created by  on 2016/12/27 0027.
 */
public class GoodsChargeForPayResultEntity extends BaseEntity {
    /**
     * 可的益豆
     */
    @SerializedName("return_bean")
    public String returnBean;
    /**
     * 平台加送益豆
     */
    @SerializedName("subsidy")
    public String subsidy;
    @SerializedName("return_integral")
    public String returnIntegral;

//    ................................................................................................
    //type=3
    /**
     * lebi : 5000
     * coupon : 200
     * integral : 0
     * deal_time : 1482541207
     */
    @SerializedName("total_total_bean")
    public String totalTotalBean;
    @SerializedName("goods_integral")
    public String goodsIntegral;
    @SerializedName("lebi")
    public String lebi;
    @SerializedName("coupon")
    public String coupon;
    @SerializedName("integral")
    public String integral;
    @SerializedName("deal_time")
    public String dealTime;
    @SerializedName("voucher")
    public String voucher;
    //送给用户的零购券（单位分）
    @SerializedName("give_voucher")
    public String giveVouncher;

    @SerializedName("group_id")
    public String groupId;//拼团id
    @SerializedName("order_index")
    public String orderIndex;
    @SerializedName("activity_id")
    public String activityId;

    //    ................................................................................................
    //type=4
    @SerializedName("merchant_name")
    public String merchantName;
    @SerializedName("deal_id")
    public String dealId;

    /**
     * {
     * "code": "1",///服务器处理结果(1/-17/-3/-4/-23/-14）
     * "lebi": "161",//交易金额
     * "coupon":"120",//交易的抵扣券
     * "merchant_name": "苹果科技体验馆",//商家名字
     * "deal_id": "2016122319013523559",//交易单号
     * "deal_time": 1482826293//交易时间
     * "order_id"://订单号
     * }
     */
//    @SerializedName("lebi")//type=3包含该字段
//    public String lebi;
//    @SerializedName("coupon")//type=3包含该字段
//    public String coupon;
    @SerializedName("give_coupon")//该笔支付赠送的抵扣券
    public String giveCoupon;
//    @SerializedName("voucher")//type=3包含该字段
//    public String voucher;
//    @SerializedName("deal_time")//type=3包含该字段
//    public String dealTime;
//    @SerializedName("order_id")//type=6包含该字段
//    public String orderId;
//    ................................................................................................
//type=5
    /**
     * {
     "code": "1",///服务器处理结果(1/-17/-3/-4/-23/-14）
     "lebi": "161",//交易金额
     "coupon":"120",//交易的抵扣券
     "deal_id": "2016122319013523559",//交易单号
     "deal_time": 1482826293//交易时间
     }
     */
//    @SerializedName("lebi")//type=3包含该字段
//    public String lebi;
//    @SerializedName("coupon")//type=3包含该字段
//    public String coupon;
//    @SerializedName("deal_id")//type=4已有该字段
//    public String dealId;
//    @SerializedName("deal_time")//type=3包含该字段
//    public String dealTime;
    //    ................................................................................................
    //    type=6
    /**
     * "code":"1",//服务器处理结果(1/-17/-3/-4/-23/-14）
     * "usable_time":""//使用时间
     * "order_id":""//订单id
     * "name":"",//套餐名称
     * "codes": [
     * {
     * "code": "10004900",
     * "status": "1"
     * },
     * {
     * "code": "10008555",
     * "status": "1"
     * }
     * ],//生成的券码数组 status的状态1未兑换 2已兑换 3待退款 4已退款'
     */
    @SerializedName("usable_time")
    public String usableTime;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("name")
    public String name;
    //    @SerializedName("lebi")//type等于3已有
//    public String lebi;
    @SerializedName("delivery_price")
    public String deliveryPrice;
    //    @SerializedName("voucher")//type等于3已有
//    public String voucher;
//    @SerializedName("give_coupoon")//type等于3已有
//    public String giveCoupon;
    @SerializedName("codes")
    public ArrayList<MTCodesEntity> codes;

    @SerializedName("is_delivery")
    public String isDelivery;
}
