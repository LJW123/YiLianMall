package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;


/**
 * 判断当前登陆用户是否关联购物卡
 * Created by Zg on 2018/11/18
 */
public class CheckShopCardRelEntity extends HttpResultBean {
    /**
     *    1未关联购物卡            2已关联购物卡
     */
    @SerializedName("isOpenCard")
    public int isOpenCard;

}
