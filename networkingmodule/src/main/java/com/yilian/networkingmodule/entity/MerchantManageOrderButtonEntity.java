package com.yilian.networkingmodule.entity;

/**
 * Created by  on 2017/10/13 0013.
 */

public class MerchantManageOrderButtonEntity extends MerchantManageOrderBaseEntity {
    public String parcelIndex;
    public String expressCompany;
    public String expressNum;
    public MerchantOrderEntity.GoodsBean goodsBean;
    public String orderStatus;
    public String goodStatus;

    public MerchantManageOrderButtonEntity(String orderStatus, String goodStatus) {
//        this.goodsBean = goodsBean;
        this.orderStatus = orderStatus;
        this.goodStatus = goodStatus;
    }

    public MerchantManageOrderButtonEntity(String orderStatus, String goodsStatus, String expressNum, String expressCompany, String parcelIndex) {
        this.orderStatus = orderStatus;
        this.goodStatus = goodsStatus;
        this.expressNum = expressNum;
        this.expressCompany = expressCompany;
        this.parcelIndex = parcelIndex;
    }

    @Override
    public int getItemType() {
        return MerchantManageOrderBaseEntity.BUTTON;
    }
}
