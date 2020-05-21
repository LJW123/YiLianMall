package com.yilianmall.merchant.event;

/**
 * Created by  on 2017/10/12 0012.
 * * 商家未处理订单备货事件
 */

public class UpdateMerchantManageOrderList {
    public int position;
    public int orderStatus = 1;//请求订单的类型 //请求订单的类型 1待备货(已取消) 2待发货 3待收货 4已完成 用来保证刷新列表EventBus的秩序

    public UpdateMerchantManageOrderList(int position, int orderStatus) {
        this.position = position;
        this.orderStatus = orderStatus;
    }
}
