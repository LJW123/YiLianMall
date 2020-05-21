package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by  on 2017/8/24 0024.
 */

public class GameListEntity extends HttpResultBean {

    @SerializedName("banner")
    public List<BannerBean> banner;
    @SerializedName("data")
    public List<DataBean> data;

    public static class BannerBean {
        /**
         * id : 27
         * banner_photo : admin/images/b94c48421edb2fcc1e50678f20609c297662fc16_38dbb6fd5266d0169b27e181962bd40735fa3558.jpg
         * banner_content : http://www.baidu.com
         * banner_type : 5
         * banner_owner_id : 0
         * banner_region : 0
         * sort : 23
         * addtime : 1503623831
         * is_show : 1
         * titles : 好搜
         * is_del : 0
         */

        @SerializedName("id")
        public String id;
        @SerializedName("banner_photo")
        public String bannerPhoto;
        @SerializedName("banner_content")
        public String bannerContent;
        @SerializedName("banner_type")
        public String bannerType;
        @SerializedName("banner_owner_id")
        public String bannerOwnerId;
        @SerializedName("banner_region")
        public String bannerRegion;
        @SerializedName("sort")
        public String sort;
        @SerializedName("addtime")
        public String addtime;
        @SerializedName("is_show")
        public String isShow;
        @SerializedName("titles")
        public String titles;
        @SerializedName("is_del")
        public String isDel;
    }

    public static class DataBean {
        /**
         * id : 5
         * name : 魂斗罗
         * icon : admin/images/5ba25d9986dedcc2a0cdb1946f7ef48c90861819_死神觉醒.png
         * desc :
         * ios_package : {"boundle_id":"","schemes":"","url":"https://app.i1170.com"}
         * android_package : {"package_name":"","action":"","category":"","url":""}
         * html5_url :
         * is_online : 1
         * sort : 1503652205
         * play_count : 0
         * download_count : 0
         * time : 1503652205
         */

        @SerializedName("game_id")
        public String gameId;
        @SerializedName("name")
        public String name;
        @SerializedName("icon")
        public String icon;
        @SerializedName("desc")
        public String desc;
        @SerializedName("ios_package")
        public String iosPackage;
        @SerializedName("android_package")
        public String androidPackage;
        @SerializedName("html5_url")
        public String html5Url;
        @SerializedName("is_online")
        public String isOnline;
        @SerializedName("sort")
        public String sort;
        @SerializedName("play_count")
        public long playCount;
        @SerializedName("download_count")
        public long downloadCount;
        @SerializedName("time")
        public String time;
    }
}
