package com.yilian.mylibrary;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by  on 2018/1/21.
 */

public class AliIdCardBackDataValue {

    /**
     * config_str : {"side":"back"}
     * end_date : 20190902
     * issue : **县公安局
     * request_id : 20180121134714_393c9abfc85ae14f6778a7b2e5c62869
     * start_date : 20090902
     * success : true
     */

    @SerializedName("config_str")
    public String configStr;
    @SerializedName("end_date")
    public String endDate;
    @SerializedName("issue")
    public String issue;
    @SerializedName("request_id")
    public String requestId;
    @SerializedName("start_date")
    public String startDate;
    @SerializedName("success")
    public boolean success;
}
