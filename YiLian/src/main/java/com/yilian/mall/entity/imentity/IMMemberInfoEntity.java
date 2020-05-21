package com.yilian.mall.entity.imentity;

import com.google.gson.annotations.SerializedName;
import com.yilian.mall.entity.BaseEntity;

/**
 * author XiuRenLi on 2016/8/25  PRAY NO BUG
 */

public class IMMemberInfoEntity extends BaseEntity {

    /**
     * username :
     * head :
     * reg_time :
     * lev :
     * member_buy_lefen :
     * member_buy_lebi :
     */

    @SerializedName("username")
    public String username;
    @SerializedName("head")
    public String head;
    @SerializedName("reg_time")
    public String regTime;
    @SerializedName("lev")
    public String lev;
    @SerializedName("member_buy_lefen")
    public String memberBuyLefen;
    @SerializedName("member_buy_lebi")
    public String memberBuyLebi;
}
