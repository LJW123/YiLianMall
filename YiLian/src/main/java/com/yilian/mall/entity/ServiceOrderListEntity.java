package com.yilian.mall.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/2/21.
 */
public class ServiceOrderListEntity extends BaseEntity {

    public List<ServiceOrderList> list;

    class ServiceOrderList {

        public String service_id;//退款订单号
        public String type;//售后类型 0换货返修 1退货
        public String order_id;//退款订单号
        public String status;//订单状态：0已取消，1待审核2审核拒绝3待退货4维修中/换货中5待发货6待收货7已完成
        public String goods_price;//退货商品单价
        public String goods_count;//退货数量
        public String goods_id;//商品唯一索引
        public String goods_name;//商品名称
        public String goods_icon;//商品缩略图
        public String goods_norms;//商品sku描述
        public String service_time;//申请时间
}

}
