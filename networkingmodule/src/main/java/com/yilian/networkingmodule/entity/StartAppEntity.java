package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

public class StartAppEntity extends HttpResultBean {

    /**
     * 登录状态,0表示失效,1表示有效
     */
    @SerializedName("login_status")
    public String loginStatus;

    /**
     * APP 最新版本号
     */
    @SerializedName("newest_version")
    public String newestVersion;

    /**
     * 盐值
     */
    @SerializedName("server_salt")
    public String serverSalt;

    /**
     * 如果登录失效,当前登录的设备名称
     */
    @SerializedName("login_device_name")
    public String loginDeviceName;

    /**
     * 如果登录失效,异地登录时间
     */
    @SerializedName("login_time")
    public String loginTime;


}
