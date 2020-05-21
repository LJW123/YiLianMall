package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2017/5/5 0005.
 */
public class MerchantPhotoAlbumName extends BaseEntity {

    /**
     * data : {"group_name":[{"group_id":"","merchant_id":"","group_name":""},{}]}
     */

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("group_name")
        public List<GroupNameBean> groupName;

        public class GroupNameBean {
            /**
             * group_id :
             * merchant_id :
             * group_name :
             */

            @SerializedName("group_id")
            public String groupId;
            @SerializedName("merchant_id")
            public String merchantId;
            @SerializedName("group_name")
            public String groupName;
        }
    }
}
