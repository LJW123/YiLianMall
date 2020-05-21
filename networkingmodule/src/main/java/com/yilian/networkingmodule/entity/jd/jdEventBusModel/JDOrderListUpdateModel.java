package com.yilian.networkingmodule.entity.jd.jdEventBusModel;

import java.io.Serializable;

/**
 * 订单列表更新控制
 * Created by @author Zg on 2018/5/30.
 */

public class JDOrderListUpdateModel implements Serializable {

    /**
     * 支付
     */
    public static final int HandleType_pay = 111;
    /**
     * 确认收货
     */
    public static final int HandleType_receiving = 222;
    /**
     * 取消订单
     */
    public static final int HandleType_cancle = 333;
    /**
     * 删除订单
     */
    public static final int HandleType_delete = 22222;

    /**
     * 添加新订单
     */
    public static final int HandleType_add = 444;

    /**
     * 操作类型
     */
    public int handleType;
    /**
     * 订单ID 用于更新对比
     */
    public String jdOrderId;
    /**
     * 订单状态类型
     */
//    public int updateOrderType;

}
