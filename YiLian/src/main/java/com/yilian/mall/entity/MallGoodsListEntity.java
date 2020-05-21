package com.yilian.mall.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/21.
 */
public class MallGoodsListEntity extends BaseEntity {

    public ArrayList<MallGoodsLists> list;
    public class MallGoodsLists{
        public String goods_id;//商品ID
        public String goods_name;//商品名字
        public String goods_price;//商品价格
        public String goods_grade;//商品评分0-50,代表0-5颗星
        public String goods_icon;//商品缩略图
        public String goods_sale;//商品销量
        public String goods_renqi;//商品浏览次数
        public String goods_discount; //10或20 代表服务费百分比
        public int goods_type;//3乐购区（送区），4买区
        public String return_integral;//消费返利
    }
}
