package com.yilian.mall.entity;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/31.
 */
public class MallCategoryListEntity extends BaseEntity {
    @SerializedName("list")
    public ArrayList<MallCategory> list;

    public class MallCategory{

        @SerializedName("sort_id")//分类id
        public String sort_id;
        @SerializedName("sort_name")//分类名字
        public String sort_name;
        @SerializedName("sort_region")//所属地区
        public String sort_region;
        @SerializedName("sort_icon")//分类图片
        public String sort_icon;
        @SerializedName("sort_type")//商品类型 3表示乐分去，4表示乐享区
        public String sort_type;
        @SerializedName("sort_recommend")//推荐指数
        public String sort_recommend;
        @SerializedName("sort_status")//分类状态1正常0禁用
        public String sort_status;
    }
}
