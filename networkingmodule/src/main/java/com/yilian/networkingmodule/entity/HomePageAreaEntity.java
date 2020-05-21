package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.mylibrary.MyBannerData;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/5/2.
 */

public class HomePageAreaEntity extends HttpResultBean {
    public static final int SHOW=1;
    public static final int UN_SHOW=0;

    /**
     * data : {"index_1":{"title_icon":"/admin/images/jingxuanzhuanqu.png","title_content":[{"title":"下午茶点","sub_title":"莫负午后好时光","type":"5","content":"0,104431","img_1":"/admin/images/1-104431-1 .jpg","img_2":"/admin/images/1-104431-2.jpg"},{"title":"好吃不停","sub_title":"让你留口水的美食","type":"5","content":"0,104254","img_1":"/admin/images/2-104254-1.jpg","img_2":"/admin/images/2-104254-2.jpg"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":5,"content":"0,116351","img_1":"/admin/images/3-116351.jpg"},{"title":"女神风范","sub_title":"出行必备","type":5,"content":"0,117885","img_1":"/admin/images/4-117885.jpg"},{"title":"精品墨镜","sub_title":"永不过时","type":5,"content":"0,119509","img_1":"/admin/images/5-119509.jpg"},{"title":"韩版草帽","sub_title":"时尚防晒","type":5,"content":"0,119551","img_1":"/admin/images/6-119551.jpg"}],"index_type":"1","index_content":"33","more":"/admin/images/jingxuangengduo.png"},"index_2":{"title_icon":"/admin/images/pinpaizhuanqu.png","title_content":[{"title":"汇吃美食","sub_title":"唯美丽与美食不可辜负","type":1,"content":3,"img_1":"/admin/images/sanzhisongshu.png","logo":"/admin/images/sanzhisongshuLOGO.png","shop_name":"三只松鼠"},{"title":"女神养成","sub_title":"美靠衣装 更靠靓装","type":1,"content":3,"img_1":"/admin/images/zirantang.png","logo":"/admin/images/zirantangLOGO.png","shop_name":"自然堂"},{"title":"品质家电","sub_title":"实用小家电了解一下","type":1,"content":3,"img_1":"/admin/images/kangjia.png","logo":"/admin/images/kangjiaLOGO.png","shop_name":"康佳"},{"title":"酒类专场","sub_title":"葡萄美酒夜光杯","type":1,"content":3,"img_1":"/admin/images/wuliangye.png","logo":"/admin/images/wuliangyeLOGO.png","shop_name":"五粮液"}],"index_type":"1","index_content":"44","more":"/admin/images/pinpaigengduo.png"},"index_3":{"title_icon":"/admin/images/taobaozhuanqu.png","title_content":[{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"}],"index_type":"1","index_content":"45","more":"/admin/images/taobaogengduo.png"},"adv_1":{"title_icon":"","title_content":[{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"},{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"}],"index_type":"0","index_content":"0","more":" "},"adv_2":{"title_icon":"","title_content":[{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"},{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"}],"index_type":"0","index_content":"0","more":" "}}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * index_1 : {"title_icon":"/admin/images/jingxuanzhuanqu.png","title_content":[{"title":"下午茶点","sub_title":"莫负午后好时光","type":"5","content":"0,104431","img_1":"/admin/images/1-104431-1 .jpg","img_2":"/admin/images/1-104431-2.jpg"},{"title":"好吃不停","sub_title":"让你留口水的美食","type":"5","content":"0,104254","img_1":"/admin/images/2-104254-1.jpg","img_2":"/admin/images/2-104254-2.jpg"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":5,"content":"0,116351","img_1":"/admin/images/3-116351.jpg"},{"title":"女神风范","sub_title":"出行必备","type":5,"content":"0,117885","img_1":"/admin/images/4-117885.jpg"},{"title":"精品墨镜","sub_title":"永不过时","type":5,"content":"0,119509","img_1":"/admin/images/5-119509.jpg"},{"title":"韩版草帽","sub_title":"时尚防晒","type":5,"content":"0,119551","img_1":"/admin/images/6-119551.jpg"}],"index_type":"1","index_content":"33","more":"/admin/images/jingxuangengduo.png"}
         * index_2 : {"title_icon":"/admin/images/pinpaizhuanqu.png","title_content":[{"title":"汇吃美食","sub_title":"唯美丽与美食不可辜负","type":1,"content":3,"img_1":"/admin/images/sanzhisongshu.png","logo":"/admin/images/sanzhisongshuLOGO.png","shop_name":"三只松鼠"},{"title":"女神养成","sub_title":"美靠衣装 更靠靓装","type":1,"content":3,"img_1":"/admin/images/zirantang.png","logo":"/admin/images/zirantangLOGO.png","shop_name":"自然堂"},{"title":"品质家电","sub_title":"实用小家电了解一下","type":1,"content":3,"img_1":"/admin/images/kangjia.png","logo":"/admin/images/kangjiaLOGO.png","shop_name":"康佳"},{"title":"酒类专场","sub_title":"葡萄美酒夜光杯","type":1,"content":3,"img_1":"/admin/images/wuliangye.png","logo":"/admin/images/wuliangyeLOGO.png","shop_name":"五粮液"}],"index_type":"1","index_content":"44","more":"/admin/images/pinpaigengduo.png"}
         * index_3 : {"title_icon":"/admin/images/taobaozhuanqu.png","title_content":[{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"}],"index_type":"1","index_content":"45","more":"/admin/images/taobaogengduo.png"}
         * adv_1 : {"title_icon":"","title_content":[{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"},{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"}],"index_type":"0","index_content":"0","more":" "}
         * adv_2 : {"title_icon":"","title_content":[{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"},{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"}],"index_type":"0","index_content":"0","more":" "}
         */

        @SerializedName("index_1")
        public Index1Bean index1;
        @SerializedName("index_2")
        public Index2Bean index2;
        @SerializedName("index_3")
        public Index3Bean index3;
        @SerializedName("index_4")
        public Index4Bean index4;
        @SerializedName("index_5")
        public Index5Bean index5;
        @SerializedName("adv_1")
        public Adv1Bean adv1;
        @SerializedName("adv_2")
        public Adv2Bean adv2;

        public static class Index1Bean {
            /**
             * title_icon : /admin/images/jingxuanzhuanqu.png
             * title_content : [{"title":"下午茶点","sub_title":"莫负午后好时光","type":"5","content":"0,104431","img_1":"/admin/images/1-104431-1 .jpg","img_2":"/admin/images/1-104431-2.jpg"},{"title":"好吃不停","sub_title":"让你留口水的美食","type":"5","content":"0,104254","img_1":"/admin/images/2-104254-1.jpg","img_2":"/admin/images/2-104254-2.jpg"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":5,"content":"0,116351","img_1":"/admin/images/3-116351.jpg"},{"title":"女神风范","sub_title":"出行必备","type":5,"content":"0,117885","img_1":"/admin/images/4-117885.jpg"},{"title":"精品墨镜","sub_title":"永不过时","type":5,"content":"0,119509","img_1":"/admin/images/5-119509.jpg"},{"title":"韩版草帽","sub_title":"时尚防晒","type":5,"content":"0,119551","img_1":"/admin/images/6-119551.jpg"}]
             * index_type : 1
             * index_content : 33
             * more : /admin/images/jingxuangengduo.png
             */
            @SerializedName("is_show")
            public int isShow;
            @SerializedName("title_icon")
            public String titleIcon;
            @SerializedName("index_type")
            public int indexType;
            @SerializedName("index_content")
            public String indexContent;
            @SerializedName("more")
            public String moreUrl;
            @SerializedName("title_content")
            public List<TitleContentBean> titleContent;

            public static class TitleContentBean {
                /**
                 * title : 下午茶点
                 * sub_title : 莫负午后好时光
                 * type : 5
                 * content : 0,104431
                 * img_1 : /admin/images/1-104431-1 .jpg
                 * img_2 : /admin/images/1-104431-2.jpg
                 */

                @SerializedName("title")
                public String title;
                @SerializedName("sub_title")
                public String subTitle;
                @SerializedName("type")
                public int type;
                @SerializedName("content")
                public String content;
                @SerializedName("img_1")
                public String img1;
                @SerializedName("img_2")
                public String img2;
            }
        }

        public static class Index2Bean {
            /**
             * title_icon : /admin/images/pinpaizhuanqu.png
             * title_content : [{"title":"汇吃美食","sub_title":"唯美丽与美食不可辜负","type":1,"content":3,"img_1":"/admin/images/sanzhisongshu.png","logo":"/admin/images/sanzhisongshuLOGO.png","shop_name":"三只松鼠"},{"title":"女神养成","sub_title":"美靠衣装 更靠靓装","type":1,"content":3,"img_1":"/admin/images/zirantang.png","logo":"/admin/images/zirantangLOGO.png","shop_name":"自然堂"},{"title":"品质家电","sub_title":"实用小家电了解一下","type":1,"content":3,"img_1":"/admin/images/kangjia.png","logo":"/admin/images/kangjiaLOGO.png","shop_name":"康佳"},{"title":"酒类专场","sub_title":"葡萄美酒夜光杯","type":1,"content":3,"img_1":"/admin/images/wuliangye.png","logo":"/admin/images/wuliangyeLOGO.png","shop_name":"五粮液"}]
             * index_type : 1
             * index_content : 44
             * more : /admin/images/pinpaigengduo.png
             */
            @SerializedName("is_show")
            public int isShow;
            @SerializedName("title_icon")
            public String titleIcon;
            @SerializedName("index_type")
            public int indexType;
            @SerializedName("index_content")
            public String indexContent;
            @SerializedName("more")
            public String moreUrl;
            @SerializedName("title_content")
            public List<TitleContentBeanX> titleContent;

            public static class TitleContentBeanX {
                /**
                 * title : 汇吃美食
                 * sub_title : 唯美丽与美食不可辜负
                 * type : 1
                 * content : 3
                 * img_1 : /admin/images/sanzhisongshu.png
                 * logo : /admin/images/sanzhisongshuLOGO.png
                 * shop_name : 三只松鼠
                 */

                @SerializedName("title")
                public String title;
                @SerializedName("sub_title")
                public String subTitle;
                @SerializedName("type")
                public int type;
                @SerializedName("content")
                public String content;
                @SerializedName("img_1")
                public String img1;
                @SerializedName("logo")
                public String logo;
                @SerializedName("shop_name")
                public String shopName;
            }
        }

        public static class Index3Bean {
            /**
             * is_show 0不显示   1显示
             * title_icon : /admin/images/taobaozhuanqu.png
             * title_content : [{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"}]
             * index_type : 1
             * index_content : 45
             * more : /admin/images/taobaogengduo.png
             */
            @SerializedName("is_show")
            public int isShow;
            @SerializedName("title_icon")
            public String titleIcon;
            @SerializedName("index_type")
            public int indexType;
            @SerializedName("index_content")
            public String indexContent;
            @SerializedName("more")
            public String moreUrl;
            @SerializedName("title_content")
            public List<TitleContentBeanXX> titleContent;

            public static class TitleContentBeanXX {
                /**
                 * title : 休闲T恤
                 * sub_title : 潮流新风尚
                 * type : 1
                 * content : 3
                 * img_1 : url_1
                 */

                @SerializedName("title")
                public String title;
                @SerializedName("sub_title")
                public String subTitle;
                @SerializedName("type")
                public int type;
                @SerializedName("content")
                public String content;
                @SerializedName("img_1")
                public String img1;
            }
        }

        public static class Index4Bean {
            /**
             * is_show 0不显示   1显示
             * title_icon : /admin/images/taobaozhuanqu.png
             * title_content : [{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"}]
             * index_type : 1
             * index_content : 45
             * more : /admin/images/taobaogengduo.png
             */
            @SerializedName("is_show")
            public int isShow;
            @SerializedName("title_icon")
            public String titleIcon;
            @SerializedName("index_type")
            public int indexType;
            @SerializedName("index_content")
            public String indexContent;
            @SerializedName("more")
            public String moreUrl;
            @SerializedName("title_content")
            public List<TitleContentBeanXXX> titleContent;

            public static class TitleContentBeanXXX {
                /**
                 * title : 休闲T恤
                 * sub_title : 潮流新风尚
                 * type : 1
                 * content : 3
                 * img_1 : url_1
                 */

                @SerializedName("title")
                public String title;
                @SerializedName("sub_title")
                public String subTitle;
                @SerializedName("type")
                public int type;
                @SerializedName("content")
                public String content;
                @SerializedName("img_1")
                public String img1;
            }
        }
        public static class Index5Bean {
            /**
             * is_show 0不显示   1显示
             * title_icon : /admin/images/taobaozhuanqu.png
             * title_content : [{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"},{"title":"休闲T恤","sub_title":"潮流新风尚","type":1,"content":3,"img_1":"url_1"}]
             * index_type : 1
             * index_content : 45
             * more : /admin/images/taobaogengduo.png
             */
            @SerializedName("is_show")
            public int isShow;
            @SerializedName("title_icon")
            public String titleIcon;
            @SerializedName("index_type")
            public int indexType;
            @SerializedName("index_content")
            public String indexContent;
            @SerializedName("more")
            public String moreUrl;
            @SerializedName("title_content")
            public List<TitleContentBeanXXX> titleContent;

            public static class TitleContentBeanXXX {
                /**
                 * title : 休闲T恤
                 * sub_title : 潮流新风尚
                 * type : 1
                 * content : 3
                 * img_1 : url_1
                 */

                @SerializedName("title")
                public String title;
                @SerializedName("sub_title")
                public String subTitle;
                @SerializedName("type")
                public int type;
                @SerializedName("content")
                public String content;
                @SerializedName("img_1")
                public String img1;
            }
        }

        public static class Adv1Bean {
            /**
             * title_icon :
             * title_content : [{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"},{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"}]
             * index_type : 0
             * index_content : 0
             * more :
             */
            @SerializedName("is_show")
            public int isShow;
            @SerializedName("title_icon")
            public String titleIcon;
            @SerializedName("index_type")
            public String indexType;
            @SerializedName("index_content")
            public String indexContent;
            @SerializedName("more")
            public String moreUrl;
            @SerializedName("title_content")
            public List<TitleContentBeanXXX> titleContent;

            public static class TitleContentBeanXXX implements MyBannerData {
                /**
                 * type : 1
                 * content : 3
                 * img_1 : /admin/images/shouyeguanggao1.png
                 */

                @SerializedName("type")
                public int type;
                @SerializedName("content")
                public String content;
                @SerializedName("img_1")
                public String img1;

                @Override
                public String getImgUrl() {
                    return img1;
                }

                @Override
                public int getType() {
                    return type;
                }

                @Override
                public String getContent() {
                    return content;
                }
            }
        }

        public static class Adv2Bean {
            /**
             * title_icon :
             * title_content : [{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"},{"type":1,"content":3,"img_1":"/admin/images/shouyeguanggao1.png"}]
             * index_type : 0
             * index_content : 0
             * more :
             */
            @SerializedName("is_show")
            public int isShow;
            @SerializedName("title_icon")
            public String titleIcon;
            @SerializedName("index_type")
            public String indexType;
            @SerializedName("index_content")
            public String indexContent;
            @SerializedName("more")
            public String moreUrl;
            @SerializedName("title_content")
            public List<TitleContentBeanXXXX> titleContent;

            public static class TitleContentBeanXXXX {
                /**
                 * type : 1
                 * content : 3
                 * img_1 : /admin/images/shouyeguanggao1.png
                 */

                @SerializedName("type")
                public int type;
                @SerializedName("content")
                public int content;
                @SerializedName("img_1")
                public String img1;
            }
        }
    }
}
