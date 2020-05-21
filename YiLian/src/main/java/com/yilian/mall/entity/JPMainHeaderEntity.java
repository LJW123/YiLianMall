package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by  on 2016/10/19 0019.
 * 新版商城 首页 包括轮播、分类图标、运营活动图片、三个活动（左边一个 右边上下结构两个）
 */

public class JPMainHeaderEntity extends BaseEntity {

    @SerializedName("data")
    public DataBean JPData;

    public class DataBean implements Serializable {


        /**
         * 轮播
         * banner_type : 1
         * banner_data :
         * image_url :
         * titles :
         */

        @SerializedName("app_banner")
        public ArrayList<JPBannerEntity> JPAppBanner;


        /**
         * 五个图标
         * type : 1
         * content :
         * image_url :
         * name :
         */

        @SerializedName("icon_class")
        public ArrayList<IconClassBean> JPIconClass;
        @SerializedName("news")
        public ArrayList<NoticeViewEntity> JPNews;
        /**
         * 显示或隐藏的运营图片
         * type : 1
         * content :
         * image_url :
         * display :
         */

        @SerializedName("activity_banner")
        public ArrayList<ActivityBannerBean> JPActivityBanner;
        /**
         * 三个活动图片
         * content : 1
         * type :
         * msg :
         * image_url :
         */

        @SerializedName("activity")
        public ArrayList<ActivityBean> JPActivity;

//        /**
//         * type://  1 扫一扫 2 每日推荐 3 幸运购 4 每日签到 5 送券商家 （6 App内URL，7有奖邀请，8跳转旗舰店列表9充值中心，10本地活动列表，11常见问题，12浏览器打开URL 13意见反馈）
//         * content: 内容
//         */
//        public class ActivityNews implements Serializable {
//            @SerializedName("type")
//            public int type;
//            @SerializedName("content")
//            public String content;
//            @SerializedName("msg")
//            public String msg;
//        }

        public class ActivityBannerBean implements Serializable {
            @SerializedName("type")
            public int JPType;
            @SerializedName("content")
            public String JPContent;
            @SerializedName("image_url")
            public String JPImageUrl;
            @SerializedName("display")
            public String JPDisplay;
        }

        public class ActivityBean implements Serializable {
            @SerializedName("content")
            public String JPContent;
            @SerializedName("type")
            public String JPType;
            @SerializedName("msg")
            public String JPMsg;
            @SerializedName("image_url")
            public String JPImageUrl;
        }
    }
}
