package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by @author zhaiyaohua on 2018/1/22 0022.
 */

public class DrivingLicenseEntity {

    @SerializedName("outputs")
    public List<Outputs> outputs;

    public static class Outputs {
        /**
         * outputLabel : ocr_vehicle
         * outputMulti : {}
         * outputValue : {"dataType":50,"dataValue":"{\"config_str\":\"\",\"engine_num\":\"\",\"issue_date\":\"\",\"model\":\"\",\"owner\":\"\",\"plate_num\":\"\",\"register_date\":\"\",\"request_id\":\"20180122210949_9b0d268447f7e21b1a603a2a031cc53c\",\"success\":true,\"vehicle_type\":\"\",\"vin\":\"\"}"}
         */

        @SerializedName("outputLabel")
        public String outputLabel;
        @SerializedName("outputMulti")
        public OutputMulti outputMulti;
        @SerializedName("outputValue")
        public OutputValue outputValue;

        public static class OutputMulti {
        }

        public static class OutputValue {
            /**
             * dataType : 50
             * dataValue : {"config_str":"","engine_num":"","issue_date":"","model":"","owner":"","plate_num":"","register_date":"","request_id":"20180122210949_9b0d268447f7e21b1a603a2a031cc53c","success":true,"vehicle_type":"","vin":""}
             */

            @SerializedName("dataType")
            public int dataType;
            @SerializedName("dataValue")
            public String dataValue;


        }
    }
}
