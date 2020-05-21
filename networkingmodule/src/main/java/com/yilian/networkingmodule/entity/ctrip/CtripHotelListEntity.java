package com.yilian.networkingmodule.entity.ctrip;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;


/**
 * 携程 酒店列表
 *
 * @author Zg
 */
public class CtripHotelListEntity extends HttpResultBean {
    /**
     * 1定位出的数据 0没有定位
     */
    @SerializedName("location")
    public int location;
    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * {
         * HotelID: "430214", // 酒店id
         * HotelName: "北京长峰假日酒店", // 酒店名字
         * StarRatingDec: "高档型", // 档型
         * AdjacentIntersection: "近武警总医院，301医院，五棵松地铁站，五棵松体育馆", // 最近的一个路口
         * CtripUserRating: "4", // 评分
         * CtripUserRatingsDec: "很棒", // 评分描述
         * image: "https://dimg12.c-ctrip.com/images/fd/hotel/g4/M07/92/1B/CggYHFXp_yCAYEQnAASutyPwC6k860_R_550_412.jpg", // 图片
         * ThemesName: "休闲度假,看动物,商务出行,特价频道", // 逗号分隔的酒店主题
         * MinPrice: "513", // 起价(元)
         * returnBean: "25.65",  // 赠送的益豆
         * gdLng: "116.265188", // 经纬度
         * gdLat: "39.908991", // 经纬度
         * Distance: "12.83", // 距离市中心的距离(km) 经纬度都为0时使用 一直返回都有
         * juli: "1289" // 距离定位的位置(m) 只有经纬度都大于0时返回 使用
         * }
         */
        @SerializedName("HotelID")
        public String HotelID;
        @SerializedName("HotelName")
        public String HotelName;
        @SerializedName("StarRatingDec")
        public String StarRatingDec;
        @SerializedName("AdjacentIntersection")
        public String AdjacentIntersection;
        @SerializedName("CtripUserRating")
        public String CtripUserRating;
        @SerializedName("CtripUserRatingsDec")
        public String CtripUserRatingsDec;
        @SerializedName("image")
        public String image;
        @SerializedName("ThemesName")
        public String ThemesName;
        @SerializedName("MinPrice")
        public String MinPrice;
        @SerializedName("returnBean")
        public String returnBean;
        @SerializedName("gdLng")
        public double gdLng;
        @SerializedName("gdLat")
        public double gdLat;
        @SerializedName("Distance")
        public String Distance;
        @SerializedName("juli")
        public String juli;

    }

}
