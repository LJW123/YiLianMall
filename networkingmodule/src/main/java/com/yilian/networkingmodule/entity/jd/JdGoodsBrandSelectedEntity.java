package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东首页品牌精选实体类
 *
 * @author Created by zhaiyaohua on 2018/5/24.
 */

public class JdGoodsBrandSelectedEntity extends HttpResultBean {
    @SerializedName("banner1")
    public Banner1 banner1;
    @SerializedName("banner")
    public List<String> banner;
    @SerializedName("data")
    public List<Data> data;

    public List<String> getBanner() {
        if (banner == null) {
            return new ArrayList<>();
        }
        return banner;
    }

    public List<Data> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public static class Banner1 {
        @SerializedName("title")
        public String title;
        @SerializedName("type")
        public int type;
        @SerializedName("img")
        public String img;
        @SerializedName("content")
        public String content;
        @SerializedName("display")
        public int display;
    }

    public static class Data extends JdBrandSelectedMultiItem {
        /**
         * productArea : 中国大陆广东省中山市
         * imagePath : jfs/t18148/292/1823170128/451922/a167fb95/5ad82898N9731a861.jpg
         * brandName : 南极人（Nanjiren）
         * num : 725
         * goods_list : [{"sku":"5463473","name":"南极人（NanJiren）靠垫 升级款办公室午休趴睡抱枕午睡枕头","imagePath":"jfs/t11425/75/1714040157/148652/1f55f082/5a069c9fN3f4abba8.jpg","jdPrice":"159.00","return_bean":"109.00","sale":"1016"},{"sku":"3731376","name":"南极人（NanJiren）套件家纺 全棉双人加大四件套 思绪 1.8米床 220*240cm","imagePath":"jfs/t15166/100/241237090/199047/ce8faed9/5a279fc7Nd73abab2.jpg","jdPrice":"259.00","return_bean":"80.00","sale":"1023"},{"sku":"3606418","name":"南极人（NanJiren）被芯家纺 舒柔双人四季被子 米黄 2*2.3米 5斤","imagePath":"jfs/t9601/350/246423749/468207/d8f97208/59c9c220Nd0d26fc0.jpg","jdPrice":"178.00","return_bean":"73.00","sale":"972"},{"sku":"5384844","name":"南极人（nanJiren）皮革汽车座垫 汽车座套 五座四季通用 含珍珠棉头枕腰靠枕 豪华款四季座垫套装 炫酷黑","imagePath":"jfs/t7633/87/1045939453/159092/c52b55f4/599a3f58N8b41b453.jpg","jdPrice":"468.00","return_bean":"69.00","sale":"1021"},{"sku":"3606496","name":"南极人枕芯 全棉星级酒店羽丝绒安睡枕头 一对装（2只）饱满舒适双边立体","imagePath":"jfs/t11551/159/11971874/135345/13130c30/59e5aa00Nf078e743.jpg","jdPrice":"148.00","return_bean":"53.00","sale":"950"},{"sku":"3799979","name":"南极人（NanJiren）被芯家纺 舒适印花夏被 单人空调被 灰色地带 200*230cm","imagePath":"jfs/t4462/226/1277409845/263092/af1c1187/58db8651N6c18ddde.jpg","jdPrice":"129.00","return_bean":"50.00","sale":"1003"},{"sku":"5139745","name":"南极人（NanJiren）浴巾 纯棉素色柔软吸水大浴巾 单条 米色","imagePath":"jfs/t15715/28/1818986955/372421/bf4f033c/5a6192bdN9c5fca9e.jpg","jdPrice":"89.00","return_bean":"44.00","sale":"970"},{"sku":"4354876","name":"南极人（NanJiren）靠垫被 时尚靠垫靠枕抱枕被子两用夏凉被 灰色 110*150cm","imagePath":"jfs/t13963/256/1494064783/270055/d265f679/5a20bc0dN419bcefb.jpg","jdPrice":"79.00","return_bean":"34.00","sale":"1007"},{"sku":"6650499","name":"南极人4条装生理内裤女士防漏月经期姨妈例假卫生裤透气三角女生内裤 0012款4条 L/165（腰围1.9-2.3）","imagePath":"jfs/t17248/16/1874386018/164367/deabb54a/5adc53f6Ncd48d9a8.jpg","jdPrice":"79.00","return_bean":"29.10","sale":"1047"},{"sku":"4354900","name":"南极人（NanJiren）抱枕家纺 亚麻风格沙发靠垫办公室汽车靠枕 含芯 绿叶 45*45cm","imagePath":"jfs/t18769/50/2031835218/407953/13be4771/5ae18367N956a05bd.jpg","jdPrice":"49.00","return_bean":"20.00","sale":"954"},{"sku":"4354902","name":"南极人（NanJiren）抱枕 亚麻风格沙发靠垫办公室汽车靠枕 含芯 灰色 45*45cm","imagePath":"jfs/t18490/69/1501068172/303587/d87a2ab7/5acdac42N3dc96302.jpg","jdPrice":"49.00","return_bean":"20.00","sale":"1046"},{"sku":"4666419","name":"南极人（nanJiren）汽车头枕腰靠 太空记忆棉腰靠垫 护颈枕 两件套 炫酷黑","imagePath":"jfs/t10969/97/1336544562/126070/99085990/59df1280N69e2006a.jpg","jdPrice":"179.00","return_bean":"20.00","sale":"985"},{"sku":"4603932","name":"南极人 (Nanjiren)汽车座垫 夏季凉垫冰垫 木珠汽车坐垫 办公室座垫 小方垫 米咖色 前排单座","imagePath":"jfs/t4372/230/3650705312/365851/58e1ae27/58e48b58N150d76a6.jpg","jdPrice":"59.00","return_bean":"17.00","sale":"1024"},{"sku":"5192256","name":"南极人 (Nanjiren) 汽车方向盘套 车用头层牛皮 真皮把套 四季通用 黑色","imagePath":"jfs/t12637/103/1457482368/200039/18e96b63/5a5c6890Nfc560571.jpg","jdPrice":"109.00","return_bean":"16.35","sale":"990"},{"sku":"1776429","name":"南极人（Nanjiren）男士内裤净色宽边U凸短裤头平角中腰式内裤4条礼盒装 蓝黑宽边系列 XL","imagePath":"jfs/t17383/155/1593054940/382605/aa5d89cb/5ad044c5N107848f9.jpg","jdPrice":"59.00","return_bean":"14.90","sale":"1021"},{"sku":"1776434","name":"南极人（Nanjiren）男士内裤净色宽边U凸短裤头平角中腰式内裤4条礼盒装 蓝黑宽边系列 XXXL","imagePath":"jfs/t18412/112/1595704667/382605/aa5d89cb/5ad04516N44e989a3.jpg","jdPrice":"59.00","return_bean":"14.90","sale":"968"},{"sku":"1776441","name":"南极人（Nanjiren）男士内裤净色宽边U凸短裤头平角中腰式内裤4条礼盒装 蓝黑宽边系列 XXL","imagePath":"jfs/t18484/233/1612288799/382605/aa5d89cb/5ad044e2N63f65239.jpg","jdPrice":"59.00","return_bean":"14.90","sale":"1031"},{"sku":"1776472","name":"南极人（Nanjiren）男士内裤净色宽边U凸短裤头平角中腰式内裤4条礼盒装 蓝黑宽边系列 L","imagePath":"jfs/t19213/302/1589501832/382605/aa5d89cb/5ad044a7N44656980.jpg","jdPrice":"59.00","return_bean":"14.90","sale":"997"},{"sku":"4425374","name":"南极人 被芯家纺 棉花被芯 亲肤柔软空调被子 全棉夏凉被 千百度 200*230cm","imagePath":"jfs/t18520/65/1815869918/624750/313faaaf/5ad9a250N1665123c.jpg","jdPrice":"169.00","return_bean":"14.00","sale":"1037"},{"sku":"4486986","name":"南极人 被芯家纺 棉花被芯 亲肤柔软空调被子 全棉夏凉被 爱巢 200*230cm","imagePath":"jfs/t16597/180/1838869757/658668/68cab8f5/5ad9a119Ne76b5c07.jpg","jdPrice":"169.00","return_bean":"14.00","sale":"1001"}]
         */

        @SerializedName("productArea")
        public String productArea;
        @SerializedName("imagePath")
        public String imagePath;
        @SerializedName("brandName")
        public String brandName;
        @SerializedName("num")
        public String num;
        @SerializedName("goods_list")
        public List<Goods> goodsList;

        @Override
        public int getSpanSize() {
            return ITEM_TYPE_THREE;
        }

        @Override
        public int getItemType() {
            return ITEM_TYPE_THREE;
        }

        public static class Goods extends JdBrandSelectedMultiItem {
            /**
             * sku : 5463473
             * name : 南极人（NanJiren）靠垫 升级款办公室午休趴睡抱枕午睡枕头
             * imagePath : jfs/t11425/75/1714040157/148652/1f55f082/5a069c9fN3f4abba8.jpg
             * jdPrice : 159.00
             * return_bean : 109.00
             * sale : 1016
             */

            @SerializedName("sku")
            public String sku;
            @SerializedName("name")
            public String name;
            @SerializedName("imagePath")
            public String imagePath;
            @SerializedName("jdPrice")
            public float jdPrice;
            @SerializedName("return_bean")
            public String returnBean;
            @SerializedName("sale")
            public String sale;
            /**
             * 本地自定义字段
             * 0--隐藏分割线
             * 1--显示分割线
             */
            public int tag;

            public int getTag() {
                return tag;
            }

            public void setTag(int tag) {
                this.tag = tag;
            }

            @Override
            public int getSpanSize() {
                return ITEM_TYPE_ONE;
            }

            @Override
            public int getItemType() {
                return ITEM_TYPE_ONE;
            }
        }
    }
}
