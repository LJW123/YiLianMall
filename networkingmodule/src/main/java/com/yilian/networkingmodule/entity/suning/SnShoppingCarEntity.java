package com.yilian.networkingmodule.entity.suning;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 苏宁购物车实体类
 *
 * @author Created by Zg on 2018/7/20.
 */

public class SnShoppingCarEntity extends HttpResultBean {


    @SerializedName("list")
    public List<ListBean> list;


    public List<ListBean> getList() {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }


    public static class ListBean {

        /**
         * code: 1,
         * msg: "success",
         * list: [{
         * cart_index: "1892",
         * goods_sku: "192748416",
         * goods_count: "3",
         * consumer_id: "0",
         * device_index: "0",
         * goods_info: {
         * skuId: "192748416",
         * name: "美菱冷藏冷冻箱BCD-221WP3B格调金",
         * image: "http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000192748416_1_400x400.jpg",
         * snPrice: "3299.00",
         * return_bean: "42.95",
         * sale: "0"
         * }}]
         */


        @SerializedName("cart_index")
        public String cartIndex;
        @SerializedName("skuId")
        public String skuId;
        @SerializedName("num")
        public String num;
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

        public ListBean(Parcel in) {
            cartIndex = in.readString();
            skuId = in.readString();
            num = in.readString();
            consumerId = in.readString();
            deviceIndex = in.readString();
            goodsInfo = in.readParcelable(GoodsInfo.class.getClassLoader());
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
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
             * skuId : 192748416
             * name : 美菱冷藏冷冻箱BCD-221WP3B格调金
             * image : http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000192748416_1_400x400.jpg
             * snPrice : 3299.00
             * return_bean : 42.95
             * sale : 0
             * `state` tinyint(1) unsigned NOT NULL COMMENT '上下架状态 1在售 0下架',
             */

            @SerializedName("skuId")
            private String skuId;
            @SerializedName("name")
            private String name;
            @SerializedName("image")
            private String image;
            @SerializedName("snPrice")
            private String snPrice;
            @SerializedName("return_bean")
            private String returnBean;
            @SerializedName("sale")
            private String sale;
            @SerializedName("state")
            private int state;


            protected GoodsInfo(Parcel in) {
                skuId = in.readString();
                name = in.readString();
                image = in.readString();
                snPrice = in.readString();
                returnBean = in.readString();
                sale = in.readString();
            }

            public String getSkuId() {
                return skuId == null ? "" : skuId;
            }

            public void setSkuId(String skuId) {
                this.skuId = skuId;
            }

            public String getName() {
                return name == null ? "" : name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image == null ? "" : image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getSnPrice() {
                return snPrice == null ? "" : snPrice;
            }

            public void setSnPrice(String snPrice) {
                this.snPrice = snPrice;
            }

            public String getReturnBean() {
                return returnBean == null ? "" : returnBean;
            }

            public void setReturnBean(String returnBean) {
                this.returnBean = returnBean;
            }

            public String getSale() {
                return sale == null ? "" : sale;
            }

            public void setSale(String sale) {
                this.sale = sale;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(skuId);
                parcel.writeString(name);
                parcel.writeString(image);
                parcel.writeString(snPrice);
                parcel.writeString(returnBean);
                parcel.writeString(sale);
            }
        }


    }


}
