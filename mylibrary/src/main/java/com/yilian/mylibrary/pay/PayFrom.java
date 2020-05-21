package com.yilian.mylibrary.pay;

/**
 * @author xiaofo on 2018/7/8.
 */

public enum PayFrom {
    /**
     * 从商品详情去支付
     */
    GOODS_DETAIL,
    /**
     * 从购物车去支付
     */
    GOODS_SHOPPING_CART,
    /**
     * 从订单去支付
     */
    GOODS_ORDER,
    /**
     * 从网页去支付
     */
    WEB_VIEW
}
