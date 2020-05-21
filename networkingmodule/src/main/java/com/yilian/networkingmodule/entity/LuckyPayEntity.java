package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/10/30 0030
 */

public class LuckyPayEntity extends HttpResultBean {

    @SerializedName("activity_id")
    public String activity_id;
    @SerializedName("snatch_issue")
    public String snatch_issue;
    @SerializedName("snatch_num")
    public String snatch_num;
    @SerializedName("snatch_man_time")
    public String snatch_man_time;
    @SerializedName("snatch_name")
    public String snatch_name;
    @SerializedName("snatch_goods_url")
    public String snatch_goods_url;
    @SerializedName("snatch_index")
    public String snatch_index;
}
