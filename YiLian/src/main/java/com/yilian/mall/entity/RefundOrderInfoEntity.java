package com.yilian.mall.entity;

/**
 * Created by Administrator on 2016/2/21.
 */
public class RefundOrderInfoEntity extends BaseEntity {

    public Refund refund;

    class Refund {
        public String refund_index;//退货编号
        public String refund_id;//订单id
        public String refund_status;//退货状态
        public String refund_remark;//退货备注
        public String refund_images;//退货原因照片
        public String refund_refuse;//退货拒绝原因 订单状态：0已取消，1待审核2审核拒绝3待退货4待退款5已完成
        public String refund_time;//申请时间
        public String payment_type;//返款款类型
        public String payment_deal_id;//流水编号
        public String payment_time;//返款时间
        public String refund_goods_price;//申请退货商品价格
        public String refund_goods_count;//申请退货商品数量
        public String refund_goods_index;//商品编号
        public String refund_goods_sku;//商品sku
        public String refund_total_price;//退款总价
        public String express_time;//返货时间
        public String express_company;//物流公司名称
        public String express_number;//物流编号
        public String confirm_time;//确认收货时间
    }
}

