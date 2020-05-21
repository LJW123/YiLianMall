package com.yilian.networkingmodule.entity.suning.snEventBusModel;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 订单列表更新控制  苏宁
 * Created by @author Zg on 2018/7/31.
 */

public class SnOrderListUpdateModel implements Serializable {

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
    public String snOrderId;


    /**
     * @author Created by Zg on 2018/7/31.
     */
    @IntDef({HandleType_pay, HandleType_receiving, HandleType_cancle, HandleType_delete,HandleType_add})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SnHandleType {
        public class TAB_BRAND_SELECTED {
        }
    }

    public SnOrderListUpdateModel( @SnHandleType int handleType, String snOrderId) {
        this.handleType = handleType;
        this.snOrderId = snOrderId;
    }
}
