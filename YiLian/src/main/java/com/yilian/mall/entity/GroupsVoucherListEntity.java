package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2017/5/15 0015.
 */
public class GroupsVoucherListEntity extends com.yilian.networkingmodule.entity.BaseEntity {

    /**
     * code :
     * data : {"list":[{"user_name":"","goods_name":"","vouchar_time":""},{}]}
     */

    @SerializedName("code")
    public String codeX;
    @SerializedName("data")
    public DataBean data;

    public  class DataBean {
        @SerializedName("list")
        public List<NoticeViewEntity> list;

        public  class ListBean {
            /**
             * user_name :
             * goods_name :
             * vouchar_time :
             */

        }
    }
}
