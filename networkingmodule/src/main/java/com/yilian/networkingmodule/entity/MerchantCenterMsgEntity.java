package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2018/2/5 0005.
 */

public class MerchantCenterMsgEntity extends HttpResultBean {


    /**
     * supplier_info : {"supplier_name":"漫威宇宙","supplier_icon":"http://img.lefenmall.net/goods/goods_info/82fe6c184d0a506f01c38a730486553651b260cc_icon.png","supplier_rank":"皇冠店铺","supplier_phone":"17638513353"}
     * supplier_order : [{"title":"待接单","img":"/app/images/icon/20180205/icon_daijiedan.png","type":34,"content":1,"count":"0","content_type":0},{"title":"待发货","img":"/app/images/icon/20180205/icon_daifahuo.png","type":34,"content":2,"count":0,"content_type":0},{"title":"待收货","img":"/app/images/icon/20180205/icon_daishouhuo.png","type":34,"content":3,"count":"0","content_type":0},{"title":"售后","img":"/app/images/icon/20180205/icon_shouhou.png","type":34,"content":4,"count":0,"content_type":0},{"title":"订单管理","img":"/app/images/icon/20180205/icon_dingdanguanli.png","type":34,"content":0,"count":0,"content_type":0}]
     * beLooked : [{"title":"我要开店","img":"/app/images/icon/20180205/icon_kaidian.png","type":9,"content":48},{"title":"信用通","img":"/app/images/icon/20180205/icon_xinyongtong.png","type":13,"content":49},{"title":"我的钱柜","img":"/app/images/icon/20180205/icon_qiangui.png","type":9,"content":50},{"title":"云图","img":"/app/images/icon/20180205/icon_yuntu.png","type":9,"content":51},{"title":"店铺管理","img":"/app/images/icon/20180205/icon_dianpuguanli.png","type":9,"content":52},{"title":"商家数据","img":"/app/images/icon/20180205/icon_shuju.png","type":9,"content":53},{"title":"收款码","img":"/app/images/icon/20180205/icon_shoukuanma.png","type":9,"content":55},{"title":"年费缴纳","img":"/app/images/icon/20180205/icon_nianfei.png","type":9,"content":55}]
     * advert : [{"title":"这是广告位","img":"/app/images/icon/20180205/icon_nianfei.png","type":9,"content":10}]
     */

    @SerializedName("supplier_info")
    public SupplierInfoBean supplierInfo;
    @SerializedName("supplier_order")
    public ArrayList<MerchantItemBeanEntity> supplierOrder;
    @SerializedName("beLooked")
    public ArrayList<MerchantItemBeanEntity> beLooked;
    @SerializedName("advert")
    public ArrayList<AdvertBean> advert;

    public static class SupplierInfoBean {
        /**
         * supplier_name : 漫威宇宙
         * supplier_icon : http://img.lefenmall.net/goods/goods_info/82fe6c184d0a506f01c38a730486553651b260cc_icon.png
         * supplier_rank : 皇冠店铺
         * supplier_phone : 17638513353
         */

        @SerializedName("supplier_name")
        public String supplierName;
        @SerializedName("supplier_icon")
        public String supplierIcon;
        @SerializedName("supplier_rank")
        public String supplierRank;
        @SerializedName("supplier_phone")
        public String supplierPhone;
        @SerializedName("supplier_id")
        public String supplierId;
        @SerializedName("fee_type")
        public String feeType;
    }

    public static class AdvertBean {
        /**
         * title : 这是广告位
         * img : /app/images/icon/20180205/icon_nianfei.png
         * type : 9
         * content : 10
         */

        @SerializedName("title")
        public String title;
        @SerializedName("img")
        public String img;
        @SerializedName("type")
        public int type;
        @SerializedName("content")
        public String content;
    }
}
