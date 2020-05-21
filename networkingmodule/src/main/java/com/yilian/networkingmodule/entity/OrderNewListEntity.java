package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by liuyuqi on 2017/9/5 0005.
 */

public class OrderNewListEntity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;

    public  class DataBean {
        /**
         * id : 13
         * push_title : 您的配送套餐商家已配送
         * title : 配送套餐-已配送
         * sub_title : 七月的店
         * desc : 带你去看星星
         * sub_desc : 配送至：河南省郑州市管城回族区正商·和谐大厦66栋66层6号
         * region_type : 1
         * type : 14
         * data : 440
         * owner_type : 1
         * owner_id : 198
         * owner_name : 七月的店
         * image : supplier/images/6acc97d71da571d59bf2274539d76c3b2a720dd0_IMG20170811210335.jpg
         * to_consumer : 6851521739209407
         * push_time : 1504581840
         * push_status : 1
         * push_result : {"ios":{"result":"NoTargetDeviceToken"},"android":{"result":"ok","contentId":"OSL-0905_NY05Lt0ObJ7v05J70TofX5","details":{"318abdd96d5cf299c7b3fdbf2951f04c":"successed_online"}}}
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
        @SerializedName("type")
        public int type;
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
        @SerializedName("region_type")
        public int regionType;//1商城 2套餐

    }

}
