package com.yilian.networkingmodule.entity.jd;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Created by  on 2018/5/22.
 *         商品详情
 */

public class JDGoodsDetailInfoEntity extends HttpResultBean implements Serializable {

    /**
     * login_status : 0
     * data : {"goods_info":{"saleUnit":"台","weight":"1.2","productArea":"广东省深圳市","wareQD":"平板电脑x1、产品说明与保修凭证x1、充电器x1、卡针x1","imagePath":"jfs/t20263/278/10505433/70505/33db77e2/5af51769Ne24d6505.jpg","param":"<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tr><th class=\"tdTitle\" colspan=\"2\">主体<\/th><tr><tr><td class=\"tdTitle\">型号<\/td><td>Remix Pro<\/td><\/tr><tr><td class=\"tdTitle\">颜色<\/td><td>前黑后银白色<\/td><\/tr><tr><td class=\"tdTitle\">材质/工艺<\/td><td>镁合金<\/td><\/tr><tr><td class=\"tdTitle\">上市时间<\/td><td>2018年5月<\/td><\/tr><tr><td class=\"tdTitle\">操作系统<\/td><td>JIDE OS<\/td><\/tr><tr><td class=\"tdTitle\">翻新类型<\/td><td>全新<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">配置<\/th><tr><tr><td class=\"tdTitle\">存储容量<\/td><td>32GB<\/td><\/tr><tr><td class=\"tdTitle\">处理器<\/td><td>骁龙652<\/td><\/tr><tr><td class=\"tdTitle\">核心数量<\/td><td>八核<\/td><\/tr><tr><td class=\"tdTitle\">处理器速度<\/td><td>其他<\/td><\/tr><tr><td class=\"tdTitle\">系统内存<\/td><td>3GB<\/td><\/tr><tr><td class=\"tdTitle\">扩展支持<\/td><td>Micro SD (TF)卡<\/td><\/tr><tr><td class=\"tdTitle\">可扩展容量<\/td><td>256GB<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">显示<\/th><tr><tr><td class=\"tdTitle\">屏幕尺寸<\/td><td>12英寸<\/td><\/tr><tr><td class=\"tdTitle\">屏幕分辨率<\/td><td>其他<\/td><\/tr><tr><td class=\"tdTitle\">屏幕比例<\/td><td>3:2<\/td><\/tr><tr><td class=\"tdTitle\">屏幕类型<\/td><td>IPS<\/td><\/tr><tr><td class=\"tdTitle\">屏幕描述<\/td><td>2160*1440<\/td><\/tr><tr><td class=\"tdTitle\">指取设备<\/td><td>触摸<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">连接<\/th><tr><tr><td class=\"tdTitle\">WiFi功能<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">蓝牙功能<\/td><td>支持<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">端口<\/th><tr><tr><td class=\"tdTitle\">音频接口<\/td><td>3.5mm<\/td><\/tr><tr><td class=\"tdTitle\">视频接口<\/td><td>其他<\/td><\/tr><tr><td class=\"tdTitle\">USB接口<\/td><td>标准2.0<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">音效<\/th><tr><tr><td class=\"tdTitle\">扬声器<\/td><td>2个<\/td><\/tr><tr><td class=\"tdTitle\">麦克风<\/td><td>2个<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">功能<\/th><tr><tr><td class=\"tdTitle\">摄像头<\/td><td>有<\/td><\/tr><tr><td class=\"tdTitle\">前置摄像头<\/td><td>500W<\/td><\/tr><tr><td class=\"tdTitle\">后置摄像头<\/td><td>800w<\/td><\/tr><tr><td class=\"tdTitle\">多点触控<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">GPS导航<\/td><td>无<\/td><\/tr><tr><td class=\"tdTitle\">电影播放<\/td><td>1080P<\/td><\/tr><tr><td class=\"tdTitle\">Flash播放<\/td><td>不支持<\/td><\/tr><tr><td class=\"tdTitle\">方向感应器<\/td><td>不支持<\/td><\/tr><tr><td class=\"tdTitle\">内置感应<\/td><td>不支持<\/td><\/tr><tr><td class=\"tdTitle\">陀螺仪<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">重力感应<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">光线感应<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">HALL开关<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">投影功能<\/td><td>不支持<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">电源<\/th><tr><tr><td class=\"tdTitle\">电池类型<\/td><td>锂电池<\/td><\/tr><tr><td class=\"tdTitle\">电池容量<\/td><td>9000mAh<\/td><\/tr><tr><td class=\"tdTitle\">续航时间<\/td><td>8h<\/td><\/tr><tr><td class=\"tdTitle\">输入电压<\/td><td>其他<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">规格<\/th><tr><tr><td class=\"tdTitle\">尺寸<\/td><td>289.5*207*6.9mm<\/td><\/tr><tr><td class=\"tdTitle\">净重<\/td><td>640g<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">特性<\/th><tr><tr><td class=\"tdTitle\">特性<\/td><td>12英寸大屏幕超高清<\/td><\/tr><\/table>","state":1,"sku":7578335,"shouhou":null,"brandName":"技德","upc":"7578335","appintroduce":"<div id='zbViewModulesH'  value='5800'><\/div><input id='zbViewModulesHeight' type='hidden' value='5800'/><div skudesign=\"100011\"><\/div><div class=\"ssd-module-wrap\" >\n    <div class=\"ssd-module M15259461825041 animate-M15259461825041\">\n        <\/div>\n<div class=\"ssd-module M15259462054642 animate-M15259462054642\">\n        <\/div>\n<div class=\"ssd-module M15259466422204 animate-M15259466422204\">\n        <\/div>\n<div class=\"ssd-module M15259466700795 animate-M15259466700795\">\n        <\/div>\n<div class=\"ssd-module M15259466850446 animate-M15259466850446\">\n        <\/div>\n<div class=\"ssd-module M15259467158117 animate-M15259467158117\">\n        <\/div>\n<div class=\"ssd-module M15259467356188 animate-M15259467356188\">\n        <\/div>\n<div class=\"ssd-module M15259467832609 animate-M15259467832609\">\n        <\/div>\n<div class=\"ssd-module M152594680182510 animate-M152594680182510\">\n        <\/div>\n<div class=\"ssd-module M152594681757311 animate-M152594681757311\">\n        <\/div>\n<div class=\"ssd-module M152594683405212 animate-M152594683405212\">\n        <\/div>\n\n<\/div><!-- 2018-05-10 06:23:22 --> \n<link rel='stylesheet' type='text/css' href='//sku-market-gw.jd.com/css/mobile/7578335.css?t=1526925377523' media='all' />","category":"670;671;2694","name":"技德 Remix Pro","introduction":"<div cssurl='//sku-market-gw.jd.com/css/pc/7578335.css?t=1526005180143'><\/div><div id='zbViewModulesH'  value='6800'><\/div><input id='zbViewModulesHeight' type='hidden' value='6800'/><div skudesign=\"100010\"><\/div><div class=\"ssd-module-wrap\" >\n    <div class=\"ssd-module M15259461825041 animate-M15259461825041\">\n        <\/div>\n<div class=\"ssd-module M15259462054642 animate-M15259462054642\">\n        <\/div>\n<div class=\"ssd-module M15259466422204 animate-M15259466422204\">\n        <\/div>\n<div class=\"ssd-module M15259466700795 animate-M15259466700795\">\n        <\/div>\n<div class=\"ssd-module M15259466850446 animate-M15259466850446\">\n        <\/div>\n<div class=\"ssd-module M15259467158117 animate-M15259467158117\">\n        <\/div>\n<div class=\"ssd-module M15259467356188 animate-M15259467356188\">\n        <\/div>\n<div class=\"ssd-module M15259467832609 animate-M15259467832609\">\n        <\/div>\n<div class=\"ssd-module M152594680182510 animate-M152594680182510\">\n        <\/div>\n<div class=\"ssd-module M152594681757311 animate-M152594681757311\">\n        <\/div>\n<div class=\"ssd-module M152594683405212 animate-M152594683405212\">\n        <\/div>\n\n<\/div><!-- 2018-05-10 06:21:27 --> \n<link rel='stylesheet' type='text/css' href='//sku-market-gw.jd.com/css/pc/7578335.css?t=1526943620567' media='all' />"},"goods_price":{"marketPrice":3500,"price":3350,"skuId":7578335,"jdPrice":3350,"return_bean":0},"address_info":{"provinceId":"7","county":"管城区","cityId":"412","province":"河南","townId":"51752","town":"城区","countyId":"3546","nation":"中国","city":"郑州市","nationId":"4744"},"pics":["jfs/t20263/278/10505433/70505/33db77e2/5af51769Ne24d6505.jpg","jfs/t17128/258/2470175392/89364/5947f309/5af51770N04a2b2fb.jpg","jfs/t20485/65/9557757/57738/feae1cac/5af51776N33d59049.jpg","jfs/t19882/235/468412807/117362/4f9d3c80/5af51777N5b00cf6f.jpg","jfs/t18907/91/2452730703/22070/bce8a0df/5af51780Nb501ebc0.jpg"],"area_limit":1}
     */

    @SerializedName("login_status")
    public int loginStatus;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean implements Serializable {

        /**
         * //0普通京东商品  1购物卡京东商品
         */
        @SerializedName("jd_type")
        public int jdType;
        /**
         * goods_info : {"saleUnit":"台","weight":"1.2","productArea":"广东省深圳市","wareQD":"平板电脑x1、产品说明与保修凭证x1、充电器x1、卡针x1","imagePath":"jfs/t20263/278/10505433/70505/33db77e2/5af51769Ne24d6505.jpg","param":"<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tr><th class=\"tdTitle\" colspan=\"2\">主体<\/th><tr><tr><td class=\"tdTitle\">型号<\/td><td>Remix Pro<\/td><\/tr><tr><td class=\"tdTitle\">颜色<\/td><td>前黑后银白色<\/td><\/tr><tr><td class=\"tdTitle\">材质/工艺<\/td><td>镁合金<\/td><\/tr><tr><td class=\"tdTitle\">上市时间<\/td><td>2018年5月<\/td><\/tr><tr><td class=\"tdTitle\">操作系统<\/td><td>JIDE OS<\/td><\/tr><tr><td class=\"tdTitle\">翻新类型<\/td><td>全新<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">配置<\/th><tr><tr><td class=\"tdTitle\">存储容量<\/td><td>32GB<\/td><\/tr><tr><td class=\"tdTitle\">处理器<\/td><td>骁龙652<\/td><\/tr><tr><td class=\"tdTitle\">核心数量<\/td><td>八核<\/td><\/tr><tr><td class=\"tdTitle\">处理器速度<\/td><td>其他<\/td><\/tr><tr><td class=\"tdTitle\">系统内存<\/td><td>3GB<\/td><\/tr><tr><td class=\"tdTitle\">扩展支持<\/td><td>Micro SD (TF)卡<\/td><\/tr><tr><td class=\"tdTitle\">可扩展容量<\/td><td>256GB<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">显示<\/th><tr><tr><td class=\"tdTitle\">屏幕尺寸<\/td><td>12英寸<\/td><\/tr><tr><td class=\"tdTitle\">屏幕分辨率<\/td><td>其他<\/td><\/tr><tr><td class=\"tdTitle\">屏幕比例<\/td><td>3:2<\/td><\/tr><tr><td class=\"tdTitle\">屏幕类型<\/td><td>IPS<\/td><\/tr><tr><td class=\"tdTitle\">屏幕描述<\/td><td>2160*1440<\/td><\/tr><tr><td class=\"tdTitle\">指取设备<\/td><td>触摸<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">连接<\/th><tr><tr><td class=\"tdTitle\">WiFi功能<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">蓝牙功能<\/td><td>支持<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">端口<\/th><tr><tr><td class=\"tdTitle\">音频接口<\/td><td>3.5mm<\/td><\/tr><tr><td class=\"tdTitle\">视频接口<\/td><td>其他<\/td><\/tr><tr><td class=\"tdTitle\">USB接口<\/td><td>标准2.0<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">音效<\/th><tr><tr><td class=\"tdTitle\">扬声器<\/td><td>2个<\/td><\/tr><tr><td class=\"tdTitle\">麦克风<\/td><td>2个<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">功能<\/th><tr><tr><td class=\"tdTitle\">摄像头<\/td><td>有<\/td><\/tr><tr><td class=\"tdTitle\">前置摄像头<\/td><td>500W<\/td><\/tr><tr><td class=\"tdTitle\">后置摄像头<\/td><td>800w<\/td><\/tr><tr><td class=\"tdTitle\">多点触控<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">GPS导航<\/td><td>无<\/td><\/tr><tr><td class=\"tdTitle\">电影播放<\/td><td>1080P<\/td><\/tr><tr><td class=\"tdTitle\">Flash播放<\/td><td>不支持<\/td><\/tr><tr><td class=\"tdTitle\">方向感应器<\/td><td>不支持<\/td><\/tr><tr><td class=\"tdTitle\">内置感应<\/td><td>不支持<\/td><\/tr><tr><td class=\"tdTitle\">陀螺仪<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">重力感应<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">光线感应<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">HALL开关<\/td><td>支持<\/td><\/tr><tr><td class=\"tdTitle\">投影功能<\/td><td>不支持<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">电源<\/th><tr><tr><td class=\"tdTitle\">电池类型<\/td><td>锂电池<\/td><\/tr><tr><td class=\"tdTitle\">电池容量<\/td><td>9000mAh<\/td><\/tr><tr><td class=\"tdTitle\">续航时间<\/td><td>8h<\/td><\/tr><tr><td class=\"tdTitle\">输入电压<\/td><td>其他<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">规格<\/th><tr><tr><td class=\"tdTitle\">尺寸<\/td><td>289.5*207*6.9mm<\/td><\/tr><tr><td class=\"tdTitle\">净重<\/td><td>640g<\/td><\/tr><tr><th class=\"tdTitle\" colspan=\"2\">特性<\/th><tr><tr><td class=\"tdTitle\">特性<\/td><td>12英寸大屏幕超高清<\/td><\/tr><\/table>","state":1,"sku":7578335,"shouhou":null,"brandName":"技德","upc":"7578335","appintroduce":"<div id='zbViewModulesH'  value='5800'><\/div><input id='zbViewModulesHeight' type='hidden' value='5800'/><div skudesign=\"100011\"><\/div><div class=\"ssd-module-wrap\" >\n    <div class=\"ssd-module M15259461825041 animate-M15259461825041\">\n        <\/div>\n<div class=\"ssd-module M15259462054642 animate-M15259462054642\">\n        <\/div>\n<div class=\"ssd-module M15259466422204 animate-M15259466422204\">\n        <\/div>\n<div class=\"ssd-module M15259466700795 animate-M15259466700795\">\n        <\/div>\n<div class=\"ssd-module M15259466850446 animate-M15259466850446\">\n        <\/div>\n<div class=\"ssd-module M15259467158117 animate-M15259467158117\">\n        <\/div>\n<div class=\"ssd-module M15259467356188 animate-M15259467356188\">\n        <\/div>\n<div class=\"ssd-module M15259467832609 animate-M15259467832609\">\n        <\/div>\n<div class=\"ssd-module M152594680182510 animate-M152594680182510\">\n        <\/div>\n<div class=\"ssd-module M152594681757311 animate-M152594681757311\">\n        <\/div>\n<div class=\"ssd-module M152594683405212 animate-M152594683405212\">\n        <\/div>\n\n<\/div><!-- 2018-05-10 06:23:22 --> \n<link rel='stylesheet' type='text/css' href='//sku-market-gw.jd.com/css/mobile/7578335.css?t=1526925377523' media='all' />","category":"670;671;2694","name":"技德 Remix Pro","introduction":"<div cssurl='//sku-market-gw.jd.com/css/pc/7578335.css?t=1526005180143'><\/div><div id='zbViewModulesH'  value='6800'><\/div><input id='zbViewModulesHeight' type='hidden' value='6800'/><div skudesign=\"100010\"><\/div><div class=\"ssd-module-wrap\" >\n    <div class=\"ssd-module M15259461825041 animate-M15259461825041\">\n        <\/div>\n<div class=\"ssd-module M15259462054642 animate-M15259462054642\">\n        <\/div>\n<div class=\"ssd-module M15259466422204 animate-M15259466422204\">\n        <\/div>\n<div class=\"ssd-module M15259466700795 animate-M15259466700795\">\n        <\/div>\n<div class=\"ssd-module M15259466850446 animate-M15259466850446\">\n        <\/div>\n<div class=\"ssd-module M15259467158117 animate-M15259467158117\">\n        <\/div>\n<div class=\"ssd-module M15259467356188 animate-M15259467356188\">\n        <\/div>\n<div class=\"ssd-module M15259467832609 animate-M15259467832609\">\n        <\/div>\n<div class=\"ssd-module M152594680182510 animate-M152594680182510\">\n        <\/div>\n<div class=\"ssd-module M152594681757311 animate-M152594681757311\">\n        <\/div>\n<div class=\"ssd-module M152594683405212 animate-M152594683405212\">\n        <\/div>\n\n<\/div><!-- 2018-05-10 06:21:27 --> \n<link rel='stylesheet' type='text/css' href='//sku-market-gw.jd.com/css/pc/7578335.css?t=1526943620567' media='all' />"}
         * goods_price : {"marketPrice":3500,"price":3350,"skuId":7578335,"jdPrice":3350,"return_bean":0}
         * address_info : {"provinceId":"7","county":"管城区","cityId":"412","province":"河南","townId":"51752","town":"城区","countyId":"3546","nation":"中国","city":"郑州市","nationId":"4744"}
         * pics : ["jfs/t20263/278/10505433/70505/33db77e2/5af51769Ne24d6505.jpg","jfs/t17128/258/2470175392/89364/5947f309/5af51770N04a2b2fb.jpg","jfs/t20485/65/9557757/57738/feae1cac/5af51776N33d59049.jpg","jfs/t19882/235/468412807/117362/4f9d3c80/5af51777N5b00cf6f.jpg","jfs/t18907/91/2452730703/22070/bce8a0df/5af51780Nb501ebc0.jpg"]
         * area_limit : 1
         */

        @SerializedName("goods_info")
        public GoodsInfoBean goodsInfo;
        @SerializedName("goods_price")
        public GoodsPriceBean goodsPrice;
        @SerializedName("address_info")
        public JDShippingAddressInfoEntity.DataBean addressInfo;
        /**
         * //是否区域限购  0限购 1不限购
         */
        @SerializedName("area_limit")
        public int areaLimit;
        /**
         * //轮播图图片url
         */
        @SerializedName("pics")
        public List<String> pics;

        @Override
        public String toString() {
            return "DataBean{" +
                    "goodsInfo=" + goodsInfo +
                    ", goodsPrice=" + goodsPrice +
                    ", addressInfo=" + addressInfo +
                    ", areaLimit=" + areaLimit +
                    ", pics=" + pics +
                    '}';
        }

        public static class GoodsInfoBean implements Serializable {
            /**
             * 支持7天无理由退换货
             */
            public static final int IS7_TO_RETURN=1;
            /**
             * 不支持7天无理由退换货
             */
            public static final int NOT7_TO_RETURN=0;
            /**
             * 是否支持7天退货   1支持   0不支持
             */
            @SerializedName("is7ToReturn")
            public int is7ToReturn;            @Override
            public String toString() {
                return "GoodsInfoBean{" +
                        "is7ToReturn=" + is7ToReturn +
                        ", saleUnit='" + saleUnit + '\'' +
                        ", weight='" + weight + '\'' +
                        ", productArea='" + productArea + '\'' +
                        ", wareQD='" + wareQD + '\'' +
                        ", imagePath='" + imagePath + '\'' +
                        ", param='" + param + '\'' +
                        ", state=" + state +
                        ", sku='" + sku + '\'' +
                        ", shouhou='" + shouhou + '\'' +
                        ", brandName='" + brandName + '\'' +
                        ", upc='" + upc + '\'' +
                        ", appintroduce='" + appintroduce + '\'' +
                        ", category='" + category + '\'' +
                        ", name='" + name + '\'' +
                        ", introduction='" + introduction + '\'' +
                        '}';
            }
            /**
             * saleUnit : 台
             * weight : 1.2
             * productArea : 广东省深圳市
             * wareQD : 平板电脑x1、产品说明与保修凭证x1、充电器x1、卡针x1
             * imagePath : jfs/t20263/278/10505433/70505/33db77e2/5af51769Ne24d6505.jpg
             * param : <table cellpadding="0" cellspacing="1" width="100%" border="0" class="Ptable"><tr><th class="tdTitle" colspan="2">主体</th><tr><tr><td class="tdTitle">型号</td><td>Remix Pro</td></tr><tr><td class="tdTitle">颜色</td><td>前黑后银白色</td></tr><tr><td class="tdTitle">材质/工艺</td><td>镁合金</td></tr><tr><td class="tdTitle">上市时间</td><td>2018年5月</td></tr><tr><td class="tdTitle">操作系统</td><td>JIDE OS</td></tr><tr><td class="tdTitle">翻新类型</td><td>全新</td></tr><tr><th class="tdTitle" colspan="2">配置</th><tr><tr><td class="tdTitle">存储容量</td><td>32GB</td></tr><tr><td class="tdTitle">处理器</td><td>骁龙652</td></tr><tr><td class="tdTitle">核心数量</td><td>八核</td></tr><tr><td class="tdTitle">处理器速度</td><td>其他</td></tr><tr><td class="tdTitle">系统内存</td><td>3GB</td></tr><tr><td class="tdTitle">扩展支持</td><td>Micro SD (TF)卡</td></tr><tr><td class="tdTitle">可扩展容量</td><td>256GB</td></tr><tr><th class="tdTitle" colspan="2">显示</th><tr><tr><td class="tdTitle">屏幕尺寸</td><td>12英寸</td></tr><tr><td class="tdTitle">屏幕分辨率</td><td>其他</td></tr><tr><td class="tdTitle">屏幕比例</td><td>3:2</td></tr><tr><td class="tdTitle">屏幕类型</td><td>IPS</td></tr><tr><td class="tdTitle">屏幕描述</td><td>2160*1440</td></tr><tr><td class="tdTitle">指取设备</td><td>触摸</td></tr><tr><th class="tdTitle" colspan="2">连接</th><tr><tr><td class="tdTitle">WiFi功能</td><td>支持</td></tr><tr><td class="tdTitle">蓝牙功能</td><td>支持</td></tr><tr><th class="tdTitle" colspan="2">端口</th><tr><tr><td class="tdTitle">音频接口</td><td>3.5mm</td></tr><tr><td class="tdTitle">视频接口</td><td>其他</td></tr><tr><td class="tdTitle">USB接口</td><td>标准2.0</td></tr><tr><th class="tdTitle" colspan="2">音效</th><tr><tr><td class="tdTitle">扬声器</td><td>2个</td></tr><tr><td class="tdTitle">麦克风</td><td>2个</td></tr><tr><th class="tdTitle" colspan="2">功能</th><tr><tr><td class="tdTitle">摄像头</td><td>有</td></tr><tr><td class="tdTitle">前置摄像头</td><td>500W</td></tr><tr><td class="tdTitle">后置摄像头</td><td>800w</td></tr><tr><td class="tdTitle">多点触控</td><td>支持</td></tr><tr><td class="tdTitle">GPS导航</td><td>无</td></tr><tr><td class="tdTitle">电影播放</td><td>1080P</td></tr><tr><td class="tdTitle">Flash播放</td><td>不支持</td></tr><tr><td class="tdTitle">方向感应器</td><td>不支持</td></tr><tr><td class="tdTitle">内置感应</td><td>不支持</td></tr><tr><td class="tdTitle">陀螺仪</td><td>支持</td></tr><tr><td class="tdTitle">重力感应</td><td>支持</td></tr><tr><td class="tdTitle">光线感应</td><td>支持</td></tr><tr><td class="tdTitle">HALL开关</td><td>支持</td></tr><tr><td class="tdTitle">投影功能</td><td>不支持</td></tr><tr><th class="tdTitle" colspan="2">电源</th><tr><tr><td class="tdTitle">电池类型</td><td>锂电池</td></tr><tr><td class="tdTitle">电池容量</td><td>9000mAh</td></tr><tr><td class="tdTitle">续航时间</td><td>8h</td></tr><tr><td class="tdTitle">输入电压</td><td>其他</td></tr><tr><th class="tdTitle" colspan="2">规格</th><tr><tr><td class="tdTitle">尺寸</td><td>289.5*207*6.9mm</td></tr><tr><td class="tdTitle">净重</td><td>640g</td></tr><tr><th class="tdTitle" colspan="2">特性</th><tr><tr><td class="tdTitle">特性</td><td>12英寸大屏幕超高清</td></tr></table>
             * state : 1
             * sku : 7578335
             * shouhou : null
             * brandName : 技德
             * upc : 7578335
             * appintroduce : <div id='zbViewModulesH'  value='5800'></div><input id='zbViewModulesHeight' type='hidden' value='5800'/><div skudesign="100011"></div><div class="ssd-module-wrap" >
             </div><!-- 2018-05-10 06:23:22 -->
             <link rel='stylesheet' type='text/css' href='//sku-market-gw.jd.com/css/mobile/7578335.css?t=1526925377523' media='all' />
             * category : 670;671;2694
             * name : 技德 Remix Pro
             * introduction : <div cssurl='//sku-market-gw.jd.com/css/pc/7578335.css?t=1526005180143'></div><div id='zbViewModulesH'  value='6800'></div><input id='zbViewModulesHeight' type='hidden' value='6800'/><div skudesign="100010"></div><div class="ssd-module-wrap" >
             </div><!-- 2018-05-10 06:21:27 -->
             <link rel='stylesheet' type='text/css' href='//sku-market-gw.jd.com/css/pc/7578335.css?t=1526943620567' media='all' />
             */
            /**
             * 商品销售单位
             */
            @SerializedName("saleUnit")
            public String saleUnit;
            /**
             * //重量
             */
            @SerializedName("weight")
            public String weight;
            /**
             * //产地
             */
            @SerializedName("productArea")
            public String productArea;
            /**
             * //包装清单 和shouhou组合成包装售后
             */
            @SerializedName("wareQD")
            public String wareQD;
            /**
             *
             */
            @SerializedName("imagePath")
            public String imagePath;
            /**
             * 规格参数 富文本
             */
            @SerializedName("param")
            public String param;
            /**
             * //上下架状态  0下架  1上架
             */
            public static final int GOODS_SOLD_OUT=0;
            @SerializedName("state")
            public int state;
            @SerializedName("sku")
            public String sku;
            /**
             * //售后信息  有可能为null 和包装清单组合成包装售后
             */
            @Nullable
            @SerializedName("shouhou")
            public String shouhou;
            /**
             * //品牌名称
             */
            @SerializedName("brandName")
            public String brandName;
            /**
             * //条形码
             */
            @SerializedName("upc")
            public String upc;
            /**
             * 商品介绍 富文本 手机端
             */
            @SerializedName("appintroduce")
            public String appintroduce;
            /**
             * //分类  "670;671;2694" 一级；二级；三级 分号分隔
             */
            @SerializedName("category")
            public String category;
            /**
             * //商品名称
             */
            @SerializedName("name")
            public String name;
            /**
             * 商品介绍 富文本  PC端
             */
            @SerializedName("introduction")
            public String introduction;

        }

        public static class GoodsPriceBean implements Serializable {
            /**
             * marketPrice : 3500
             * price : 3350
             * skuId : 7578335
             * jdPrice : 3350
             * return_bean : 0
             */
            /**
             * 可得益豆为0
             */
            public static final String RETURN_NON = "0";
            /**
             * //市场价
             */
            @SerializedName("marketPrice")
            public String marketPrice;
            /**
             * //协议价
             */
            @SerializedName("price")
            public String price;
            /**
             *
             */
            @SerializedName("skuId")
            public String skuId;
            /**
             * 消费者购买价
             */
            @SerializedName("jdPrice")
            public String jdPrice;
            /**
             * 赠送益豆
             */
            @SerializedName("return_bean")
            public String returnBean;
        }

    }

    @Override
    public String toString() {
        return "JDGoodsDetailInfoEntity{" +
                "loginStatus=" + loginStatus +
                ", data=" + data +
                '}';
    }
}
