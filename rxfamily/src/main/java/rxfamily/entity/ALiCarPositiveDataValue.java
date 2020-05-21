package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/21 0021.
 */

public class ALiCarPositiveDataValue {

    /**
     * config_str :
     * name : 张三三
     * num : 360502xxxx03071357
     * vehicle_type : C1
     * start_date : 2010xxxx
     * end_date : 6
     * success : true
     */
    @SerializedName("config_str")
    public String configStr;
    @SerializedName("name")
    public String name;
    @SerializedName("num")
    public String num;
    @SerializedName("vehicle_type")
    public String vehicleType;
    @SerializedName("start_date")
    public String startDate;
    @SerializedName("end_date")
    public String endDate;
    @SerializedName("success")
    public boolean success;

}
