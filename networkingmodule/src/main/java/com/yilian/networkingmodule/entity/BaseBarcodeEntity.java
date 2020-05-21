package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by liuyuqi on 2017/8/31 0031.
 */

public abstract class BaseBarcodeEntity implements MultiItemEntity {


    public static final int HEADVIEW = 1;
    public static final int ITEMVIEW = 2;
    public static final int BOTTOMVIEW = 3;

    @Override
    public abstract int getItemType();
}
