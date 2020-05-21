package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/10/9 0009.
 */

public class AnswerListEntity extends BaseEntity {

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        @SerializedName("id")
        public String id;
        @SerializedName("goods_index")
        public String goods_index;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("question_info")
        public String question_info;
        @SerializedName("create_time")
        public String create_time;
        @SerializedName("employee_id")
        public String employee_id;
        @SerializedName("supplier_id")
        public String supplier_id;
        @SerializedName("reply")
        public String reply;
        @SerializedName("reply_time")
        public String reply_time;
        @SerializedName("is_del")
        public String is_del;
        @SerializedName("goods_name")
        public String goods_name;
        @SerializedName("photo")
        public String photo;
    }

}
