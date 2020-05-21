package com.yilian.mylibrary;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xiaofo on 2018/8/8.
 */

public class MerchantEnterType {
    /**
     * 入驻
     */
    public static final String ENTER = "0";
    /**
     * 续费
     */
    public static final String RENEW="1";

    /**
     * 缴费类型
     */
    @StringDef({ENTER,RENEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EnterType{}
}
