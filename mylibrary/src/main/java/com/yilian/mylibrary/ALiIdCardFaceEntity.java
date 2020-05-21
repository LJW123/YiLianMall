package com.yilian.mylibrary;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Created by  on 2018/1/21.
 */

public class ALiIdCardFaceEntity {


    @SerializedName("outputs")
    public List<OutputsBean> outputs;

    public static class OutputsBean {
        /**
         * outputLabel : ocr_idcard
         * outputMulti : {}
         * outputValue : {"dataType":50,"dataValue":"{\"address\":\"**省**县大**行政村**村**号\",\"birth\":\"19901011\",\"config_str\":\"{\\\"side\\\":\\\"face\\\"}\",\"face_rect\":{\"angle\":-2.5470666885375977,\"center\":{\"x\":2356.500244140625,\"y\":731.5},\"size\":{\"height\":512.60516357421875,\"width\":564.77764892578125}},\"name\":\"李**\",\"nationality\":\"汉\",\"num\":\"41272119901011583X\",\"request_id\":\"20180121004802_743d3d1b1930d988337e9f91f62f0fcf\",\"sex\":\"男\",\"success\":true}"}
         */

        @SerializedName("outputLabel")
        public String outputLabel;
        @SerializedName("outputMulti")
        public OutputMultiBean outputMulti;
        @SerializedName("outputValue")
        public OutputValueBean outputValue;

        public static class OutputMultiBean {
        }

        public static class OutputValueBean {
            /**
             * dataType : 50
             * dataValue : {"address":"河南省扶沟县大李庄乡行政村大李庄村86号","birth":"19901011",
             * "config_str":"{\"side\":\"face\"}","face_rect":{"angle":-2.5470666885375977,
             * "center":{"x":2356.500244140625,"y":731.5},"size":{"height":512.60516357421875,
             * "width":564.77764892578125}},"name":"李剑威","nationality":"汉","num":"41272119901011583X",
             * "request_id":"20180121004802_743d3d1b1930d988337e9f91f62f0fcf","sex":"男","success":true}
             */

            @SerializedName("dataType")
            public int dataType;
            @SerializedName("dataValue")
            public String dataValue;
        }
    }
}
