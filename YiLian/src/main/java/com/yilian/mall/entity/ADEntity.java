package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author XiuRenLi  PRAY NO BUG
 * Created by Administrator on 2016/8/4.
 */
public class ADEntity extends BaseEntity {
    /**
     * adv : {"addtime":"1499753823","banner_content":"https://img.yilian.lefenmall.com/file/news/20170706044513.html","banner_owner_id":"0","banner_photo":"admin/images/f489f8cf0168d8e078d4090d939da2577b97be2a_TB2Csjka5RnpuFjSZFCXXX2DXXa_!!72.jpg","banner_region":"0","banner_type":"3","content":"https://img.yilian.lefenmall.com/file/news/20170706044513.html","id":"17","image_url":"admin/images/f489f8cf0168d8e078d4090d939da2577b97be2a_TB2Csjka5RnpuFjSZFCXXX2DXXa_!!72.jpg","is_del":"0","is_show":"1","sort":"100","titles":"哈哈哈哈哈","type":0}
     * data : {"content":"","image_url":"","show_time":0,"type":1}
     * notice : {"content":"https://img.yilian.lefenmall.com/http://img.yilian.lefenmall.com/http://img.yilian.lefenmall.com/http://img.yilian.lefenmall.com/http://img.yilian.lefenmall.com/http://img.yilian.lefenmall.com/file/news/20170704062218.html","intro":"zheshiyigejianjie哈哈哈哈","show":1,"title":"公告"}
     */

//    @SerializedName("adv")
//    public AdvDataBean advDataBean;
//    @SerializedName("data")
//    public DataBean data;

    @SerializedName("data")
    public DataBean data;

    /**
     * 启动广告
     */
    public class DataBean {
        /**
         * type : 类型  1 URL  2. 商品详情页 3. 商家详情页面 4.线上店铺
         * image_url :图片URL
         * content :内容
         */
        @SerializedName("type")
        public String type;
        @SerializedName("image_url")
        public String imageUrl;
        @SerializedName("content")
        public String content;
        @SerializedName("show_time")
        public String showTime;

        @Override
        public String toString() {
            return "DataBean{" +
                    "type='" + type + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", content='" + content + '\'' +
                    ", showTime='" + showTime + '\'' +
                    '}';
        }
    }

    @SerializedName("adv")
    public AdvDataBean advDataBean;

    /**
     * APP主页显示的推送活动
     */
    public class AdvDataBean implements Serializable {
        @SerializedName("type")
        public String type;//  0URL 1. 商品详情页 2. 商家详情页面（只会是总部商品 兑换中心id 是 0或者1）  3.旗舰店详情
        @SerializedName("image_url")
        public String advImageUrl;//图片URL
        @SerializedName("content")
        public String advContent; //内容
        @SerializedName("is_show")
        public String isShow;//0不显示  1显示
        @SerializedName("version")
        public String version;//广告版本号

        @Override
        public String toString() {
            return "AdvDataBean{" +
                    "type='" + type + '\'' +
                    ", advImageUrl='" + advImageUrl + '\'' +
                    ", advContent='" + advContent + '\'' +
                    '}';
        }
    }

    @SerializedName("notice")
    public HomePageNoticeBean notice;

    public static class HomePageNoticeBean implements Serializable {
        /**
         * id:每次广告的ID 用来判断不在提示公告后，发新公告是进行显示
         * content : https://img.yilian.lefenmall.com/http://img.yilian.lefenmall.com/http://img.yilian.lefenmall.com/http://img.yilian.lefenmall.com/http://img.yilian.lefenmall.com/http://img.yilian.lefenmall.com/file/news/20170704062218.html
         * intro : zheshiyigejianjie哈哈哈哈
         * show : 1
         * title : 公告
         */
        @SerializedName("id")
        public String noticeId;
        @SerializedName("content")
        public String content;
        @SerializedName("intro")
        public String intro;
        @SerializedName("show")
        public int show;
        @SerializedName("title")
        public String title;

        @Override
        public String toString() {
            return "HomePageNoticeBean{" +
                    "content='" + content + '\'' +
                    ", intro='" + intro + '\'' +
                    ", show=" + show +
                    ", title='" + title + '\'' +
                    '}';
        }
    }


}

