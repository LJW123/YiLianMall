package com.yilianmall.merchant.event;

/**
 * Created by  on 2017/10/12 0012.
 * * 商家未处理订单备货事件
 */

public class RemoveMerchantManageOrderList {
    public static final int TYPE_STOCK = 1;//代表刷新的是待备货列表
    public static final int TYPE_SEND = 2;//代表刷新的是待发货列表
    public static final int TYPE_RECIVE = 3;//代表刷新的是待收货列表
    public static final int TYPE_DONE = 4;//代表刷新的是已完成列表
    public int position;
    public int type = 1;//请求订单的类型 //请求订单的类型 1待备货(已取消) 2待发货 3待收货 4已完成 用来保证刷新列表EventBus的秩序

    public RemoveMerchantManageOrderList(int position, int type) {
        this.position = position;
        this.type = type;
    }
}
