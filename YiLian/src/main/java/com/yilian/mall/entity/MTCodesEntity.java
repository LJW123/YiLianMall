package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ray_L_Pain on 2016/12/7 0007.
 */

public class MTCodesEntity implements Serializable {

    //券码
    @SerializedName("code")
    public String code;

    //券码状态 1未使用 2已使用 3待退款 4已退款
    @SerializedName("status")
    public String status;

    @Override
    public String toString() {
        return "MTCodesEntity{" +
                "code='" + code + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
