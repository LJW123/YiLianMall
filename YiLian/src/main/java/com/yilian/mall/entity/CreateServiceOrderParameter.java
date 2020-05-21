package com.yilian.mall.entity;

/**
 * Created by Administrator on 2016/2/21.
 */
public class CreateServiceOrderParameter{

    public String order_index;//订单号
    public String order_goods_index;//商品单规格订单自增编号
    public String order_goods_sku;//商品sku
    public String order_goods_norms;//商品sku描述
    public String service_total_price;//退款订单总价

    public int service_type;//服务类型 0 返修换货 1退货
    public int service_goods_count;//申请数量
    public String service_remark;//问题描述
    public String service_images;//问题情况描述图片地址拼接的字符串 "url1,url2"
    public int barter_type;//售后方式 1换货 2返修 注：返修换货时必须传 退货可以不设置该参数
    public String after_sale_type;//可以申请那种售后 0原始售后状态，默认值。1可退,可换，可修。2可换，可修。3可修。4不可售后

    @Override
    public String toString() {
        return "CreateServiceOrderParameter{" +
                "order_index='" + order_index + '\'' +
                ", order_goods_index='" + order_goods_index + '\'' +
                ", order_goods_sku='" + order_goods_sku + '\'' +
                ", order_goods_norms='" + order_goods_norms + '\'' +
                ", service_type=" + service_type +
                ", service_total_price='" + service_total_price + '\'' +
                ", service_goods_count=" + service_goods_count +
                ", service_remark='" + service_remark + '\'' +
                ", service_images='" + service_images + '\'' +
                ", barter_type=" + barter_type +
                '}';
    }
}
