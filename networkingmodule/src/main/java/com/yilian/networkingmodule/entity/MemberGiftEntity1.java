package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Administrator on 2016/5/10.
 */
public class MemberGiftEntity1 extends HttpResultBean {

    /**
     * name : 寒冬腊月
     * photo : app/images/head/20171210/6291685602586311_1971698_headIcon.jpg
     * be_userid : 6291685602586311
     * availableLebi : 0
     * lev : 1
     * availableLefen : 752169
     * availableIntegral : 752169
     * poundagePercent : 5
     */

    @SerializedName("name")
    public String name;
    @SerializedName("photo")
    public String photo;
    @SerializedName("be_userid")
    public String beUserid;
    @SerializedName("availableLebi")
    public String availableLebi;
    @SerializedName("lev")
    public int lev;
    @SerializedName("availableLefen")
    public String availableLefen;
    @SerializedName("availableIntegral")
    public String availableIntegral;
    @SerializedName("poundagePercent")
    public String poundagePercent;
}
