package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/15 0015.
 */

public class LuckyMemberShowPrizeRecordEntity extends HttpResultBean {

    /**
     * code :
     * user_name :
     * phone :
     * photo :
     * list : [{"isSelf":"","user_name":"","user_id":"","photo":"","time":"","snatch_name":"","snatch_issue":"","comment_index":"","comment_images":"","comment_content":"","snatch_announce_time":"","win_num":"","snatch_log":[{"ip":"","count":"","time":""},{}]},{}]
     */

    @SerializedName("user_name")
    public String userName;
    @SerializedName("phone")
    public String phone;
    @SerializedName("photo")
    public String photo;
    @SerializedName("list")
    public List<SnatchShowListEntity.SnatchInfoBean> list;

}
