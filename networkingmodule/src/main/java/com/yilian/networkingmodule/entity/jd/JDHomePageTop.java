package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/5/22.
 * JD 首页头部数据
 */

public class JDHomePageTop extends HttpResultBean {

    /**
     * data : {"banner":[{"title":"","img":"url","type":"0","content":"https://www.baidu.com"},{"title":"","img":"url","type":"0","content":"https://www.baidu.com"}],"icon":[{"title":"新手教程","img":"url","type":"0","content":"https://www.baidu.com"},{"title":"品牌精选","img":"url","type":"0","content":"https://www.baidu.com"},{"title":"好货推荐","img":"url","type":"0","content":"https://www.baidu.com"},{"title":"每日榜单","img":"url","type":"0","content":"https://www.baidu.com"},{"title":"全部","img":"url","type":"0","content":"https://www.baidu.com"}],"adv":[{"title":"","img":"url","type":"0","content":"https://www.baidu.com"}]}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("banner")
        public List<JDBannerEntity> banner;
        @SerializedName("icon")
        public List<JDIconsEntity> icon;
        @SerializedName("adv")
        public List<JD_ADEntity> adv;
    }
}
