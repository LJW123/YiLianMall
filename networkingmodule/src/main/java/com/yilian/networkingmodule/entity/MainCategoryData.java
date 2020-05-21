package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by  on 2017/7/31 0031.
 */

public abstract class MainCategoryData  implements MultiItemEntity {
    protected int itemType;
    public static final int SHOP = 1;
    public static final int GOOD = 2;
    public static final int TITLE = 3;
    private int spanSize;
    public static final int TITLE_SIZE = 2;
    public static final int ITEM_SIZE = 1;
    public static final int TEXT_SPAN_SIZE = 3;
    public static final int IMG_TEXT_SPAN_SIZE = 4;
    @Override
    public abstract int getItemType() ;

    public abstract int getSpanSize();



}
