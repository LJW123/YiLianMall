package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2016/12/7 0007.
 * 美团-退款详情实体类
 */

public class MTRefundDetailEntity extends BaseEntity {


    /**
     * name : 中式大盘鸡
     * codes : [{"code":"1111111111","status":"2"},{"code":"1111111112","status":"2"},{"code":"1111111113","status":"2"}]
     * price : 11300
     * ref_style : [{"style_id":1,"style_msg":"乐享币奖励"}]
     * reason : [{"reason_id":1,"reason_msg":"预约不上"},{"reason_id":2,"reason_msg":"商家营业但不接待"},{"reason_id":3,"reason_msg":"商家停业/装修/转让"},{"reason_id":4,"reason_msg":"去过了,不太满意"},{"reason_id":5,"reason_msg":"朋友/网上评价不好"},{"reason_id":6,"reason_msg":"买多了/买错了"},{"reason_id":7,"reason_msg":"计划有变,没时间消费"},{"reason_id":8,"reason_msg":"后悔了,不想要了"},{"reason_id":9,"reason_msg":"商家说可以直接以套餐价到店消费"},{"reason_id":10,"reason_msg":"联系不上商家"},{"reason_id":11,"reason_msg":"还有不爽"}]
     */

    @SerializedName("name")
    public String name;
    @SerializedName("price")
    public String price;
    @SerializedName("voucher")
    public float voucher;
    @SerializedName("return_integral")
    public String returnIntegral;
    /**
     * code : 1111111111
     * status : 2
     */

    @SerializedName("codes")
    public ArrayList<MTCodesEntity> codes;
    /**
     * style_id : 1
     * style_msg : 乐享币奖励
     */

    @SerializedName("ref_style")
    public ArrayList<RefStyleBean> refStyle;
    /**
     * reason_id : 1
     * reason_msg : 预约不上
     */

    @SerializedName("reason")
    public ArrayList<ReasonBean> reason;

    public class RefStyleBean {
        @SerializedName("style_id")
        public int styleId;
        @SerializedName("style_msg")
        public String styleMsg;
    }

    public class ReasonBean {
        @SerializedName("reason_id")
        public String reasonId;
        @SerializedName("reason_msg")
        public String reasonMsg;
    }
}
