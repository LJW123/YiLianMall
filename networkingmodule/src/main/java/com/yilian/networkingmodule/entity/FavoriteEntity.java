package com.yilian.networkingmodule.entity;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2017/8/16 0016.
 */

public class FavoriteEntity extends BaseEntity {

    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
        /**
         * collect_index : 360
         * collect_id : 1404
         * collect_name : 小店
         * collect_icon : app/images/mall/20170814/6293052682777777_5449125_image.jpg
         * collect_type : 1
         * collect_time : 1502953766
         * status : 1
         * grade_count : 3
         * grade_score : 50
         * latitude : 34.74310534298388
         * longitude : 113.7778397005235
         * address : 建正东方中心
         * industry : 火锅
         * mer_discount : 50
         * type : 2
         */

        @SerializedName("return_bean")
        public String returnBean;
        @SerializedName("is_fan")
        public String isFan;
        @SerializedName("collect_cost")
        public String collectCost;
        @SerializedName("return_integral")
        public String returnIntegral;
        @SerializedName("collect_price")
        public String collectPrice;
        @SerializedName("goods_integral")
        public String goodsIntegral;
        @SerializedName("tags_Name")
        public String tagsName;
        @SerializedName("collect_index")
        public String collectIndex;
        @SerializedName("collect_id")
        public String collectId;
        @SerializedName("filiale_id")
        public String filialeId;
        @SerializedName("collect_name")
        public String collectName;
        @SerializedName("collect_icon")
        public String collectIcon;
        @SerializedName("collect_type")
        public String collectType;
        @SerializedName("collect_time")
        public String collectTime;
        @SerializedName("status")
        public String status;
        @SerializedName("grade_count")
        public String gradeCount;
        @SerializedName("grade_score")
        public String gradeScore;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("address")
        public String address;
        @SerializedName("industry")
        public String industry;
        @SerializedName("mer_discount")
        public String merDiscount;
        @SerializedName("type")
        public String type;

        public boolean isCheck=false;//是否选中，默认没有选中
        public boolean isShowCheck=false;//是否选中，默认没有选中

    }
}
