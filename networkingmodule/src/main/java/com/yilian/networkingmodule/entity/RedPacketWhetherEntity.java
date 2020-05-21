package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/11/23 0023.
 */

public class RedPacketWhetherEntity extends HttpResultBean {

    //是否弹出领奖励窗口，0不弹窗 1弹窗
    @SerializedName("isAlert")
    public String isAlert;
    //奖励id,  isAlert=0的时候，该字段不返回
    @SerializedName("cash_bonus_id")
    public String cashBonusId;

}
