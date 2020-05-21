package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.mylibrary.entity.LocationEntity;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/8 0008.
 * 商家推荐
 */
public class MTMerchantCommendEntity extends BaseEntity {

    /**
     {
     "merchant_id":"",//商家唯一id
     "merchant_name":"",//商家名字标题
     "evaluate":"",//商家评分10-50之间，代表1-5颗星
     "merchant_address":"",//商家地址
     "merchant_longitude":"",//商家地理位置经度
     "merchant_latitude":"",//商家地理位置纬度
     "merchant_industry":"",//商家所属的二级行业
     "merchant_industry_parent":"",//商家所属的顶级行业
     "merchant_tel":"",//联系电话
     "merchant_image":""//联盟商家图片
     "industry_name":""//商家行业分类
     "praise_count":"",//点赞数量
     }
     */

    @SerializedName("merchant_list")
    public List<MerchantListBean> merchantList;

    public  class MerchantListBean  extends LocationEntity {

        @SerializedName("merchant_sort_time")
        public long merchantSortTime;
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("evaluate")
        public String evaluate;
        @SerializedName("merchant_address")
        public String merchantAddress;

        @SerializedName("merchant_industry")
        public String merchantIndustry;
        @SerializedName("merchant_industry_parent")
        public String merchantIndustryParent;
        @SerializedName("merchant_tel")
        public String merchantTel;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("industry_name")
        public String industryName;
        @SerializedName("praise_count")
        public String praiseCount;
        @SerializedName("mer_discount")
        public String merDiscount;//商家让利百分比

        /**
         * 浏览量
         */
        @SerializedName("renqi")
        public String renqi;

        /**
         * 是否支持配送
         */
        @SerializedName("is_delivery")
        public String isDelivery;
        /**
         * 是否支持预定
         */
        @SerializedName("is_reserve")
        public String isReserve;
    }
}
