package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @atuhor Created by  on 2017/10/30 0030.
 */

public class LuckyMainHeaderEntity extends HttpResultBean {

    /**
     * data : {"bannerList":[{"banner_data":"http://www.baidu.com","banner_type":"0","image_url":"admin/images/a168999ce6d67f942c35afc6dcb6fa6c1522fb68_2.jpg","titles":"无与伦比"},{"banner_data":"","banner_type":"1","image_url":"admin/images/e8b450a54c70cb24360e36174575cb666d8cbf04_1.jpg","titles":"好酒不错"},{"banner_data":"http://www.lefenmall.com","banner_type":"0","image_url":"uploads/lefen/20160825/20160825141345_914455.jpg","titles":""},{"banner_data":"","banner_type":"1","image_url":"uploads/lefen/20160825/20160825141220_543582.png","titles":""},{"banner_data":"1","banner_type":"1","image_url":"admin/images/ef4448484508d5f1a1191b7fb3f7f38ebd38c224_1.jpg","titles":"asdasd"}],"iconList":[{"content":0,"name":"最新揭晓","pic":"https://img.yilian.lefenmall.com/app/merchant/20170613/20171023144044.png","type":30},{"content":1,"name":"中奖纪录","pic":"https://img.yilian.lefenmall.com/app/merchant/20170613/20171023144048.png","type":30},{"content":2,"name":"嗮单","pic":"https://img.yilian.lefenmall.com/app/merchant/20170613/20171023144048.png","type":30},{"content":3,"name":"全部","pic":"https://img.yilian.lefenmall.com/app/merchant/20170613/20171023144048.png","type":30}],"newsList":[{"award_time":4564654564,"goods_name":"苹果偶手机","snatch_index":1,"user_id":6308691186529419,"user_name":"151****0379"}]}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("bannerList")
        public List<BannerListBean> bannerList;
        @SerializedName("iconList")
        public List<IconListBean> iconList;
        @SerializedName("newsList")
        public ArrayList<NewsListBean> newsList;
        @SerializedName("addressList")
        public ArrayList<AddressListBean> addressList;

        public static class BannerListBean {
            /**
             * banner_data : http://www.baidu.com
             * banner_type : 0
             * image_url : admin/images/a168999ce6d67f942c35afc6dcb6fa6c1522fb68_2.jpg
             * titles : 无与伦比
             */

            @SerializedName("banner_data")
            public String bannerData;
            @SerializedName("banner_type")
            public int bannerType;
            @SerializedName("image_url")
            public String imageUrl;
            @SerializedName("titles")
            public String titles;
        }

        public static class IconListBean {
            /**
             * content : 0
             * name : 最新揭晓
             * pic : https://img.yilian.lefenmall.com/app/merchant/20170613/20171023144044.png
             * type : 30
             */
            @SerializedName("content")
            public String content;
            @SerializedName("name")
            public String name;
            @SerializedName("pic")
            public String pic;
            @SerializedName("type")
            public int type;
        }

        public static class NewsListBean {
            /**
             * award_time : 4564654564
             * goods_name : 苹果偶手机
             * snatch_index : 1
             * user_id : 6308691186529419
             * user_name : 151****0379
             */
            @SerializedName("ipAddress")
            public String ipAddress;
            @SerializedName("award_time")
            public long awardTime;
            @SerializedName("goods_name")
            public String goodsName;
            @SerializedName("snatch_index")
            public String snatchIndex;
            @SerializedName("user_id")
            public long userId;
            @SerializedName("user_name")
            public String userName;

            @Override
            public String toString() {
                return "NewsListBean{" +
                        "awardTime=" + awardTime +
                        ", goodsName='" + goodsName + '\'' +
                        ", snatchIndex='" + snatchIndex + '\'' +
                        ", userId=" + userId +
                        ", userName='" + userName + '\'' +
                        '}';
            }
        }

        public static class AddressListBean {
            @SerializedName("user_name")
            public String userName;
            @SerializedName("award_linkman")
            public String awardLinkman;
            @SerializedName("user_id")
            public String userId;
            @SerializedName("goods_name")
            public String goodsName;
            @SerializedName("time")
            public String time;
            @SerializedName("snatch_index")
            public String snatchIndex;
            @SerializedName("award_address")
            public String awardAddress;
            @SerializedName("award_city")
            public String awardCity;
        }
    }
}
