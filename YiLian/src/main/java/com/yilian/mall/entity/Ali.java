package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ray_L_Pain on 2017/2/22 0022.
 */

public class Ali {

    /**
     * outputLabel : ocr_id
     * outputMulti : {}
     * outputValue : {"dataType":50,"dataValue":"{\"address\":\"郑州市中原区须水镇二砂村204号\",\"birth\":\"19930307\",\"config_str\":\"{\\\"side\\\":\\\"face\\\"}\",\"name\":\"刘玉琦\",\"nationality\":\"汉\",\"num\":\"410102199303070141\",\"request_id\":\"20170223104624_9f7c89ec58934527ec91b12564d5316b\",\"sex\":\"女\",\"success\":true}\n"}
     */

    @SerializedName("outputs")
    public List<OutputsBean> outputs;

    public static class OutputsBean {
        @SerializedName("outputLabel")
        public String outputLabel;
        @SerializedName("outputMulti")
        public OutputMultiBean outputMulti;
        /**
         * dataType : 50
         * dataValue : {"address":"郑州市中原区须水镇二砂村204号","birth":"19930307","config_str":"{\"side\":\"face\"}","name":"刘玉琦","nationality":"汉","num":"410102199303070141","request_id":"20170223104624_9f7c89ec58934527ec91b12564d5316b","sex":"女","success":true}

         */

        @SerializedName("outputValue")
        public OutputValueBean outputValue;

        public static class OutputMultiBean {
        }

        public static class OutputValueBean {
            @SerializedName("dataType")
            public int dataType;
            @SerializedName("dataValue")
            public String dataValue;
        }
    }
}
