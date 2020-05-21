package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2016/10/25 0025.
 */

public class JPMyFavoriteStorEntity extends BaseEntity {

    /**
     * collect_index :
     * collect_id :
     * collect_name :
     * collect_icon :
     * collect_type :
     * collect_time :
     * collect_price :
     * collect_cost :
     * tags_name :
     * tags_id :
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
        @SerializedName("tags_name")
        public String tagsName;
        @SerializedName("tags_id")
        public String tagsId;
        @SerializedName("status")
        public String statuts;

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
                    ", tagsName='" + tagsName + '\'' +
                    ", tagsId='" + tagsId + '\'' +
                    '}';
        }
    }
}
