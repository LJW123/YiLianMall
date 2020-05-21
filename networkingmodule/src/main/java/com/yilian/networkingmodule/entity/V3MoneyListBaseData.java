package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author Created by  on 2017/12/13.
 */

public abstract class V3MoneyListBaseData implements MultiItemEntity {
    public static final int LIST_CONTENT = 0;
    public static final int LIST_TITLE = 1;

    @Override
    public abstract int getItemType();
}
