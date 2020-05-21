package com.yilian.networkingmodule.entity.jd;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东购物车实体类
 *
 * @author Created by zhaiyaohua on 2018/6/27.
 */

public class JdShoppingCarEntity extends HttpResultBean  {


    @SerializedName("list")
    public List<ListBean> list;


    public List<ListBean> getList() {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }



    public static class ListBean  {

        /**
         * cart_index : 1904
         * goods_sku : 2291963
         * goods_count : 1
         * consumer_id : 7612281891453117
         * device_index : 0
         * goods_info : {"id":"3647","sku":"2291963","name":"天堂伞 全自动商务雨伞男士全钢晴雨伞遮阳防晒便捷折叠自动伞 JLTX30周年纪念款","weight":"0.57","imagePath":"jfs/t2206/78/1232683017/130276/bec348d9/56835103Nf559cc55.jpg","state":"1","brandName":"天堂","productArea":"浙江杭州","upc":"2291963","saleUnit":"把","one_category":"1620","two_category":"1624","three_category":"1656","price":"70.03","jdPrice":"129.00","marketPrice":"598.00","return_bean":"38.38","sale":"1031","rate":"29.75","all_return_bean":"58.97","is_set_price":"0"}
         */

        @SerializedName("cart_index")
        public String cartIndex;
        @SerializedName("goods_sku")
        public String goodsSku;
        @SerializedName("goods_count")
        public String goodsCount;
        @SerializedName("consumer_id")
        public String consumerId;
        @SerializedName("device_index")
        public String deviceIndex;
        @SerializedName("goods_info")
        public GoodsInfo goodsInfo;
        /**
         * 本地添加字段
         */
        public boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public ListBean(Parcel in) {
            cartIndex = in.readString();
            goodsSku = in.readString();
            goodsCount = in.readString();
            consumerId = in.readString();
            deviceIndex = in.readString();
            goodsInfo = in.readParcelable(GoodsInfo.class.getClassLoader());
        }

        public static class GoodsInfo implements Parcelable {
            public static final Creator<GoodsInfo> CREATOR = new Creator<GoodsInfo>() {
                @Override
                public GoodsInfo createFromParcel(Parcel in) {
                    return new GoodsInfo(in);
                }

                @Override
                public GoodsInfo[] newArray(int size) {
                    return new GoodsInfo[size];
                }
            };
            /**
             * id : 3647
             * sku : 2291963
             * name : 天堂伞 全自动商务雨伞男士全钢晴雨伞遮阳防晒便捷折叠自动伞 JLTX30周年纪念款
             * weight : 0.57
             * imagePath : jfs/t2206/78/1232683017/130276/bec348d9/56835103Nf559cc55.jpg
             * state : 1
             * brandName : 天堂
             * productArea : 浙江杭州
             * upc : 2291963
             * saleUnit : 把
             * one_category : 1620
             * two_category : 1624
             * three_category : 1656
             * price : 70.03
             * jdPrice : 129.00
             * marketPrice : 598.00
             * return_bean : 38.38
             * sale : 1031
             * rate : 29.75
             * all_return_bean : 58.97
             * is_set_price : 0
             */

            @SerializedName("id")
            public String id;
            @SerializedName("sku")
            public String sku;
            @SerializedName("name")
            public String name;
            @SerializedName("weight")
            public String weight;
            @SerializedName("imagePath")
            public String imagePath;
            @SerializedName("state")
            public String state;
            @SerializedName("brandName")
            public String brandName;
            @SerializedName("productArea")
            public String productArea;
            @SerializedName("upc")
            public String upc;
            @SerializedName("saleUnit")
            public String saleUnit;
            @SerializedName("one_category")
            public String oneCategory;
            @SerializedName("two_category")
            public String twoCategory;
            @SerializedName("three_category")
            public String threeCategory;
            @SerializedName("price")
            public String price;
            @SerializedName("jdPrice")
            public String jdPrice;
            @SerializedName("marketPrice")
            public String marketPrice;
            @SerializedName("return_bean")
            public String returnBean;
            @SerializedName("sale")
            public String sale;
            @SerializedName("rate")
            public String rate;
            @SerializedName("all_return_bean")
            public String allReturnBean;
            @SerializedName("is_set_price")
            public String isSetPrice;

            protected GoodsInfo(Parcel in) {
                id = in.readString();
                sku = in.readString();
                name = in.readString();
                weight = in.readString();
                imagePath = in.readString();
                state = in.readString();
                brandName = in.readString();
                productArea = in.readString();
                upc = in.readString();
                saleUnit = in.readString();
                oneCategory = in.readString();
                twoCategory = in.readString();
                threeCategory = in.readString();
                price = in.readString();
                jdPrice = in.readString();
                marketPrice = in.readString();
                returnBean = in.readString();
                sale = in.readString();
                rate = in.readString();
                allReturnBean = in.readString();
                isSetPrice = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(id);
                dest.writeString(sku);
                dest.writeString(name);
                dest.writeString(weight);
                dest.writeString(imagePath);
                dest.writeString(state);
                dest.writeString(brandName);
                dest.writeString(productArea);
                dest.writeString(upc);
                dest.writeString(saleUnit);
                dest.writeString(oneCategory);
                dest.writeString(twoCategory);
                dest.writeString(threeCategory);
                dest.writeString(price);
                dest.writeString(jdPrice);
                dest.writeString(marketPrice);
                dest.writeString(returnBean);
                dest.writeString(sale);
                dest.writeString(rate);
                dest.writeString(allReturnBean);
                dest.writeString(isSetPrice);
            }
        }




    }



}
