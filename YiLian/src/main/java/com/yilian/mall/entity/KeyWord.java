package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by lefenandroid on 16/6/7.
 */
public class KeyWord extends BaseEntity {
    @SerializedName("key_word")
    public ArrayList<String> keyWord;
}
