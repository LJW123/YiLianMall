package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ray_L_Pain on 2016/10/21 0021.
 */

public class JPFlagshipEntity extends BaseEntity {


    /**
     * image_url :
     * tags_name :
     * tags_id :
     * address：店铺地址
     * feedbackRate：好评率
     * mostBean：最高赠送的益豆
     * frequent_phone：客服电话
     * supplier_icon：店铺logo
     * suppliers : [{"goods_name":"","supplier_id":"","tags_name":"","goods_id":"","goods_price":"","goods_cost":"","sell_count":"","image_url":"","goods_online":""},{}]
     */

    @SerializedName("data")
    public DataBean data;

    public class DataBean implements Serializable {
        private static final long serialVersionUID = 1L;
        @SerializedName("image_url")
        public String imageUrl;
        @SerializedName("tags_name")
        public String tagsName;
        @SerializedName("tags_id")
        public String tagsId;
        @SerializedName("tags_mark")
        public String tagsMark;
        @SerializedName("is_collect")
        public int isCollect;
        @SerializedName("supplier_id")
        public String supplierId;
        @SerializedName("supplier_name")
        public String supplierName;
        @SerializedName("notice_mark")
        public String noticeMark;
        @SerializedName("notice_content")
        public String noticeContent;
        @SerializedName("address")
        public String address;
        @SerializedName("feedbackRate")
        public String feedBackRate;
        @SerializedName("mostBean")
        public String mostBean;
        @SerializedName("frequent_phone")
        public String frequentPhone;
        @SerializedName("supplier_icon")
        public String supplierIcon;
        @SerializedName("max_ratio")
        public String maxRatio;

    }
}
