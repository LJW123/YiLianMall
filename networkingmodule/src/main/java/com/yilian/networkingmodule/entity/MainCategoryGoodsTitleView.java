package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by  on 2017/8/1 0001.
 */

public class MainCategoryGoodsTitleView extends MainCategoryData implements MultiItemEntity {
    public final String title;

    public MainCategoryGoodsTitleView(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return MainCategoryData.TITLE;
    }

    @Override
    public int getSpanSize() {
        return MainCategoryData.TITLE_SIZE;
    }
}
