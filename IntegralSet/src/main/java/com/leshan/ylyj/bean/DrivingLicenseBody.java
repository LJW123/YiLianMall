package com.leshan.ylyj.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by @author zhaiyaohua on 2018/1/22 0022.
 */

public class DrivingLicenseBody {


    @SerializedName("inputs")
    public List<Inputs> inputs;

    public static class Inputs {
        /**
         * image : {"dataType":50,"dataValue":"Base64编码的字符"}
         */

        @SerializedName("image")
        public Image image;

        public static class Image {
            /**
             * dataType : 50
             * dataValue : Base64编码的字符
             */

            @SerializedName("dataType")
            public int dataType;
            @SerializedName("dataValue")
            public String dataValue;
        }
    }
}

