package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 首页改版实体类
 * Created by Administrator on 2016/7/26.
 */
public class MainActivityEntity extends BaseEntity {

    @SerializedName("data")
    public DataList data;

    public class DataList{
        @SerializedName("app_banner") //首页轮播
        public ArrayList<appBanner> appBanners;
        @SerializedName("activity_banner")//主题图片
        public ArrayList<activityBanner> activityBanners;
        @SerializedName("activity_bg")//活动背景图片
        public String activity_bg;
        @SerializedName("icon_class")//分类图标
        public ArrayList<iconClass> iconClasses;

//        @SerializedName("snatch_list")
//        public ArrayList<OneBuyGoodsListEntity.ListBean> listSnatch;
        @SerializedName("lefen_info")//乐分精品
        public ArrayList<lefenInfo> lefenInfos;

        @SerializedName("snatch_display")//幸运购 0隐藏 1显示
        public String snatch_display;

        @SerializedName("recommend_display")//推荐商品 0隐藏 1显示
        public String recommend_display;

        @SerializedName("lefen_display")//乐分精品 0隐藏 1显示
        public String lefen_display;
        @SerializedName("lefenbao_display")//乐分宝 0隐藏 1显示
        public String lefenbao_display;
        @SerializedName("mer_display")//本地商家  0隐藏 1显示
        public String mer_display;


    }
    //首页轮播
    public class appBanner{
        @SerializedName("banner_type")// 0 url 1 商品id 2有奖邀请 3 幸运购原始活动id 4联盟商家id
        public int type;
        @SerializedName("banner_data")//内容
        public String banner_data;
        @SerializedName("image_url")//图片
        public String image_url;
        @SerializedName("titles")
        public String titles;
    }
    //主题图片
    public class activityBanner{
        @SerializedName("type")/// 0 url 1 商品id 2 幸运购原始活动id 3联盟商家id 4主题商品搜索（key_word:六一儿童节）
        public String type;
        @SerializedName("content")//内容
        public String content;
        @SerializedName("image_url")//图片
        public String image_url;
        @SerializedName("display")//0不显示 1显示
        public int display;
        @SerializedName("top")//0上边 1下边
        public int top;
    }
    //分类图标
    public class iconClass{
        @SerializedName("type")// 0 url（web主题，常见问题） 1 商品列表 2 兑换中心 3幸运购 4商家 5.充值中心 6.天天抽奖 7.天天夺宝 8.有奖邀请
        public String type;
        @SerializedName("content")//内容
        public String content;
        @SerializedName("image_url")//图片
        public String image_url;
        @SerializedName("name")//分类名称
        public String name;
    }
    //乐分精品
    public class lefenInfo{
        @SerializedName("type")// 0 url 1 商品列表（key_word 检索） 2 商品详情
        public String type;
        @SerializedName("content")//内容
        public String content;
        @SerializedName("image_url")//图片
        public String image_url;
        @SerializedName("name")//分类名称
        public String name;
        @SerializedName("display")//是否显示
        public String display;
    }
//    //本地商家
//    public class merInfo{
//        @SerializedName("display")// 0不显示 1显示
//        public String display;
//    }

//    //幸运购
//    public class ListBean {
//        @SerializedName("activity_id") // 活动id
//        public String activityId;
//        @SerializedName("snatch_issue") // 活动期号
//        public String snatchIssue;
//        @SerializedName("snatch_name") // 活动名称
//        public String snatchName;
//        @SerializedName("snatch_subhead") // 活动副标题
//        public String snatchSubhead;
//        @SerializedName("snatch_zone") // 活动分区
//        public String snatchZone;
//        @SerializedName("snatch_goods_url") // 商品图片地址
//        public String snatchGoodsUrl;
//        @SerializedName("snatch_total_count") // 共需人次
//        public String snatchTotalCount;
//        @SerializedName("snatch_play_count") // 以参与人次
//        public String snatchPlayCount;
//        @SerializedName("snatch_once_expend") // 每次活动消耗金额
//        public String snatchOnceExpend;
//        @SerializedName("snatch_announce_time") // 活动揭晓时间
//        public String snatchAnnounceTime;
//        @SerializedName("snatch_filiale") // 活动发布地区  1 代表总部  其他表示兑换中心
//        public String snatchFiliale;
//    }
}
