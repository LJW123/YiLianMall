package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/5/15 0015.
 */
public class GroupsBannerEntity  extends BaseEntity{

    /**
     * code :
     * data : {"group_banner":""}
     */

    @SerializedName("code")
    public String codeX;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * group_banner :
         */

        @SerializedName("group_banner")
        public String groupBanner;
    }
}
