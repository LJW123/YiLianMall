package com.yilian.mall.entity;/**
 * Created by  on 2017/1/4 0004.
 */

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by  on 2017/1/4 0004.
 */
public class ShowMTPackageTicketEntity implements Serializable {
    public String packageName;//套餐名称
    public String usableTime;//套餐期限
    public ArrayList<MTCodesEntity> codes;//套餐券码集合
    public String packageOrderId;//套餐订单ID
    public String voucher;//套餐赠送零购券数量
    public String lebi;//订单总价
    public String deliveryPrice;//订单运费
    public String giveCoupon;//赠送的抵扣券
    public String isDelivery;//订单是否是配送订单 0不是 1是
    public String isEvaluate;//订单是否已经被评价
    public String status;//订单状态（待使用、等待配送、已使用等等）
    public String integral;

    @Override
    public String toString() {
        return "ShowMTPackageTicketEntity{" +
                "packageName='" + packageName + '\'' +
                ", usableTime='" + usableTime + '\'' +
                ", codes=" + codes +
                ", packageOrderId='" + packageOrderId + '\'' +
                ", voucher='" + voucher + '\'' +
                ", lebi='" + lebi + '\'' +
                ", deliveryPrice='" + deliveryPrice + '\'' +
                ", giveCoupon='" + giveCoupon + '\'' +
                ", isDelivery='" + isDelivery + '\'' +
                ", isEvaluate='" + isEvaluate + '\'' +
                ", status='" + status + '\'' +
                ", integral='" + integral + '\'' +
                '}';
    }
}
