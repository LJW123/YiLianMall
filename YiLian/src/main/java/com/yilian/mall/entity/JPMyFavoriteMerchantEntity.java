package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2016/10/25 0025.
 */

public class JPMyFavoriteMerchantEntity extends BaseEntity {

    /**
     * collect_index :
     * collect_id :
     * collect_name :
     * collect_icon :
     * collect_type :
     * collect_time :
     * collect_price :
     * collect_cost :
     * grade_count :
     * latitude :
     * longitude :
     * address :
     * industry :
     */

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        @SerializedName("collect_index")
        public String collectIndex;
        @SerializedName("collect_id")
        public String collectId;
        @SerializedName("collect_name")
        public String collectName;
        @SerializedName("collect_icon")
        public String collectIcon;
        @SerializedName("collect_type")
        public String collectType;
        @SerializedName("collect_time")
        public String collectTime;
        @SerializedName("collect_price")
        public String collectPrice;
        @SerializedName("collect_cost")
        public String collectCost;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("grade_count")
        public String gradeCount;
        @SerializedName("grade_score")
        public String gradeScore;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("address")
        public String address;
        @SerializedName("industry")
        public String industry;
        @SerializedName("status")
        public String status;
        @SerializedName("mer_discount")
        public String merDiscount;

        @Override
        public String toString() {
            return "ListBean{" +
                    "collectIndex='" + collectIndex + '\'' +
                    ", collectId='" + collectId + '\'' +
                    ", collectName='" + collectName + '\'' +
                    ", collectIcon='" + collectIcon + '\'' +
                    ", collectType='" + collectType + '\'' +
                    ", collectTime='" + collectTime + '\'' +
                    ", collectPrice='" + collectPrice + '\'' +
                    ", collectCost='" + collectCost + '\'' +
                    ", gradeCount='" + gradeCount + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", address='" + address + '\'' +
                    ", industry='" + industry + '\'' +
                    '}';
        }
    }
}
