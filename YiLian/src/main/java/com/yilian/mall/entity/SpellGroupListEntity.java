package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2017/5/16 0016.
 */

public class SpellGroupListEntity extends com.yilian.networkingmodule.entity.BaseEntity {

    /**
     * group_banner : [{"id":"1","img":"mallAdmin/images/e9512c89504c5204a50008d14577a929588261b7_081916-53351-600x300.jpg","status":"1","title":" 火火火火火","content":"","type":"100000"}]
     * time : 1495612621
     * data : [{"goods_id":"4867","goods_sku":"[{\"sku_index\":\"7132\",\"sku_number\":11}]","amount":"2","title":"有奖拼团","intro":"哈哈哈哈哈哈哈","prize_number":"11","start_time":"1495438207","end_time":"1495555200","group_condition":"5","time_limit":"100","people_limit":"1","is_red_envelope":"0","is_direct_buy":"1","total_number":"1001","done_number":"0","fail_number":"0","progress_number":"0","status":"2","goods_icon":"goods/147832599919206.jpg","goods_name":"欧根纱短裙_古谷浪半身裙2016新款女士欧根纱包臀裙高腰裙","goods_price":"6500","index":"3","goods_label":"抽奖团"}]
     * list : [{"consumer_id":"5937326847064817","activity_id":"2","group_id":"7","user_name":"鲁光远","goods_name":"adidasi-韩国进口棉袜_韩国进口女士棉袜beimon品牌短袜时尚全棉防臭休闲","vouchar_time":"1495610877","type":"20","content":"2"},{"consumer_id":"5702006601841715","activity_id":"1","group_id":"8","user_name":"cactus","goods_name":null,"vouchar_time":"0","type":"20","content":"1"},{"consumer_id":"5937326847064817","activity_id":"3","group_id":"13","user_name":"鲁光远","goods_name":"欧根纱短裙_古谷浪半身裙2016新款女士欧根纱包臀裙高腰裙","vouchar_time":"1495590834","type":"20","content":"3"}]
     */

    @SerializedName("time")
    public int time;
    @SerializedName("group_banner")
    public List<GroupBannerBean> groupBanner;
    @SerializedName("data")
    public List<DataBean> data;
    @SerializedName("list")
    public List<NoticeViewEntity> list;

    public  class GroupBannerBean {
        /**
         * id : 1
         * img : mallAdmin/images/e9512c89504c5204a50008d14577a929588261b7_081916-53351-600x300.jpg
         * status : 1
         * title :  火火火火火
         * content :
         * type : 100000
         */

        @SerializedName("id")
        public String id;
        @SerializedName("img")
        public String img;
        @SerializedName("status")
        public String status;
        @SerializedName("title")
        public String title;
        @SerializedName("content")
        public String content;
        @SerializedName("type")
        public String type;
    }

    public  class DataBean {
        /**
         * goods_id : 4867
         * goods_sku : [{"sku_index":"7132","sku_number":11}]
         * amount : 2
         * title : 有奖拼团
         * intro : 哈哈哈哈哈哈哈
         * prize_number : 11
         * start_time : 1495438207
         * end_time : 1495555200
         * group_condition : 5
         * time_limit : 100
         * people_limit : 1
         * is_red_envelope : 0
         * is_direct_buy : 1
         * total_number : 1001
         * done_number : 0
         * fail_number : 0
         * progress_number : 0
         * status : 2
         * goods_icon : goods/147832599919206.jpg
         * goods_name : 欧根纱短裙_古谷浪半身裙2016新款女士欧根纱包臀裙高腰裙
         * goods_price : 6500
         * index : 3
         * goods_label : 抽奖团
         */

        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_sku")
        public String goodsSku;
        @SerializedName("amount")
        public String amount;
        @SerializedName("title")
        public String title;
        @SerializedName("intro")
        public String intro;
        @SerializedName("prize_number")
        public String prizeNumber;
        @SerializedName("start_time")
        public String startTime;
        @SerializedName("end_time")
        public String endTime;
        @SerializedName("group_condition")
        public String groupCondition;
        @SerializedName("time_limit")
        public String timeLimit;
        @SerializedName("people_limit")
        public String peopleLimit;
        @SerializedName("is_red_envelope")
        public String isRedEnvelope;
        @SerializedName("is_direct_buy")
        public String isDirectBuy;
        @SerializedName("total_number")
        public String totalNumber;
        @SerializedName("done_number")
        public String doneNumber;
        @SerializedName("fail_number")
        public String failNumber;
        @SerializedName("progress_number")
        public String progressNumber;
        @SerializedName("status")
        public String status;
        @SerializedName("goods_icon")
        public String goodsIcon;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("index")
        public String index;
        @SerializedName("goods_label")
        public String goodsLabel;
    }

}
