package com.yilian.mall.entity.imentity;

import com.google.gson.annotations.SerializedName;
import com.yilian.mall.entity.BaseEntity;

/**
 * author XiuRenLi on 2016/8/23  PRAY NO BUG
 */

public class IMGroupInfoEntity extends BaseEntity {


    /**
     * message : 群组信息
     * data : {"superior":{"status":1,"user_id":"3579684699619719","group_id":"1f914154-68d7-11e6-b26e-a45e60efbdcf","group_name":"我的乐友群"},"lower":{"status":1,"user_id":"3579727966844818","group_id":"bc625611-68d7-11e6-b08c-a45e60efbdcf","group_name":"我的乐友群"}}
     * request : POST /app/im.php?c=get_group
     */

    /**
     * superior : {"status":1,"user_id":"3579684699619719","group_id":"1f914154-68d7-11e6-b26e-a45e60efbdcf","group_name":"我的乐友群"}
     * lower : {"status":1,"user_id":"3579727966844818","group_id":"bc625611-68d7-11e6-b08c-a45e60efbdcf","group_name":"我的乐友群"}
     */

    @SerializedName("data")
    public DataBean data;
    @SerializedName("request")
    public String request;

    @Override
    public String toString() {
        return "IMGroupInfoEntity{" +
                "message='" + message + '\'' +
                ", data=" + data.toString() +
                ", request='" + request + '\'' +
                '}';
    }

    public static class DataBean {
        /**
         * status : 1
         * user_id : 3579684699619719
         * group_id : 1f914154-68d7-11e6-b26e-a45e60efbdcf
         * group_name : 我的乐友群
         */

        @SerializedName("superior")
        public SuperiorBean superior;
        /**
         * status : 1
         * user_id : 3579727966844818
         * group_id : bc625611-68d7-11e6-b08c-a45e60efbdcf
         * group_name : 我的乐友群
         */

        @SerializedName("lower")
        public LowerBean lower;

        @Override
        public String toString() {
            return "ListBean{" +
                    "superior=" + superior.toString() +
                    ", lower=" + lower.toString() +
                    '}';
        }

        public static class SuperiorBean {
            @SerializedName("status")
            public int status;
            @SerializedName("user_id")
            public String userId;
            @SerializedName("group_id")
            public String groupId;
            @SerializedName("group_name")
            public String groupName;

            @Override
            public String toString() {
                return "SuperiorBean{" +
                        "status=" + status +
                        ", userId='" + userId + '\'' +
                        ", groupId='" + groupId + '\'' +
                        ", groupName='" + groupName + '\'' +
                        '}';
            }
        }

        public static class LowerBean {
            @SerializedName("status")
            public int status;
            @SerializedName("user_id")
            public String userId;
            @SerializedName("group_id")
            public String groupId;
            @SerializedName("group_name")
            public String groupName;

            @Override
            public String toString() {
                return "LowerBean{" +
                        "status=" + status +
                        ", userId='" + userId + '\'' +
                        ", groupId='" + groupId + '\'' +
                        ", groupName='" + groupName + '\'' +
                        '}';
            }
        }
    }
}
