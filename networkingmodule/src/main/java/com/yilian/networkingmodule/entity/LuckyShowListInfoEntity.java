package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/10/31 0031.
 */

public class LuckyShowListInfoEntity extends HttpResultBean {

    @SerializedName("user_name")
    public String user_name;
    @SerializedName("photo")
    public String photo;
    @SerializedName("user_id")
    public String user_id;
    @SerializedName("time")
    public String time;
    @SerializedName("snatch_name")
    public String snatch_name;
    @SerializedName("snatch_issue")
    public String snatch_issue;
    @SerializedName("comment_images")
    public String comment_images;
    @SerializedName("comment_content")
    public String comment_content;
    @SerializedName("snatch_announce_time")
    public String snatch_announce_time;
    @SerializedName("win_num")
    public String win_num;
    @SerializedName("snatch_count")
    public String snatch_count;

}
