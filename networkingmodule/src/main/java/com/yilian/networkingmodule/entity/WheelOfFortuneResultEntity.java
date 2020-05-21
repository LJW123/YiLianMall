package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by  on 2017/4/27 0027.
 * 大转盘抽奖结果
 */

public class WheelOfFortuneResultEntity extends BaseEntity {

    /**
     * data : {"prize_id":3,"name":"","type":"","image":"","goods":[]}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean implements Serializable{
        /**
         * prize_id : 3
         * name :
         * type ://1虚拟商品 2真实商品
         * image :
         * goods : []
         */

        @SerializedName("prize_id")
        public int prizeId;
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public String type;
        @SerializedName("image")
        public String image;
        @SerializedName("goods")
        public ArrayList<WheelPrizeEntity> goods;
    }
}
