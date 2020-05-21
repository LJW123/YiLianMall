package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhaiyaohua} on 2017/12/30 0030.
 * @author zhaiyaohua
 */

public class OnlineMallActivityEntity extends HttpResultBean {


    /**
     * data : {"activitys":[{"sub_title":"精品推荐","sub_title_pic":"","top":{"count":"","list":{"title_pic":"app/image/sss.jpg","zone":"","data_type":"","tag_name":"","type":0,"content":"","content_pic":[]}},"bottom":{"count":"","list":{"title_pic":"app/image/sss.jpg","zone":"","data_type":"","tag_name":"","type":0,"content":"","content_pic":[]}},"recommend_banner ":[{"name":"口碑推荐","type":"0","image":"admin/images/df0d4435b2b2612d559010f02b4e341a8af308de_1.png","content":"http://www.baidu.com"}]}]}
     */

    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("activitys")
        public List<Activitys> activitys;

        public static class Activitys extends OnlineMallCategoryData {
            /**
             * sub_title : 精品推荐
             * sub_title_pic :
             * top : {"count":"","list":{"title_pic":"app/image/sss.jpg","zone":"","data_type":"","tag_name":"","type":0,"content":"","content_pic":[]}}
             * bottom : {"count":"","list":{"title_pic":"app/image/sss.jpg","zone":"","data_type":"","tag_name":"","type":0,"content":"","content_pic":[]}}
             * recommend_banner  : [{"name":"口碑推荐","type":"0","image":"admin/images/df0d4435b2b2612d559010f02b4e341a8af308de_1.png","content":"http://www.baidu.com"}]
             */

            @SerializedName("sub_title")
            public String subTitle;
            @SerializedName("sub_title_pic")
            public String subTitlePic;
            @SerializedName("top")
            public Top top;
            @SerializedName("bottom")
            public Bottom bottom;
            @SerializedName("recommend_banner ")
            public List<RecommendBanner> recommendBanner;
            @SerializedName("custom_recommend_banner ")
            public CustomRecommendBanner customRecommendBanner;

            @Override
            public int getItemType() {
                return OnlineMallCategoryData.ITEM_TYPE_WEIT_FOUR;
            }

            @Override
            public int getSpanSize() {
                return OnlineMallCategoryData.ITEM_SPAN_SIZE_FOUR;
            }

            public static class Top {
                /**
                 * count :
                 * list : {"title_pic":"app/image/sss.jpg","zone":"","data_type":"","tag_name":"","type":0,"content":"","content_pic":[]}
                 */

                @SerializedName("count")
                public String count;
                @SerializedName("list")
                public List<TopDetails> list;


            }

            public static class Bottom {
                /**
                 * count :
                 * list : {"title_pic":"app/image/sss.jpg","zone":"","data_type":"","tag_name":"","type":0,"content":"","content_pic":[]}
                 */

                @SerializedName("count")
                public String count;
                @SerializedName("list")
                public List<BottomDetails>  list;
            }
            public static class TopDetails extends OnlineMallCategoryData  {
                /**
                 * title_pic : app/image/sss.jpg
                 * zone :
                 * data_type :
                 * tag_name :
                 * type : 0
                 * content :
                 * content_pic : []
                 */

                @SerializedName("title_pic")
                public String titlePic;
                @SerializedName("zone")
                public String zone;
                @SerializedName("data_type")
                public String dataType;
                @SerializedName("tag_name")
                public String tagName;
                @SerializedName("type")
                public int type;
                @SerializedName("content")
                public String content;
                @SerializedName("content_pic")
                public java.util.List<String> contentPic;

                @Override
                public int getItemType() {
                    return OnlineMallCategoryData.ITEM_TYPE_WEIT_TWO;
                }

                @Override
                public int getSpanSize() {
                    return OnlineMallCategoryData.ITEM_SPAN_SIZE_TWO;
                }
            }

            /**
             *
             */
            public static class BottomDetails  extends OnlineMallCategoryData{
                /**
                 * title_pic : app/image/sss.jpg
                 * zone :
                 * data_type :
                 * tag_name :
                 * type : 0
                 * content :
                 * content_pic : []
                 */

                @SerializedName("title_pic")
                public String titlePic;
                @SerializedName("zone")
                public String zone;
                @SerializedName("data_type")
                public String dataType;
                @SerializedName("tag_name")
                public String tagName;
                @SerializedName("type")
                public int type;
                @SerializedName("content")
                public String content;
                @SerializedName("content_pic")
                public java.util.List<String> contentPic;

                @Override
                public int getItemType() {
                    return OnlineMallCategoryData.ITEM_TYPE_WEIT_ONE;
                }

                @Override
                public int getSpanSize() {
                    return OnlineMallCategoryData.ITEM_SPAN_SIZE_ONE;
                }
            }

            /**
             * 底部轮播图
             */

            public static class RecommendBanner  {
                /**
                 * name : 口碑推荐
                 * type : 0
                 * image : admin/images/df0d4435b2b2612d559010f02b4e341a8af308de_1.png
                 * content : http://www.baidu.com
                 */

                @SerializedName("name")
                public String name;
                @SerializedName("type")
                public String type;
                @SerializedName("image")
                public String image;
                @SerializedName("content")
                public String content;

            }
            public static class CustomRecommendBanner  extends OnlineMallCategoryData{

                public List<RecommendBanner> recommendBanner=new ArrayList<>();

                @Override
                public int getItemType() {
                    return OnlineMallCategoryData.ITEM_TYPE_WEIT_FOUR;
                }

                @Override
                public int getSpanSize() {
                    return OnlineMallCategoryData.ITEM_SPAN_SIZE_FOUR;
                }

            }
        }
    }
}
