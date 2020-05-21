package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/10/31 0031
 */

public class LuckyNumLogListEntity extends HttpResultBean {

    @SerializedName("snatch_name")
    public String snatch_name;
    @SerializedName("snatch_issue")
    public String snatch_issue;
    @SerializedName("snatch_record")
    public ArrayList<SnatchRecord> snatch_record;

    public class SnatchRecord {
        @SerializedName("record_index")
        public String record_index;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("snatch_time")
        public String snatch_time;
        @SerializedName("snatch_man_time")
        public String snatch_man_time;
        @SerializedName("snatch_nums")
        public String snatch_nums;
    }
}
