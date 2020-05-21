package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/12/21 0021
 */

public class V5ComboListEntity extends HttpResultBean {

    @SerializedName("info")
    public InfoBean info;

    @SerializedName("login_status")
    public String loginStatus;
    @SerializedName("yet_collect")
    public String yetCollect;

    public static class InfoBean {
        @SerializedName("act_banner")
        public List<Banner> bannerList;

        public class Banner {
            @SerializedName("name")
            public String name;
            @SerializedName("type")
            public int type;
            @SerializedName("image")
            public String image;
            @SerializedName("content")
            public String content;
        }
        @SerializedName("supplier_isExist")
        public int supplierIsExist;  //线上店铺是否存在 0不 1存在
        @SerializedName("supplier_id")
        public String supplierId;  //线上店铺的id
        @SerializedName("supplier_name")
        public String supplierName;  //线上店铺的名字

        @SerializedName("countMerchantAlbum")
        public String countMerchantAlbum; //商家相册图片总数量
        @SerializedName("permission")
        public String permission;
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("praise_count")
        public String praiseCount;
        @SerializedName("merchant_lebi_percent")
        public String merchantLebiPercent;
        @SerializedName("merchant_type")
        public String merchantType;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("merchant_tel")
        public String merchantTel;
        @SerializedName("merchant_percent")
        public String merchantPercent;
        @SerializedName("merchant_province")
        public String merchantProvince;
        @SerializedName("merchant_city")
        public String merchantCity;
        @SerializedName("merchant_county")
        public String merchantCounty;
        @SerializedName("merchant_address")
        public String merchantAddress;
        @SerializedName("merchant_longitude")
        public String merchantLongitude;
        @SerializedName("merchant_latitude")
        public String merchantLatitude;
        @SerializedName("merchant_industry")
        public String merchantIndustry;
        @SerializedName("merchant_industry_parent")
        public String merchantIndustryParent;
        @SerializedName("merchant_scope")
        public String merchantScope;
        @SerializedName("merchant_desp")
        public String merchantDesp;
        @SerializedName("merchant_worktime")
        public String merchantWorktime;
        @SerializedName("merchant_regtime")
        public String merchantRegtime;
        @SerializedName("renqi")
        public String renqi;
        @SerializedName("industry_name")
        public String industryName;
        @SerializedName("graded")
        public String graded;
        @SerializedName("is_praise")
        public int isPraise;
        @SerializedName("send_lefen_count")
        public String sendLefenCount;
        @SerializedName("deal_count")
        public int dealCount;
        @SerializedName("comments_count")
        public String commentsCount;
        /**
         * 商家让利百分比
         */
        @SerializedName("mer_discount")
        public String merDiscount;
        /**
         * photo_name : filedata1
         * photo_path : merchant_image/20161128/20161128094707_1428180.jpg
         */

        @SerializedName("images")
        public List<ImagesBean> images;
        /**
         * package_id : 4
         * package_icon : merchant/images/b3a6d6a34aec4acad619c230a228757fdc640338_5.jpg
         * name : 胖哥酸菜鱼
         * amount : 6300
         * sell_count : 0
         */

        @SerializedName("packages")
        public List<PackageBean> packages;
        /**
         * name : ❤❤❤测试心
         * evaluate : 15
         * commit_date : 1481091166
         * taste : 0
         * environment : 50
         * service : 30
         * consumer_comment : 测试评价1111
         * merchant_comment :
         * sell_count : 0
         * album : ["ceshitupianllalal"]
         */

        @SerializedName("comments")
        public List<CommentsBean> comments;

        public static class ImagesBean {
            @SerializedName("photo_name")
            public String photoName;
            @SerializedName("photo_path")
            public String photoPath;
        }

        public static class PackageBean {
            @SerializedName("package_id")
            public String packageId;
            @SerializedName("package_icon")
            public String packageIcon;
            @SerializedName("name")
            public String name;
            @SerializedName("price")
            public String price;
            @SerializedName("sell_count")
            public int sellCount;
            @SerializedName("return_integral") //套餐返奖券
            public String returnIntegral;
        }

        public static class CommentsBean {
            @SerializedName("name")
            public String name;
            @SerializedName("image_url")
            public String imageurl;
            @SerializedName("evaluate")
            public String evaluate;
            @SerializedName("commit_date")
            public String commitDate;
            @SerializedName("taste")
            public String taste;
            @SerializedName("environment")
            public String environment;
            @SerializedName("service")
            public String service;
            @SerializedName("consumer_comment")
            public String consumerComment;
            @SerializedName("merchant_comment")
            public String merchantComment;
            @SerializedName("sell_count")
            public int sellCount;
            @SerializedName("album")
            public String album;
        }

        @Override
        public String toString() {
            return "InfoBean{" +
                    "permission='" + permission + '\'' +
                    ", merchantId='" + merchantId + '\'' +
                    ", praiseCount='" + praiseCount + '\'' +
                    ", merchantLebiPercent='" + merchantLebiPercent + '\'' +
                    ", merchantType='" + merchantType + '\'' +
                    ", merchantImage='" + merchantImage + '\'' +
                    ", merchantName='" + merchantName + '\'' +
                    ", merchantTel='" + merchantTel + '\'' +
                    ", merchantPercent='" + merchantPercent + '\'' +
                    ", merchantProvince='" + merchantProvince + '\'' +
                    ", merchantCity='" + merchantCity + '\'' +
                    ", merchantCounty='" + merchantCounty + '\'' +
                    ", merchantAddress='" + merchantAddress + '\'' +
                    ", merchantLongitude='" + merchantLongitude + '\'' +
                    ", merchantLatitude='" + merchantLatitude + '\'' +
                    ", merchantIndustry='" + merchantIndustry + '\'' +
                    ", merchantIndustryParent='" + merchantIndustryParent + '\'' +
                    ", merchantScope='" + merchantScope + '\'' +
                    ", merchantDesp='" + merchantDesp + '\'' +
                    ", merchantWorktime='" + merchantWorktime + '\'' +
                    ", merchantRegtime='" + merchantRegtime + '\'' +
                    ", renqi='" + renqi + '\'' +
                    ", industryName='" + industryName + '\'' +
                    ", graded=" + graded +
                    ", isPraise=" + isPraise +
                    ", sendLefenCount='" + sendLefenCount + '\'' +
                    ", dealCount=" + dealCount +
                    ", commentsCount='" + commentsCount + '\'' +
                    ", images=" + images +
                    ", packageX=" + packages +
                    ", comments=" + comments +
                    '}';
        }
    }
}
