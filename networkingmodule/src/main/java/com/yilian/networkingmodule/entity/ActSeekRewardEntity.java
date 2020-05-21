package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by ZYH on 2017/12/17 0017.
 */

public class ActSeekRewardEntity extends HttpResultBean {

    /**
     * name : 昵称
     * award_integral : 56   分数
     */
    @SerializedName("name")
    public String name;
    @SerializedName("award_integral")
    public String awardIntegral;
}
