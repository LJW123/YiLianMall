package com.yilian.mall.entity;/**
 * Created by  on 2017/3/4 0004.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/3/4 0004.
 * 阿里客服信息
 */
public class AliCustomerServiceInfo extends BaseEntity {

    /**
     * data : {"person_id":"","group_id":""}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * person_id :
         * group_id :
         */

        @SerializedName("person_id")
        public String personId;
        @SerializedName("group_id")
        public String groupId;
    }
}
