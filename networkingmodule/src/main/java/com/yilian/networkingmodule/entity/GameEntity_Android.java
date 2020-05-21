package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by  on 2017/8/26 0026.
 */

public class GameEntity_Android implements Serializable{

    /**
     * action :
     * category :
     * package_name :
     * url : https://itunes.apple.com/cn/app/le-fen-shang-cheng/id1039737253?mt=8
     */

    @SerializedName("schemes")
    public String scheme;
    @SerializedName("package_name")
    public String packageName;
    @SerializedName("url")
    public String url;
    @SerializedName("activity_name")
    public String startActivityName;
}
