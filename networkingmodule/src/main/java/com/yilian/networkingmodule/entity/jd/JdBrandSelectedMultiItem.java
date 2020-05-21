package com.yilian.networkingmodule.entity.jd;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author Created by zhaiyaohua on 2018/6/19.
 */

public abstract class JdBrandSelectedMultiItem implements MultiItemEntity {
    public static final int ITEM_TYPE_ONE=1;
    public static final int ITEM_TYPE_THREE=3;
    public abstract int getSpanSize();
}
