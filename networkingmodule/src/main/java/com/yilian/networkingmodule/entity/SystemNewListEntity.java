package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by liuyuqi on 2017/9/5 0005.
 */

public class SystemNewListEntity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 3
         * push_title : aa
         * title : hkkh
         * sub_title : ccc
         * desc : yy
         * sub_desc : uooo
         * region_type : 0
         * type : 0
         * data :
         * owner_type : 0
         * owner_id : 0
         * owner_name :
         * image :
         * to_consumer : 0
         * push_time : 0
         * push_status : 0
         * push_result : qq
         */

        @SerializedName("id")
        public String id;
        @SerializedName("push_title")
        public String pushTitle;
        @SerializedName("title")
        public String title;
        @SerializedName("sub_title")
        public String subTitle;
        @SerializedName("desc")
        public String desc;
        @SerializedName("sub_desc")
        public String subDesc;
        @SerializedName("region_type")
        public int regionType;
        @SerializedName("type")
        public String type;
        @SerializedName("data")
        public String data;
        @SerializedName("owner_type")
        public String ownerType;
        @SerializedName("owner_id")
        public String ownerId;
        @SerializedName("owner_name")
        public String ownerName;
        @SerializedName("image")
        public String image;
        @SerializedName("to_consumer")
        public String toConsumer;
        @SerializedName("push_time")
        public String pushTime;
        @SerializedName("push_status")
        public String pushStatus;
        @SerializedName("push_result")
        public String pushResult;
    }
}
