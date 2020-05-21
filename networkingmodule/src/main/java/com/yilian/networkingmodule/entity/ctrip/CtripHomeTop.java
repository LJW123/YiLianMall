package com.yilian.networkingmodule.entity.ctrip;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.entity.suning.SnAdvEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Zg
 *         携程 首页头部数据
 */
public class CtripHomeTop extends HttpResultBean {
    /**
     * systemTime 系统时间戳
     * checkIn 今天日期
     * checkOut 明天日期
     */
    @SerializedName("data")
    public DataBean data;
    @SerializedName("systemTime")
    public long systemTime;
    @SerializedName("checkIn")
    public long checkIn;
    @SerializedName("checkOut")
    public long checkOut;

    public static class DataBean {
        @SerializedName("banner")
        public List<BannerBean> banner;
        @SerializedName("activity")
        public List<ActivityBean> activity;
    }

    public static class BannerBean {
        /**
         * title :
         * img : admin/jd/jd_home/banner0.png
         * type : 3
         * content :
         */
        @SerializedName("title")
        public String title;
        @SerializedName("img")
        public String img;
        @SerializedName("type")
        public String type;
        @SerializedName("content")
        public String content;
    }

    public static class ActivityBean {

        /**
         * title :
         * img : admin/jd/jd_home/adv1.png
         * type : 1
         * content : http://test.i1170.com/wechat/m/activity/jingDong/shopList.php?shopType=3
         */

        @SerializedName("title")
        public String title;
        @SerializedName("img")
        public String img;
        @SerializedName("type")
        public String type;
        @SerializedName("content")
        public String content;
    }
}
