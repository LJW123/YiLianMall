package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/01/24.
 */

public class V3MoneyMenuEntity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {

        /**
         * type : 9
         * content : 40
         * image : admin/images/6bbd42bbf62dfd5d4dbd68d16ec61d74a36cdaab_1.jpg
         * name : 我的信用
         */

        @SerializedName("type")
        private String type;
        @SerializedName("content")
        private String content;
        @SerializedName("image")
        private String image;
        @SerializedName("name")
        private String name;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
