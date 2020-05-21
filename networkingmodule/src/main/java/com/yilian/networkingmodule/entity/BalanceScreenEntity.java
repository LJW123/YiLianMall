package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2017/7/8 0008.
 */
public class BalanceScreenEntity extends BaseEntity {
    @SerializedName("data")
    public List<ScreenEntity> data;

    public class ScreenEntity {
        @SerializedName("type")
        public String type;
        @SerializedName("typeValue")
        public String typeValue;
    }
}
