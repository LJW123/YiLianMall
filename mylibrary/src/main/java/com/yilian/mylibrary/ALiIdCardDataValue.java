package com.yilian.mylibrary;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by  on 2018/1/21.
 */

public class ALiIdCardDataValue {

    /**
     * address : 河南省**县**行政村**村**号
     * birth : 19901011
     * config_str : {"side":"face"}
     * face_rect : {"angle":-2.5470666885375977,"center":{"x":2356.500244140625,"y":731.5},"size":{"height":512.6051635742188,"width":564.7776489257812}}
     * name : 李**
     * nationality : 汉
     * num : 4127*********583X
     * request_id : 20180121004802_743d3d1b1930d988337e9f91f62f0fcf
     * sex : 男
     * success : true
     */

    @SerializedName("address")
    public String address;
    @SerializedName("birth")
    public String birth;
    @SerializedName("config_str")
    public String configStr;
    @SerializedName("face_rect")
    public FaceRectBean faceRect;
    @SerializedName("name")
    public String name;
    @SerializedName("nationality")
    public String nationality;
    @SerializedName("num")
    public String num;
    @SerializedName("request_id")
    public String requestId;
    @SerializedName("sex")
    public String sex;
    @SerializedName("success")
    public boolean success;

    public static class FaceRectBean {
        /**
         * angle : -2.5470666885375977
         * center : {"x":2356.500244140625,"y":731.5}
         * size : {"height":512.6051635742188,"width":564.7776489257812}
         */

        @SerializedName("angle")
        public double angle;
        @SerializedName("center")
        public CenterBean center;
        @SerializedName("size")
        public SizeBean size;

        public static class CenterBean {
            /**
             * x : 2356.500244140625
             * y : 731.5
             */

            @SerializedName("x")
            public double x;
            @SerializedName("y")
            public double y;
        }

        public static class SizeBean {
            /**
             * height : 512.6051635742188
             * width : 564.7776489257812
             */

            @SerializedName("height")
            public double height;
            @SerializedName("width")
            public double width;
        }
    }
}
