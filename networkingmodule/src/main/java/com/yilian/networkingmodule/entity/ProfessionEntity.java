package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/1/15.
 */

public class ProfessionEntity extends HttpResultBean {
    public static final int TEXT_TYPE = 0;
    public static final int EDIT_TYPE = 1;
    /**
     * 其他行业ID
     */
    public static final int EDIT_ID = 13;
    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean implements MultiItemEntity {
        /**
         * id : 1
         * name : 计算机/互联网/通讯
         * tag_name : IT
         * tag_pic : app/images/profession/zy_it.png
         * sort : 0
         */

        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("tag_name")
        public String tagName;
        @SerializedName("tag_pic")
        public String tagPic;
        @SerializedName("sort")
        public String sort;

        @Override
        public int getItemType() {
            return id == EDIT_ID ? EDIT_TYPE : TEXT_TYPE;
        }
    }
}
