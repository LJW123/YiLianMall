package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by ${zhaiyaohua} on 2017/12/26 0026.
 * @author zhaiyaohua
 */

public abstract class OnlineMallCategoryData  implements MultiItemEntity {
    public  static final int ITEM_TYPE_WEIT_ONE=1;
    public  static final int ITEM_TYPE_WEIT_TWO=2;
    public  static final int ITEM_TYPE_WEIT_THREE=3;
    public  static final int ITEM_TYPE_WEIT_FOUR=4;


    public  static final int ITEM_SPAN_SIZE_ONE=1;
    public  static final int ITEM_SPAN_SIZE_TWO=2;
    public  static final int ITEM_SPAN_SIZE_THREE=3;
    public  static final int ITEM_SPAN_SIZE_FOUR=4;



    @Override
    public abstract int getItemType();
    public abstract int getSpanSize();

}
