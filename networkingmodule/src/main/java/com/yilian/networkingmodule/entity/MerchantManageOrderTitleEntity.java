package com.yilian.networkingmodule.entity;

/**
 * Created by  on 2017/10/13 0013.
 */

public class MerchantManageOrderTitleEntity extends MerchantManageOrderBaseEntity {
    public int count;
    public String title;

    public MerchantManageOrderTitleEntity(int count, String title) {
        this.count = count;
        this.title = title;
    }

    @Override
    public int getItemType() {
        return MerchantManageOrderBaseEntity.TITLE;
    }
}
