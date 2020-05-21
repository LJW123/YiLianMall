package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author XiuRenLi  PRAY NO BUG
 * Created by Administrator on 2016/8/9.
 */
public class OneBuyCategoryListEntity extends BaseEntity {

    /**
     * class_id : 类别ID
     * class_name : 类别名称
     * icon : 图片URL
     */

    @SerializedName("class")
    public List<DataBean> data;

    public static class DataBean {
        @SerializedName("class_id")
        public String classId;
        @SerializedName("class_name")
        public String className;
        @SerializedName("icon")
        public String icon;
    }
}
