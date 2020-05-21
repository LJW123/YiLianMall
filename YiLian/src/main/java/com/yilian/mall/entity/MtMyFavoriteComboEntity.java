package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/6 0006.
 * 收藏套餐的实体类
 */
public class MtMyFavoriteComboEntity extends BaseEntity {

    /**
     * collect_index :
     * collect_id :
     * collect_name :
     * collect_icon :
     * collect_type :
     * collect_time :
     * collect_price :
     * latitude :
     * longitude :
     */

    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
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
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("status")
        public String status;

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
                    ", latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    '}';
        }
    }
}
