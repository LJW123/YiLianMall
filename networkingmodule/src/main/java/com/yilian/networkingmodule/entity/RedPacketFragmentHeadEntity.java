package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/11/24 0024.
 */

public class RedPacketFragmentHeadEntity extends HttpResultBean {

    /**
     * banner : [{"content":"http://yilian.lefenmall.net/wechat/m/activity/wheel_v2.php","display":1,"image":"app/images/hd.png","name":"","type":"18"}]
     * illustrate :  每日凌晨5点之前，平台根据消费指数向奖券持有会员自动发放不定额奖励。会员当日首次登录APP奖励自动弹出，未能及时拆取的奖励可通过首页“奖励”进入详情页拆取。  15个自然日内未拆的奖励将失效，针对失效奖励需使用等额奖券激活方可再次拆取。
     * name : 调皮的小汽车
     * packet_exist : 1
     * packet_info : {"activate_at":"0","amount":"1508377","apply_at":"0","consumer_id":"6869464257497014","created_at":"1512414006","deduct_integral":"1675975","id":"1093","index":"2400","is_pop":"1","lost_at":"1513699200","order_id":"2017120503000688757","percent":65,"status":"0"}
     * phone : 18203660535
     * photo : app/images/head/20171109/7435908194499807_9657332_.jpg
     * share : {"content":"我刚刚在益联益家拆到一个15,083.77的大惊喜，快来试试你的吧~","img_url":"https://img.yilian.lefenmall.com/app/images/chb.png","title":"拆一拆，惊喜一下~","url":"http://yilian.lefenmall.net/wechat/m/activity/sharePacket/sharePacket.php?gain_money=15,083.77&gain_time=01-01 08:00&user_img=https://img.yilian.lefenmall.com/app/images/head/20171109/7435908194499807_9657332_.jpg&user_name=调皮的小汽车&sign=18203660535&uid=6869464257497014"}
     */

    @SerializedName("illustrate")
    public String illustrate;
    @SerializedName("name")
    public String name;
    @SerializedName("packet_exist")
    public String packetExist;
    @SerializedName("packet_info")
    public PacketInfoBean packetInfo;
    @SerializedName("phone")
    public String phone;
    @SerializedName("photo")
    public String photo;
    @SerializedName("share")
    public ShareBean share;
    @SerializedName("banner")
    public ArrayList<Banner> banner;

    public static class PacketInfoBean {
        /**
         * activate_at : 0
         * amount : 1508377
         * apply_at : 0
         * consumer_id : 6869464257497014
         * created_at : 1512414006
         * deduct_integral : 1675975
         * id : 1093
         * index : 2400
         * is_pop : 1
         * lost_at : 1513699200
         * order_id : 2017120503000688757
         * percent : 65
         * status : 0
         */

        @SerializedName("activate_at")
        public String activateAt;
        @SerializedName("amount")
        public String amount;
        @SerializedName("apply_at")
        public String applyAt;
        @SerializedName("consumer_id")
        public String consumerId;
        @SerializedName("created_at")
        public String createdAt;
        @SerializedName("deduct_integral")
        public String deductIntegral;
        @SerializedName("id")
        public String id;
        @SerializedName("index")
        public String index;
        @SerializedName("is_pop")
        public String isPop;
        @SerializedName("lost_at")
        public String lostAt;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("percent")
        public int percent;
        @SerializedName("status")
        public String status;
    }

    public static class ShareBean {
        /**
         * content : 我刚刚在益联益家拆到一个15,083.77的大惊喜，快来试试你的吧~
         * img_url : https://img.yilian.lefenmall.com/app/images/chb.png
         * title : 拆一拆，惊喜一下~
         * url : http://yilian.lefenmall.net/wechat/m/activity/sharePacket/sharePacket.php?gain_money=15,083.77&gain_time=01-01 08:00&user_img=https://img.yilian.lefenmall.com/app/images/head/20171109/7435908194499807_9657332_.jpg&user_name=调皮的小汽车&sign=18203660535&uid=6869464257497014
         */

        @SerializedName("content")
        public String content;
        @SerializedName("img_url")
        public String imgUrl;
        @SerializedName("title")
        public String title;
        @SerializedName("url")
        public String url;
    }

    public static class Banner {
        /**
         * content : http://yilian.lefenmall.net/wechat/m/activity/wheel_v2.php
         * display : 1
         * image : app/images/hd.png
         * name :
         * type : 18
         */

        @SerializedName("content")
        public String content;
        @SerializedName("display")
        public int display;
        @SerializedName("image")
        public String image;
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public int type;
    }
}
