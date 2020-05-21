package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/7/24 0024.
 */

public class SearchListEntity extends BaseEntity {

    @SerializedName("list")
    public ArrayList<JPGoodsEntity> list;

}
