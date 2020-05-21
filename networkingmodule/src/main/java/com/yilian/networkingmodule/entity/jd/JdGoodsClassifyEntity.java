package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东商品分类
 *
 * @author Created by zhaiyaohua on 2018/5/23.
 */

public class JdGoodsClassifyEntity extends HttpResultBean {


    @SerializedName("data")
    public List<Data> data;

    public List<Data> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        /**
         * catId : 15901
         * title : 手机
         * content : [{"catId":"15908","img":"admin/jd/jtqj/zp/0.png","title":"抽纸"}]
         */

        @SerializedName("catId")
        public String catId;
        @SerializedName("title")
        public String title;
        @SerializedName("content")
        public List<Content> content;

        public String getCatId() {
            return catId == null ? "" : catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public String getTitle() {
            return title == null ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Content> getContent() {
            if (content == null) {
                return new ArrayList<>();
            }
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }

        public static class Content {
            /**
             * catId : 15908
             * img : admin/jd/jtqj/zp/0.png
             * title : 抽纸
             */

            @SerializedName("catId")
            public String catId;
            @SerializedName("img")
            public String img;
            @SerializedName("title")
            public String title;
        }
    }
}
