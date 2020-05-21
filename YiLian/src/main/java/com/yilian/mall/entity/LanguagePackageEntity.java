package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2017/8/3 0003.
 * 语言包实体类
 */

public class LanguagePackageEntity {


    /**
     * identifier : yilian
     * ip : http://test.i1170.com/
     * onlineIp : https://on.i1170.com/
     * testIp : http://test.i1170.com/
     * bIp:https://b.i1170.com/ 压测服
     */

    @SerializedName("identifier")
    public String identifier;
    @SerializedName("ip")
    public String ip;
    @SerializedName("onlineIp")
    public String onlineIp;
    @SerializedName("testIp")
    public String testIp;
    @SerializedName("bIp")
    public String bIp;

    @Override
    public String toString() {
        return "LanguagePackageEntity{" +
                "identifier='" + identifier + '\'' +
                ", ip='" + ip + '\'' +
                ", onlineIp='" + onlineIp + '\'' +
                ", testIp='" + testIp + '\'' +
                '}';
    }
}
