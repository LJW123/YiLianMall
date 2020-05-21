package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by  on 2017/8/24 0024.
 */

public class GameListHeaderEntity extends HttpResultBean {

    /**
     * code :
     * banner : [{"id":"","banner_photo":"","banner_content":"","banner_type":"","banner_owner_id":"","banner_region":"","sort":"","addtime":"","is_show":"","titles":"","is_del":""},{}]
     */

    @SerializedName("banner")
    public List<BannerBean> banner;

    public static class BannerBean {
        /**
         * id :
         * banner_photo :
         * banner_content :
         * banner_type :
         * banner_owner_id :
         * banner_region :
         * sort :
         * addtime :
         * is_show :
         * titles :
         * is_del :
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
}
