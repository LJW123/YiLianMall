package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/5/22.
 * 品牌精选
 */

public class JDRecommendBrandEntity extends HttpResultBean {


    @SerializedName("banner")
    public List<JDBannerEntity> banner;
    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * productArea : 广东佛山
         * imagePath : jfs/t14944/238/2354076207/266151/d208ce00/5a9cb20aN02138345.jpg
         * brandName : 周大福（CHOW TAI FOOK）
         * num : 538
         * goods_list : [{"sku":"4451583","name":"周大福（CHOW TAI FOOK）臻选17916系列精致花形22K金项链套链吊坠 E122273 1480 40cm","imagePath":"jfs/t17158/134/665100515/137983/2a95b15c/5a9cf4e1N766057d9.jpg","jdPrice":"1479.00","return_bean":"0.00","sale":"995"},{"sku":"4945220","name":"周大福（CHOW TAI FOOK）许巍蓝莲盛开系列电黑足金黄金转运珠吊坠 R19083 1280","imagePath":"jfs/t15703/282/2342421566/424019/c62f3857/5a9caf29N435304a1.jpg","jdPrice":"1216.00","return_bean":"0.00","sale":"976"},{"sku":"5395508","name":"周大福（CHOW TAI FOOK）十二生肖狗 开开心心 足金黄金吊坠 F205804 68 约1.6克","imagePath":"jfs/t17575/51/352798761/145597/ee6690f7/5a6eced4N2fd5190c.jpg","jdPrice":"616.00","return_bean":"0.00","sale":"1019"},{"sku":"5709962","name":"周大福（CHOW TAI FOOK）心花盛放车花足金黄金手链 F159053 58 约3.8克 17.5cm","imagePath":"jfs/t19528/63/599529141/206272/d38afbaf/5a97beabN0aaf3fdc.jpg","jdPrice":"1361.00","return_bean":"0.00","sale":"991"},{"sku":"4451587","name":"周大福（CHOW TAI FOOK）情约系列PT950铂金钻石戒指 对戒 女戒 PT159299 3380 13号","imagePath":"jfs/t15223/269/2375804982/209168/90bd2e8f/5a9d0ac7N80cbde69.jpg","jdPrice":"3211.00","return_bean":"0.00","sale":"960"},{"sku":"4945228","name":"周大福（CHOW TAI FOOK）钟情 情约系列PT950铂金钻石戒指对戒男戒 PT159298 4380 16号","imagePath":"jfs/t16921/174/666883913/213913/2efaafc/5a9ce9e2Nce0ae1d4.jpg","jdPrice":"4161.00","return_bean":"0.00","sale":"1014"},{"sku":"5395536","name":"周大福（CHOW TAI FOOK）十二生肖狗 健健康康 足金黄金吊坠 F205473 68 约3.1克","imagePath":"jfs/t14620/279/2325895458/250916/8b06c850/5a97c541N1337c96b.jpg","jdPrice":"1131.00","return_bean":"0.00","sale":"958"},{"sku":"5709964","name":"周大福（CHOW TAI FOOK）心花盛放车花足金黄金手链 F159053 58 约 3.8克 16.25cm","imagePath":"jfs/t19528/63/599529141/206272/d38afbaf/5a97beabN0aaf3fdc.jpg","jdPrice":"1361.00","return_bean":"0.00","sale":"984"},{"sku":"4451601","name":"周大福（CHOW TAI FOOK）雪花PT950铂金耳钉 PT148796 1080","imagePath":"jfs/t15238/265/2124502219/248804/2af1f407/5a718e86N47101d29.jpg","jdPrice":"918.00","return_bean":"0.00","sale":"964"},{"sku":"4945242","name":"周大福（CHOW TAI FOOK）路路通转运珠足金黄金吊坠 F155440 48 约1.2克","imagePath":"jfs/t19702/336/338371305/311305/717b792e/5a6eb9eaNbf4dd5c6.jpg","jdPrice":"459.00","return_bean":"0.00","sale":"992"},{"sku":"5395554","name":"周大福（CHOW TAI FOOK）时尚优雅光身18K金项链 E51 1280 45cm","imagePath":"jfs/t15007/362/2433566059/139370/fa0b8673/5a9cbe4dN1818cbc7.jpg","jdPrice":"1088.00","return_bean":"0.00","sale":"984"},{"sku":"7135713","name":"周大福（CHOW TAI FOOK）蛇肚福字 足金黄金戒指 F166083 118 约6.6克","imagePath":"jfs/t16726/306/1488453389/164127/9207860f/5acc37f3N85448afe.jpg","jdPrice":"2381.00","return_bean":"0.00","sale":"962"},{"sku":"4451605","name":"周大福（CHOW TAI FOOK）女神系列蝶舞翩翩 耳钉黄K金18K金耳环 E121208 560","imagePath":"jfs/t14734/109/2410001151/80265/641801c8/5a9cb253N3430bfc4.jpg","jdPrice":"532.00","return_bean":"0.00","sale":"993"},{"sku":"4945252","name":"周大福（CHOW TAI FOOK）怦然系列 灵动优雅 18K金镶钻石吊坠 U136886 2900","imagePath":"jfs/t17638/207/629227932/230425/77da8653/5a9cce53N388c783d.jpg","jdPrice":"2668.00","return_bean":"0.00","sale":"970"},{"sku":"7135715","name":"周大福（CHOW TAI FOOK）因你而幸lucky字母足金黄金项链 吊坠 F205322 108 45cm 约6克","imagePath":"jfs/t17158/131/1487123533/124382/aba706ab/5acc37dcNa396117d.jpg","jdPrice":"2166.00","return_bean":"0.00","sale":"981"},{"sku":"4451609","name":"周大福（CHOW TAI FOOK）别致车花黄金手链 F147283 108 约5.2克 16.25cm","imagePath":"jfs/t15112/351/2342582522/199964/d1624563/5a97c8b0Na56ea0db.jpg","jdPrice":"1891.00","return_bean":"0.00","sale":"1024"},{"sku":"4945270","name":"周大福（CHOW TAI FOOK）婚嫁男女款足金黄金戒指 F152999 158 约9.6克","imagePath":"jfs/t18262/31/583920692/395086/30301c77/5a97c17fNfceedaae.jpg","jdPrice":"3450.00","return_bean":"0.00","sale":"1022"},{"sku":"5399612","name":"周大福（CHOW TAI FOOK）生肖鸡福袋 足金黄金吊坠 F201061 48 约2克","imagePath":"jfs/t17539/147/561871373/320240/ff4c5f66/5a97b96eN1255566e.jpg","jdPrice":"734.00","return_bean":"0.00","sale":"984"},{"sku":"7136663","name":"周大福（CHOW TAI FOOK）福气笑面佛公 翡翠吊坠 K61944 560 ","imagePath":"jfs/t17155/236/1492078595/260357/f757cc75/5acc366fN039e77b5.jpg","jdPrice":"515.00","return_bean":"0.00","sale":"965"},{"sku":"4451615","name":"周大福（CHOW TAI FOOK）五瓣花 PT950铂金耳钉 PT124476 760","imagePath":"jfs/t15196/327/2088144851/205060/64a25e93/5a6ede3cN40ecf16f.jpg","jdPrice":"722.00","return_bean":"0.00","sale":"987"}]
         */
        /**
         * 产地
         */
        @SerializedName("productArea")
        public String productArea;
        /**
         * 品牌图片
         */
        @SerializedName("imagePath")
        public String imagePath;
        /**
         * 品牌名称
         */
        @SerializedName("brandName")
        public String brandName;
        @SerializedName("num")
        public String num;
        /**
         * 商品列表
         */
        @SerializedName("goods_list")
        public List<JDGoodsAbstractInfoEntity> goodsList;
    }
}
