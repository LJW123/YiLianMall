package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/2/5.
 *         3级分类
 */

public class MerchantEnterCategory3Entity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;
    @SerializedName("classify_id")
    public String checkedClassifyId;

    public static class DataBean {
        public DataBean(String name, List<ListBeanX> list) {
            this.name = name;
            this.list = list;
        }

        /**
         * id : 248
         * name : 刘云志
         * level : 1
         * parent : 0
         * img : admin/images/1cbbd0c312d875e2a6cd7711554ef86c1081b7bd_1.jpg
         * list : [{"id":"249","name":"刘云志儿子","level":"2","parent":"248","img":"admin/images/aaf29a7ec5decac2df79df80de197f433ea5c419_1.jpg","list":[{"id":"249","name":"刘云志儿子","level":"2","parent":"248","img":"admin/images/aaf29a7ec5decac2df79df80de197f433ea5c419_1.jpg"}]}]
         */

        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("level")
        public String level;
        @SerializedName("parent")
        public String parent;
        @SerializedName("img")
        public String img;
        @SerializedName("list")
        public List<ListBeanX> list;

        public static class ListBeanX extends MerchantEnterSubCategory {
            public ListBeanX(String name, List<ListBean> list, String id) {
                this.name = name;
                this.list = list;
                this.id = id;
            }

            /**
             * id : 249
             * name : 刘云志儿子
             * level : 2
             * parent : 248
             * img : admin/images/aaf29a7ec5decac2df79df80de197f433ea5c419_1.jpg
             * list : [{"id":"249","name":"刘云志儿子","level":"2","parent":"248","img":"admin/images/aaf29a7ec5decac2df79df80de197f433ea5c419_1.jpg"}]
             */

            @SerializedName("id")
            public String id;
            @SerializedName("name")
            public String name;
            @SerializedName("level")
            public String level;
            @SerializedName("parent")
            public String parent;
            @SerializedName("img")
            public String img;
            @SerializedName("list")
            public List<ListBean> list;

            @Override
            public int getItemType() {
                return SUB_2_CATEGORY;
            }

            @Override
            public int getSpanSize() {
                return SUB_2_CATEGORY;
            }

            @SerializedName("is_check")
            public int isChecked;

            public static class ListBean extends MerchantEnterSubCategory {
                public ListBean(String name, String id) {
                    this.name = name;
                    this.id = id;
                }

                /**
                 * id : 249
                 * name : 刘云志儿子
                 * level : 2
                 * parent : 248
                 * img : admin/images/aaf29a7ec5decac2df79df80de197f433ea5c419_1.jpg
                 */

                @SerializedName("id")
                public String id;
                @SerializedName("name")
                public String name;
                @SerializedName("level")
                public String level;
                @SerializedName("parent")
                public String parent;
                @SerializedName("img")
                public String img;
                @SerializedName("is_check")
                public int isChecked;

                @Override
                public int getItemType() {
                    return SUB_3_CATEGORY;
                }

                @Override
                public int getSpanSize() {
                    return SUB_3_CATEGORY;
                }
            }
        }
    }
}
