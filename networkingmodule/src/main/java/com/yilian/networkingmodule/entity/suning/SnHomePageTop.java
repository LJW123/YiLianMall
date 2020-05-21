package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author zhaiyaohua
 *         苏宁首页头部数据
 */
public class SnHomePageTop extends HttpResultBean {
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("banner")
        public List<SnBannerEntity> banner;
        @SerializedName("icon")
        public List<SnHomePageIcon> icon;
        @SerializedName("adv")
        public List<SnAdvEntity> adv;
    }
}
