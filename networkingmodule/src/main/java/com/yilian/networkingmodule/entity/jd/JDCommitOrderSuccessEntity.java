package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;

/**
 * @author Created by  on 2018/5/27.
 */

public class JDCommitOrderSuccessEntity extends HttpResultBean implements Serializable {

    /**
     * orderJdPrice : 订单商品部分价格
     * freight : 订单运费
     * returnBean : 赠送益豆
     * order_id : 益联订单号
     * count_down : 30
     */
    /**
     * 订单代购券金额
     */
    @SerializedName("order_quan_price")
    public String daiGouQuanMoney;
    @SerializedName("orderJdPrice")
    public String goodsPrice;
    @SerializedName("freight")
    public String freight;

    /**
     * returnBean : 赠送益豆  订单列表 订单详情跳转不传
     */
    @SerializedName("returnBean")
    public String returnBean;
    /**
     * "益联订单号",code1 返回此字段
     */
    @SerializedName("order_id")
    public String orderId;
    /**
     * 支付使用的订单号
     */
    @SerializedName("order_index")
    public String orderIndex;
    /**
     * 付款倒计时时长 单位秒
     */
    @SerializedName("count_down")
    public String countDown;
}
