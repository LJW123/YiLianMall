package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import rxfamily.entity.BaseEntity;

/**
 * Created by @author zhaiyaohua on 2018/1/22 0022.
 */

public class DrivingEntity  extends BaseEntity{
    /**
     * outputLabel : ocr_vehicle_license
     * outputMulti : {}
     * outputValue : {"dataType":50,"dataValue":{"config_str":"null","owner":"张三","plate_num":"沪A0M084","vehicle_type":"小型轿车","vin":"LSVFF66R8C2116280","engine_num":"416098","register_date":"20121127","request_id":"84701974fb983158_2016052610011","success":true}}
     */
    @SerializedName("outputs")
    List<OutputMultis> outputs;

    public static class OutputMultis{
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
             * dataValue : {"config_str":"null","owner":"张三","plate_num":"沪A0M084","vehicle_type":"小型轿车","vin":"LSVFF66R8C2116280","engine_num":"416098","register_date":"20121127","request_id":"84701974fb983158_2016052610011","success":true}
             */

            @SerializedName("dataType")
            public int dataType;
            @SerializedName("dataValue")
            public DataValue dataValue;

            public static class DataValue {
                /**
                 * config_str : null
                 * owner : 张三
                 * plate_num : 沪A0M084
                 * vehicle_type : 小型轿车
                 * vin : LSVFF66R8C2116280
                 * engine_num : 416098
                 * register_date : 20121127
                 * request_id : 84701974fb983158_2016052610011
                 * success : true
                 */

                @SerializedName("config_str")
                public String configStr;
                @SerializedName("owner")
                public String owner;
                @SerializedName("plate_num")
                public String plateNum;
                @SerializedName("vehicle_type")
                public String vehicleType;
                @SerializedName("vin")
                public String vin;
                @SerializedName("engine_num")
                public String engineNum;
                @SerializedName("register_date")
                public String registerDate;
                @SerializedName("request_id")
                public String requestId;
                @SerializedName("success")
                public boolean success;
            }
        }
    }
}
