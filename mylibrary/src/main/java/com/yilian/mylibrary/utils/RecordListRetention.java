package com.yilian.mylibrary.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 记录列表
 * Created by Zg on 2018/9/5
 */

public class RecordListRetention {
    /**
     * 我的兑换券 明细记录
     */
    public static final int EXCHANGE_MINE = 0;
    /**
     * 服务中心带核销 明细记录
     */
    public static final int EXCHANGE_VERIFICATION = 1;
    /**
     * 待提取乐天使
     */
    public static final int WAIT_EXTRACT_LE_ANGEL = 2;

    /**
     * 明细列表类型
     */
    @IntDef({EXCHANGE_MINE, EXCHANGE_VERIFICATION,WAIT_EXTRACT_LE_ANGEL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Record {
    }
    public static final int TYPE_105=105;
    public static final int TYPE_4=4;
}
