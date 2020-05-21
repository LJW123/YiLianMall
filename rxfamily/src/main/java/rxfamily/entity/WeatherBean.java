package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class WeatherBean extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("aqi")
        public String aqi;
        @SerializedName("city")
        public String city;
        @SerializedName("temp")
        public String temp;
        @SerializedName("weather")
        public String weather;
    }
}
