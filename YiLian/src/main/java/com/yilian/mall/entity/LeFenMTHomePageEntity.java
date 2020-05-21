package com.yilian.mall.entity;/**
 * Created by  on 2017/3/21 0021.
 */

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.entity.ScoreExponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2017/3/21 0021.
 */
public class LeFenMTHomePageEntity extends BaseEntity {

    /**
     * data : {"activity_banner":[{"name":"","type":"1","image":"mallAdmin/images/a0894467ae7de4cc4e5ce05b47d2f5230fa964e5_wallpaper-2269548.jpg","content":"https://item.taobao.com/item.htm?spm=a230r.1.14.79.zYNxcV&id=522989416668&ns=1&abbucket=6#detail","display":1}],"icon_class":[{"name":"扫一扫","type":"1","image":"mallAdmin/images/f6bb27a0ac29f1639a6791dfe6f1a2be8ec66e17_1207046.png","content":""},{"name":"扫一扫","type":"1","image":"mallAdmin/images/b8b2c73c5f8fb26b70491425222b518897584cc3_1207047.png","content":""},{"name":"扫一扫","type":"1","image":"mallAdmin/images/dafc8c70ed79c06e96a39b1bb7493c352b0b6e37_1207042.png","content":""},{"name":"扫一扫","type":"1","image":"mallAdmin/images/7486a8d7735a832a1beb80e6157845bf212a78cb_wallpaper-2269548.jpg","content":""},{"name":"扫一扫","type":"1","image":"mallAdmin/images/4185663858d5c9becce9501355a91296878de9b3_wallpaper-2269548.jpg","content":""}],"news":[{"name":"","type":"1","image":"mallAdmin/images/2f9b1ef24ddc81b030f461e18fd932280c80f330_wallpaper-2269548.jpg","content":"1204"}],"activity":[{"content":"","type":"1","image":"/app/icon/20170318/meiriqiandao.png","name":"每日签到"},{"content":"","type":"2","image":"/app/icon/20170318/bendihuodong.png","name":"本地活动"},{"content":"","type":"3","image":"app/icon/20170318/bendishangcheng.png","name":"本地商城"},{"content":"lefenmall.com","type":"4","image":"/app/icon/20170318/shenbianhaohuo.png","name":"身边好货"}]}
     */

    @SerializedName("data")
    public DataBean data;

    /**
     * dataArr : {"dealCharts":[{"allAmount":"1","consumer":"6179332486376008","name":"****","value":"1","phone":"18237115864"},{"allAmount":"16800","consumer":"6178629170191004","name":"****","value":"16800"},{"allAmount":"16800","consumer":"6179300009758119","name":"****","value":"16800"},{"allAmount":"22800","consumer":"6180067264787410","name":"182****5864","phone":"18237115864","value":"22800"},{"allAmount":"22900","consumer":"6178424477040912","name":"****","phone":"","value":"22900"}],"areaDiscountCharts":[],"merDiscountCharts":[{"allbonus":"4246","name":"182****0536","phone":"18203660536","user_id":"6186700787325300","value":"4246"},{"allbonus":"2082","name":"152****4340","phone":"15238664340","user_id":"6179767615642021","value":"2082"},{"allbonus":"200","name":"****","phone":"","user_id":"6178424477040912","value":"200"},{"allbonus":"200","name":"****","phone":"","user_id":"6178424477040913","value":"200"}],"totalAmount":{"integralNumber":"0.0523684","totalBonus":"0"},"userIntegralCharts":[{"integral":"53277","name":"135****1721","phone":"13525661721","value":"53277"},{"integral":"5420","name":"182****0536","phone":"18203660536","value":"5420"},{"integral":"5235","name":"152****4340","phone":"15238664340","value":"5235"},{"integral":"122","name":"185****1620","phone":"18500201620","value":"122"},{"integral":"12","name":"182****1819","phone":"18237111819","value":"12"}]}
     */


    public static class DataBean {

        /**
         * 让利数据
         */
        @SerializedName("dataArr")
        public DataArrBean dataArrBean;

        public static class DataArrBean {
            /**
             * dealCharts : [{"allAmount":"1","consumer":"6179332486376008","name":"****","value":"1"},{"allAmount":"16800","consumer":"6178629170191004","name":"****","value":"16800"},{"allAmount":"16800","consumer":"6179300009758119","name":"****","value":"16800"},{"allAmount":"22800","consumer":"6180067264787410","name":"182****5864","phone":"18237115864","value":"22800"},{"allAmount":"22900","consumer":"6178424477040912","name":"****","phone":"","value":"22900"}]
             * areaDiscountCharts : []
             * merDiscountCharts : [{"allbonus":"4246","name":"182****0536","phone":"18203660536","user_id":"6186700787325300","value":"4246"},{"allbonus":"2082","name":"152****4340","phone":"15238664340","user_id":"6179767615642021","value":"2082"},{"allbonus":"200","name":"****","phone":"","user_id":"6178424477040912","value":"200"},{"allbonus":"200","name":"****","phone":"","user_id":"6178424477040913","value":"200"}]
             * totalAmount : {"integralNumber":"0.0523684","totalBonus":"0"}
             * userIntegralCharts : [{"integral":"53277","name":"135****1721","phone":"13525661721","value":"53277"},{"integral":"5420","name":"182****0536","phone":"18203660536","value":"5420"},{"integral":"5235","name":"152****4340","phone":"15238664340","value":"5235"},{"integral":"122","name":"185****1620","phone":"18500201620","value":"122"},{"integral":"12","name":"182****1819","phone":"18237111819","value":"12"}]
             */

            @SerializedName("totalAmount")
            public TotalAmountBean totalAmount;//奖券指数
            @SerializedName("dealCharts")
            public ArrayList<DealChartsBean> dealCharts;//消费排行
            @SerializedName("discountCharts")
            public ArrayList<AreaDiscount> areaDiscountCharts;//地区让利排行
            @SerializedName("merDiscountCharts")
            public ArrayList<MerDiscountChartsBean> merDiscountCharts;//商户让利排行
            @SerializedName("userIntegralCharts")
            public ArrayList<UserIntegralChartsBean> userIntegralCharts;//用户奖券排行

            public static class TotalAmountBean {
                /**
                 * integralNumber : 0.0523684
                 * totalBonus : 0
                 */

                @SerializedName("integralNumber")
                public String integralNumber;
                @SerializedName("totalBonus")
                public String totalBonus;
                @SerializedName("merchantStr")
                public String merchantCount;
                @SerializedName("userStr")
                public String memberCount;
                @SerializedName("integralNumberArr")
                public ArrayList<ScoreExponent> scoreExponent;
            }

            public static class DealChartsBean {
                /**
                 * allAmount : 1
                 * consumer : 6179332486376008
                 * name : ****
                 * value : 1
                 * phone : 18237115864
                 */

                @SerializedName("allAmount")
                public String allAmount;
                @SerializedName("consumer")
                public String consumer;
                @SerializedName("name")
                public String name;
                @SerializedName("value")
                public String value;
                @SerializedName("phone")
                public String phone;
            }

            public static class AreaDiscount {
                /**
                 * allAmount : 1
                 * consumer : 6179332486376008
                 * name : 白云区
                 * value : 1
                 * phone : 18237115864
                 */

                @SerializedName("allAmount")
                public String allAmount;
                @SerializedName("consumer")
                public String consumer;
                @SerializedName("name")
                public String name;
                @SerializedName("value")
                public String value;
                @SerializedName("phone")
                public String phone;
            }

            public static class MerDiscountChartsBean {
                /**
                 * allbonus : 4246
                 * name : 182****0536
                 * phone : 18203660536
                 * user_id : 6186700787325300
                 * value : 4246
                 */

                @SerializedName("allbonus")
                public String allbonus;
                @SerializedName("name")
                public String name;
                @SerializedName("phone")
                public String phone;
                @SerializedName("user_id")
                public String userId;
                @SerializedName("value")
                public String value;
            }

            public static class UserIntegralChartsBean {
                /**
                 * integral : 53277
                 * name : 135****1721
                 * phone : 13525661721
                 * value : 53277
                 */

                @SerializedName("integral")
                public String integral;
                @SerializedName("name")
                public String name;
                @SerializedName("phone")
                public String phone;
                @SerializedName("value")
                public String value;
            }
        }


        /**
         * "activity_banner":[ {//主题图片   首页广告
         * "type": 1,// 0 url 1 商品id 2 一元购原始活动id 3联盟商家id 4主题商品搜索（key_word:六一儿童节）   5，二级分类
         * "content": "",//内容
         * "image":"",//图片地址
         * "name":"",//主题名称
         * "display":""//0不显示 1显示
         * },{}],
         */
        @SerializedName("activity_banner")
        public List<ActivityBannerBean> activityBanner;
        /**
         * "icon_class": [{//分类图标
         * "type": 1,//  1 扫一扫 2 每日推荐 3 幸运购 4 每日签到 5 送券商家 （6 App内URL，7有奖邀请，8跳转旗舰店列表9充值中心，10本地活动列表，11常见问题，12浏览器打开URL 13意见反馈 14乐分区）
         * "content": "",//内容
         * "image":"",//图片地址
         * "name":"",//分类名称
         * },{}],
         */
        @SerializedName("icon_class")
        public List<ActivityBannerBean> iconClass;
        /**
         * "news": [{//乐分快报
         * "type": 1,//  1 扫一扫 2 每日推荐 3 幸运购 4 每日签到 5 送券商家 6 App内URL，7有奖邀请，8跳转旗舰店列表9充值中心，10本地活动列表，11常见问题，12浏览器打开URL 13意见反馈
         * "content": "",//内容
         * "image":"",//图片地址
         * "name": "",//新闻标题
         * },{}],
         */
        @SerializedName("news")
        public ArrayList<NoticeViewEntity> news;
        /**
         * "activity": [{//特色频道
         * "content":"",//type=3时，该字段返回商城数量,兑换中心id   // type=4,该字段返回filiale_id
         * "type":1,// 1 每日签到  2本地活动 3 本地商城   4身边好货
         * "image":"",//图片地址
         * "name": "",//每日签到
         * },{}]
         */
        @SerializedName("activity")
        public List<ActivityBannerBean> activity;

        public static class ActivityBannerBean {
            /**
             * name :
             * type : 1
             * image : mallAdmin/images/a0894467ae7de4cc4e5ce05b47d2f5230fa964e5_wallpaper-2269548.jpg
             * content : https://item.taobao.com/item.htm?spm=a230r.1.14.79.zYNxcV&id=522989416668&ns=1&abbucket=6#detail
             * display : 1
             */

            @SerializedName("name")
            public String name;
            @SerializedName("type")
            public int type;
            @SerializedName("image")
            public String image;
            @SerializedName("content")
            public String content;
            @SerializedName("display")
            public String display;
        }
    }

}
