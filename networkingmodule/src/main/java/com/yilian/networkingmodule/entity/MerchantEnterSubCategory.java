package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author Created by  on 2018/2/5.
 */

public abstract class MerchantEnterSubCategory implements MultiItemEntity {
    public static final int SUB_2_CATEGORY = 3;
    public static final int SUB_3_CATEGORY = 1;

    public abstract int getSpanSize();
}
