package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ${zhaiyaohua} on 2017/12/26 0026.
 * @author zhaiyaohua
 */

public class JPOnlineMallBanner  {


    /**
     * activity_banner : [{"hot_type":"1","hot_content":[{"type":"0","content":"https://app.i1170.com/wechat/m/activity/twelve/twelve.php"}],"name":"iphoneX","type":"0","image":"admin/images/99ea441cb4e709670d5e4ca65aa0dc9d02085328_1.jpg","content":"https://app.i1170.com/wechat/m/activity/twelve/twelve.php"}]
     * icon_class : [{"name":"女装","type":"9","image":"admin/images/a3067ff5523fff362b971dbcd0ade403f4a2d166_1.png","content":"34"}]
     * advert : {"hot_type":"1"," hot_content":[{"type":"0","content":"https://app.i1170.com/wechat/m/activity/twelve/twelve.php"}],"name":"12 12","type":"0","image":"admin/images/b033655dea699470ddd98220f21321d5aaa48b6e_1.png","content":"https://app.i1170.com/wechat/m/activity/twelve/twelve.php"}
     * advert_two : {}
     *  icon_bg : {"image":"admin/images/78f2e1bb2e16b2511b4b9098ded49b8b81833fb2_1.jpg"}
     * activity_prefecture : {"sub_title":"活动专区","sub_title_pic":"","bg":"","left":{"type":"","content":"","image":""},"right_top":{"type":"","content":"","image":""},"right_right":{"type":"","content":"","image":""}}
     */

    @SerializedName("advert")
    public Advert advert;
    @SerializedName("advert_two")
    public AdvertTwo advertTwo;
    @SerializedName(" icon_bg")
    public iconBg iconBg;
    @SerializedName("activity_prefecture")
    public ActivityPrefecture activityPrefecture;
    @SerializedName("activity_banner")
    public List<ActivityBanner> activityBanner;
    @SerializedName("icon_class")
    public List<IconClass> iconClass;

    public static class Advert extends OnlineMallCategoryData{
        /**
         * hot_type : 1
         *  hot_content : [{"type":"0","content":"https://app.i1170.com/wechat/m/activity/twelve/twelve.php"}]
         * name : 12 12
         * type : 0
         * image : admin/images/b033655dea699470ddd98220f21321d5aaa48b6e_1.png
         * content : https://app.i1170.com/wechat/m/activity/twelve/twelve.php
         */

        @SerializedName("hot_type")
        public String hotType;
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public String type;
        @SerializedName("image")
        public String image;
        @SerializedName("content")
        public String content;
        @SerializedName(" hot_content")
        public List<hotContent> hotContent;

        @Override
        public int getItemType() {
            return OnlineMallCategoryData.ITEM_TYPE_WEIT_FOUR;
        }

        @Override
        public int getSpanSize() {
            return OnlineMallCategoryData.ITEM_SPAN_SIZE_FOUR;
        }

        public static class hotContent {
        }
    }

    public static class AdvertTwo {
    }

    public static class iconBg  {
        /**
         * image : admin/images/78f2e1bb2e16b2511b4b9098ded49b8b81833fb2_1.jpg
         */

        @SerializedName("image")
        public String image;
    }

    public static class ActivityPrefecture  extends OnlineMallCategoryData{
        /**
         * sub_title : 活动专区
         * sub_title_pic :
         * bg :
         * left : {"type":"","content":"","image":""}
         * right_top : {"type":"","content":"","image":""}
         * right_right : {"type":"","content":"","image":""}
         */

        @SerializedName("sub_title")
        public String subTitle;
        @SerializedName("sub_title_pic")
        public String subTitlePic;
        @SerializedName("bg")
        public String bg;
        @SerializedName("left")
        public Left left;
        @SerializedName("right_top")
        public RightTop rightTop;
        @SerializedName("right_right")
        public RightRight rightRight;

        @Override
        public int getItemType() {
            return OnlineMallCategoryData.ITEM_TYPE_WEIT_TWO;
        }

        @Override
        public int getSpanSize() {
            return OnlineMallCategoryData.ITEM_SPAN_SIZE_TWO;
        }

        public static class Left {
            /**
             * type :
             * content :
             * image :
             */

            @SerializedName("type")
            public String type;
            @SerializedName("content")
            public String content;
            @SerializedName("image")
            public String image;
        }

        public static class RightTop {
            /**
             * type :
             * content :
             * image :
             */

            @SerializedName("type")
            public String type;
            @SerializedName("content")
            public String content;
            @SerializedName("image")
            public String image;
        }

        public static class RightRight {
            /**
             * type :
             * content :
             * image :
             */

            @SerializedName("type")
            public String type;
            @SerializedName("content")
            public String content;
            @SerializedName("image")
            public String image;
        }
    }

    public static class ActivityBanner  extends OnlineMallCategoryData{
        /**
         * hot_type : 1
         * hot_content : [{"type":"0","content":"https://app.i1170.com/wechat/m/activity/twelve/twelve.php"}]
         * name : iphoneX
         * type : 0
         * image : admin/images/99ea441cb4e709670d5e4ca65aa0dc9d02085328_1.jpg
         * content : https://app.i1170.com/wechat/m/activity/twelve/twelve.php
         */

        @SerializedName("hot_type")
        public String hotType;
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public String type;
        @SerializedName("image")
        public String image;
        @SerializedName("content")
        public String content;
        @SerializedName("hot_content")
        public List<HotContent> hotContent;

        @Override
        public int getItemType() {
            return OnlineMallCategoryData.ITEM_TYPE_WEIT_ONE;
        }

        @Override
        public int getSpanSize() {
            return OnlineMallCategoryData.ITEM_SPAN_SIZE_THREE;
        }

        public static class HotContent {
            /**
             * type : 0
             * content : https://app.i1170.com/wechat/m/activity/twelve/twelve.php
             */

            @SerializedName("type")
            public String type;
            @SerializedName("content")
            public String content;
        }
    }

    public static class IconClass {
        /**
         * name : 女装
         * type : 9
         * image : admin/images/a3067ff5523fff362b971dbcd0ade403f4a2d166_1.png
         * content : 34
         */

        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public String type;
        @SerializedName("image")
        public String image;
        @SerializedName("content")
        public String content;
    }
}
