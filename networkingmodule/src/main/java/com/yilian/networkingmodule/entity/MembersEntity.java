package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ray_L_Pain on 2017/8/18 0018.
 */

public class MembersEntity implements Serializable {
    @SerializedName("member_icon")
    public String memberIcon;
    @SerializedName("member_id")
    public String memberId;
    @SerializedName("member_name")
    public String memberName;
    @SerializedName("reg_time")
    public long regTime;
    @SerializedName("rank")
    public String rank;
}
