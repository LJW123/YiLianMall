package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaofo on 2018/7/16.
 */

public class SnGoodsDetailInfoEntity extends HttpResultBean implements Serializable{


    /**
     * info : {"id":"10","skuId":"109527340","name":"浪木（LM） JRL-1010M 饮水机","weight":"8.99","image":"http://image4.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_1_400x400.jpg","state":1,"saleUnit":"件","brand":"LM","category":"0001a280-88","sale":"0","real_sale":"0","is_del":"0","price":"590.73","snPrice":"609.00","discountRate":"0.97","all_return_bean":"18.27","return_bean":"11.89","rate":"1.95","introduction":"<p><img onload=\"if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}\" src=\"http://image.suning.cn/uimg/sop/commodity/201405220854185988_x.jpg\" alt=\"\" width=\"750\" height=\"591\" /><img onload=\"if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}\" src=\"http://image.suning.cn/uimg/sop/commodity/201405220855074919_x.jpg\" alt=\"\" width=\"750\" height=\"2400\" /><img onload=\"if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}\" src=\"http://image.suning.cn/uimg/sop/commodity/201405220855114834_x.jpg\" alt=\"\" width=\"750\" height=\"2400\" /><img onload=\"if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}\" src=\"http://image.suning.cn/uimg/sop/commodity/201405220855168814_x.jpg\" alt=\"\" width=\"750\" height=\"2400\" /><img onload=\"if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}\" src=\"http://image.suning.cn/uimg/sop/commodity/201405220853208239_x.jpg\" alt=\"\" width=\"750\" height=\"140\" /><img onload=\"if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}\" src=\"http://image.suning.cn/uimg/sop/commodity/201405220852137485_x.jpg\" alt=\"\" width=\"750\" height=\"2476\" /><\/p>","img_arr":["http://image5.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_1_800x800.jpg","http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_2_800x800.jpg","http://image4.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_3_800x800.jpg","http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_4_800x800.jpg","http://image3.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_5_800x800.jpg"],"increaseTicket":"01","noReasonLimit":"7","noReasonTip":"7天无理由退货","returnGoods":"01"}
     */

    @SerializedName("info")
    public InfoBean info;

    public static class InfoBean implements Serializable{
        /**
         * id : 10
         * skuId : 109527340
         * name : 浪木（LM） JRL-1010M 饮水机
         * weight : 8.99
         * image : http://image4.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_1_400x400.jpg
         * state : 1
         * saleUnit : 件
         * brand : LM
         * category : 0001a280-88
         * sale : 0
         * real_sale : 0
         * is_del : 0
         * price : 590.73
         * snPrice : 609.00
         * discountRate : 0.97
         * all_return_bean : 18.27
         * return_bean : 11.89
         * rate : 1.95
         * introduction : <p><img onload="if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}" src="http://image.suning.cn/uimg/sop/commodity/201405220854185988_x.jpg" alt="" width="750" height="591" /><img onload="if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}" src="http://image.suning.cn/uimg/sop/commodity/201405220855074919_x.jpg" alt="" width="750" height="2400" /><img onload="if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}" src="http://image.suning.cn/uimg/sop/commodity/201405220855114834_x.jpg" alt="" width="750" height="2400" /><img onload="if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}" src="http://image.suning.cn/uimg/sop/commodity/201405220855168814_x.jpg" alt="" width="750" height="2400" /><img onload="if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}" src="http://image.suning.cn/uimg/sop/commodity/201405220853208239_x.jpg" alt="" width="750" height="140" /><img onload="if(this.width>750){this.height=this.height*(750.0/this.width); this.width = 750;}" src="http://image.suning.cn/uimg/sop/commodity/201405220852137485_x.jpg" alt="" width="750" height="2476" /></p>
         * img_arr : ["http://image5.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_1_800x800.jpg","http://image2.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_2_800x800.jpg","http://image4.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_3_800x800.jpg","http://image1.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_4_800x800.jpg","http://image3.suning.cn/uimg/b2c/newcatentries/0000000000-000000000109527340_5_800x800.jpg"]
         * increaseTicket : 01
         * noReasonLimit : 7
         * noReasonTip : 7天无理由退货
         * returnGoods : 01
         */

        @SerializedName("id")
        public String id;
        @SerializedName("skuId")
        public String skuId;
        @SerializedName("name")
        public String name;
        @SerializedName("weight")
        public String weight;
        @SerializedName("image")
        public String image;
        @SerializedName("state")
        public int state;
        @SerializedName("saleUnit")
        public String saleUnit;
        @SerializedName("brand")
        public String brand;
        @SerializedName("category")
        public String category;
        @SerializedName("sale")
        public String sale;
        @SerializedName("real_sale")
        public String realSale;
        @SerializedName("is_del")
        public String isDel;
        @SerializedName("price")
        public String price;
        @SerializedName("snPrice")
        public String snPrice;
        @SerializedName("discountRate")
        public String discountRate;
        @SerializedName("all_return_bean")
        public String allReturnBean;
        @SerializedName("return_bean")
        public String returnBean;
        @SerializedName("rate")
        public String rate;
        @SerializedName("introduction")
        public String introduction;
        @SerializedName("increaseTicket")
        public String increaseTicket;
        @SerializedName("noReasonLimit")
        public String noReasonLimit;
        @SerializedName("noReasonTip")
        public String noReasonTip;
        @SerializedName("returnGoods")
        public String returnGoods;
        @SerializedName("img_arr")
        public List<String> imgArr;
    }

    /**
     * 支持七天退货
     */
    public static final String SUPPORT_GOODS_RETURN="01";

}
