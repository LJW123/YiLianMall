package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;

/**
 * @author Created by  on 2018/5/28.
 */

public class JDPayInfoEntity extends HttpResultBean implements Serializable{

    @Override
    public String toString() {
        return "JDPayInfoEntity{" +
                "totalCash='" + totalCash + '\'' +
                ", freight='" + freight + '\'' +
                ", returnBean='" + returnBean + '\'' +
                ", orderJdPrice='" + orderJdPrice + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderIndex='" + orderIndex + '\'' +
                ", jdOrderId='" + jdOrderId + '\'' +
                ", payType=" + payType +
                '}';
    }
@SerializedName("order_quan_price")
public String orderDaiGouQuanMoney;
    /**
     *用户支付的金额
     */
    @SerializedName("total_cash")
    public String totalCash;
    /**
     *运费
     */
    @SerializedName("freight")
    public String freight;
    /**
     *赠送的益豆
     */
    @SerializedName("return_bean")
    public String returnBean;
    /**
     *商品的金额
     */
    @SerializedName("orderJdPrice")
    public String orderJdPrice;
    /**
     *益联的长订单号
     */
    @SerializedName("order_id")
    public String orderId;
    /**益联短的订单号
     */
    @SerializedName("order_index")
    public String orderIndex;
    /**京东订单号
     */
    @SerializedName("jdOrderId")
    public String jdOrderId;
    /**
     * '1' => '支付宝支付',
     '2' => 'App内微信支付',
     '3' => '微信公众号支付',
     '4' => '银联支付',
     "5" => "APP红包支付",
     "6" => '开联通支付',
     "7" => '快钱支付',
     "8" => '通联支付',
     "9" => '易通支付'
     */
    @SerializedName("paymentType")
    public int payType;
    @SerializedName("paymentTypeStr")
    public String payTypeStr;

}
