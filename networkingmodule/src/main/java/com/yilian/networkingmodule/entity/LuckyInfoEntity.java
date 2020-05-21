package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/10/30 0030
 */

public class LuckyInfoEntity extends HttpResultBean {

    @SerializedName("server_time")
    public String server_time;
    @SerializedName("snatch_info")
    public SnatchInfo snatch_info;

    public class SnatchInfo {
        /**
         * 看到该详情的用户是否能参加该活动 0 不可以参加 1 可以参加 该值得优先级大于snatch_status
         */
        @SerializedName("flg")
        public int flag;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("snatch_xj_title")
        public String snatchXjTitle;
        @SerializedName("snatch_xj_desc")
        public String snatchXjDesc;
        @SerializedName("snatch_goods_introduce")
        public ArrayList<String> snatch_goods_introduce;
        @SerializedName("snatch_status") //状态1表示正在进行，3表示已经结束开奖   4标识没有下一期   5也没有下一期  紧急下架状态
        public String snatch_status;
        @SerializedName("activity_id")
        public String activity_id;
        @SerializedName("snatch_issue")
        public String snatch_issue;
        @SerializedName("activity_name")
        public String activity_name;
        @SerializedName("snatch_subhead")
        public String snatch_subhead;
        @SerializedName("snatch_start_time")
        public String snatch_start_time;
        @SerializedName("snatch_once_expend")
        public String snatch_once_expend;
        @SerializedName("snatch_announce_time")
        public String snatch_announce_time;
        @SerializedName("snatch_server_time")
        public String snatch_server_time;
        @SerializedName("snatch_goods_id")
        public String snatch_goods_id;
        @SerializedName("snatch_goods_album")
        public List<String> snatch_goods_album;
        @SerializedName("snatch_total_count")
        public String snatch_total_count;
        @SerializedName("snatch_play_count")
        public String snatch_play_count;
        @SerializedName("extra_bean")
        public String extraBean;


        @Override
        public String toString() {
            return "SnatchInfo{" +
                    "snatch_status='" + snatch_status + '\'' +
                    ", activity_id='" + activity_id + '\'' +
                    ", snatch_issue='" + snatch_issue + '\'' +
                    ", activity_name='" + activity_name + '\'' +
                    ", snatch_subhead='" + snatch_subhead + '\'' +
                    ", snatch_start_time='" + snatch_start_time + '\'' +
                    ", snatch_once_expend='" + snatch_once_expend + '\'' +
                    ", snatch_announce_time='" + snatch_announce_time + '\'' +
                    ", snatch_server_time='" + snatch_server_time + '\'' +
                    ", snatch_goods_id='" + snatch_goods_id + '\'' +
                    ", snatch_goods_album=" + snatch_goods_album +
                    ", snatch_total_count='" + snatch_total_count + '\'' +
                    ", snatch_play_count='" + snatch_play_count + '\'' +
                    ", extra_bean='" + extraBean + '\'' +
                    '}';
        }
    }

    @SerializedName("snatch_log")
    public ArrayList<SnatchLog> snatch_log;

    public class SnatchLog {
        @SerializedName("user_id")
        public String userId;
        @SerializedName("photo")
        public String photo;
        @SerializedName("user_name")
        public String user_name;
        @SerializedName("ip")
        public String ip;
        @SerializedName("ip_address")
        public String ip_address;
        @SerializedName("count")
        public String count;
        @SerializedName("time")
        public String time;

        @Override
        public String toString() {
            return "SnatchLog{" +
                    "photo='" + photo + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", ip='" + ip + '\'' +
                    ", ip_address='" + ip_address + '\'' +
                    ", count='" + count + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }
    }

    @SerializedName("user_play_count") //用户已参与人次 -1 表示用户未登录
    public String user_play_count;

    @SerializedName("win_log")
    public WinLog win_log;

    public class WinLog {
        @SerializedName("user_name")
        public String user_name;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("phone")
        public String phone;
        @SerializedName("count")
        public String count;
        @SerializedName("time")
        public String time;
        @SerializedName("win_num")
        public String win_num;
        @SerializedName("photo")
        public String photo;

        @Override
        public String toString() {
            return "WinLog{" +
                    "user_name='" + user_name + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", phone='" + phone + '\'' +
                    ", count='" + count + '\'' +
                    ", time='" + time + '\'' +
                    ", win_num='" + win_num + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LuckyInfoEntity{" +
                "server_time='" + server_time + '\'' +
                ", snatch_info=" + snatch_info +
                ", snatch_log=" + snatch_log +
                ", user_play_count='" + user_play_count + '\'' +
                ", win_log=" + win_log +
                '}';
    }
}
