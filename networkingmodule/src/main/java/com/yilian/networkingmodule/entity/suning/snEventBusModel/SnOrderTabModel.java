package com.yilian.networkingmodule.entity.suning.snEventBusModel;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 苏宁订单Fragment tab 控制
 * Created by @author Zg on 2018/8/1.
 */

public class SnOrderTabModel implements Serializable {

    /**
     * 我的订单-全部订单
     */
    public static final int TAB_ALL = 0;
    /**
     * 我的订单-待支付
     */
    public static final int TAB_PAY = 1;
    /**
     * 我的订单-待收货
     */
    public static final int TAB_RECEIVE = 2;
    /**
     * 我的订单-已完成
     */
    public static final int TAB_FINISH = 3;

    /**
     * 我的订单-已取消
     */
    public static final int TAB_CANCEL = 4;
    /**
     * 我的订单-申请售后
     */
    public static final int TAB_APPLY_AFTER_SALE = 5;

    /**
     * 我的订单-申请售后 可申请
     */
    public static final int TAB_APPLY_AFTER_SALE_CAN = 0;
    /**
     * 我的订单-申请售后 记录
     */
    public static final int TAB_APPLY_AFTER_SALE_RECORD = 1;

    /**
     * 我的订单 所显示的TAB
     */
    public int orderTab;
    /**
     * 我的订单-申请售后 所显示的tab
     */
    public int afterSaleTab;


    public SnOrderTabModel(@OrderTab int orderTab, @AfterSaleTab int afterSaleTab) {
        this.orderTab = orderTab;
        this.afterSaleTab = afterSaleTab;
    }

    /**
     * 我的订单显示tab
     *
     * @author Created by Zg on 2018/7/31.
     */
    @IntDef({TAB_ALL, TAB_PAY, TAB_RECEIVE, TAB_FINISH, TAB_CANCEL, TAB_APPLY_AFTER_SALE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrderTab {
        public class TAB_BRAND_SELECTED {
        }
    }

    /**
     * 申请售后显示tan
     *
     * @author Created by Zg on 2018/7/31.
     */
    @IntDef({TAB_APPLY_AFTER_SALE_CAN, TAB_APPLY_AFTER_SALE_RECORD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AfterSaleTab {
        public class TAB_BRAND_SELECTED {
        }
    }
}
