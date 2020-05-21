package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/5/28.
 */

public class JDCashPayResult extends HttpResultBean {


    /**
     * total_cash : 用户支付的订单总金额（包含运费）
     * orderPrice : 订单商品部分公司账户扣除金额
     * orderJdPrice : 订单商品部分价格
     * freight : 订单运费
     * return_bean : 赠送益豆
     * count_down : 1800
     * order_index : 1
     */

    @SerializedName("total_cash")
    public String totalCash;
    @SerializedName("orderPrice")
    public String orderPrice;
    @SerializedName("orderJdPrice")
    public String orderJdPrice;
    @SerializedName("freight")
    public String freight;
    @SerializedName("return_bean")
    public String returnBean;
    @SerializedName("count_down")
    public String countDown;
    @SerializedName("order_index")
    public String orderIndex;
}
