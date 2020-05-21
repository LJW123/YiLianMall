package com.yilian.networkingmodule.entity.ctrip.ctripMultiItem;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 关键字 布局类型
 * <p>
 * Created by Zg on 2018/9/03
 */

public abstract class CtripKeywordLayoutType implements MultiItemEntity {
    // 记录
    public static final int record = 1;
    // 行政区域
    public static final int location = 2;
    // 商圈
    public static final int zone = 3;
    // 品牌
    public static final int brand = 4;
    // 酒店
    public static final int hotel = 5;
}
