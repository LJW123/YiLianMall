package com.yilian.networkingmodule.entity.shoppingcard;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 购物卡 首页头部
 *
 * @author Created by Zg on 2018/11/15.
 */

public class CardHomeHeaderEntity extends HttpResultBean {


    @SerializedName("indexbannerlist")
    public List<CommJumpBean> bannerList;
    @SerializedName("indexIconlist")
    public List<Icon> iconList;
    /**
     * indexadvlist : {"title":"adad","fu_title":"无","type":"54","img":"admin/images/43efc89670a5ca870e3b2bc6ea355d664cc2b30a_1.png","content":"3377","display":1}
     * card_one : {"card_one_title":{"title":"wearweq","fu_title":"无","type":"9","img":"admin/images/1f9dd77a24af32bc2da93b481acbcb2f76e9fd8a_1.png","content":"购物卡京东品牌精选","display":1},"card_one_left_up":{"title":"32r132","fu_title":"无","type":"9","img":"admin/images/fd4214f06ad92236e3f433772180474e92dc78a9_1.png","content":"购物卡京东品牌精选","display":1},"card_one_right_up":{"title":"erfqgewrf","fu_title":"无","type":"9","img":"admin/images/3da251b420a63c00e9128db42092e64174b06694_1.png","content":"购物卡京东品牌精选","display":1},"card_one_left_down":{"title":"ewqrewq","fu_title":"无","type":"9","img":"admin/images/51c733f25770f4d874b370709758ce857ebc5d68_1.png","content":"购物卡京东品牌精选","display":1},"card_one_middle_down":{"title":"ewrwe","fu_title":"无","type":"9","img":"admin/images/711cf5eff4480910f6bb048844dee85c6e289d2a_1.png","content":"购物卡京东品牌精选","display":1},"card_one_right_down":{"title":"ewqrewq","fu_title":"无","type":"9","img":"admin/images/9543e3f63aae3119f8c4cb8ea26ae8d80533ae4e_1.png","content":"购物卡京东品牌精选","display":1}}
     * card_two : {"card_two_title":{"title":"ewqrew","fu_title":"无","type":"9","img":"admin/images/236eaa3e594b0cd5bc6d34e343ef570251794bce_1.png","content":"购物卡京东品牌精选","display":1},"card_two_left_up":{"title":"rewqrewq","fu_title":"部分第三方","type":"9","img":"admin/images/c26d88562c46cedf7526329d2bb0f74a0b0541a8_1.gif","content":"购物卡京东品牌精选","display":1},"card_two_right_up":{"title":"去玩儿","fu_title":"去玩儿","type":"9","img":"admin/images/9b85c25882bd22e25a33def36277da0ddb77e536_1.png","content":"购物卡京东品牌精选","display":1},"card_two_left_down":{"title":"而我却热无群","fu_title":"而我却热无群","type":"9","img":"admin/images/8ece159e76e7a53fce84b859e7db6e8d013c65cd_1.png","content":"购物卡京东品牌精选","display":1},"card_two_right_down":{"title":"而我却绕","fu_title":"而我却绕","type":"9","img":"admin/images/415902cdc792937efeac4bbe068745b0c6da54ce_1.png","content":"购物卡京东品牌精选","display":1}}
     * card_three : {"card_three_title":{"title":"热武器二群","fu_title":"无","type":"9","img":"admin/images/c407b99a10b86ae99122061931c827cea22f8b40_1.png","content":"购物卡京东品牌精选","display":1},"card_three_middle_four":[{"title":"4444444444","fu_title":"","type":"9","img":"admin/images/9f0a536e40a32d18eec8696e520ecf7a7002a64c_1.png","content":"购物卡京东品牌精选","display":1}]}
     * middlebannerlist : [{"title":"热吻亲热","fu_title":"","type":"9","img":"admin/images/5006b408807b6a843374b4f266784e1fc6976a4e_1.png","content":"购物卡京东品牌精选","display":1}]
     * hotsalelist : [{"id":"34043","sku":"5835485","name":"联想昭阳E42-80 14英寸商用笔记本i7-6567U/4G/1T/2G/DVD刻录/Win7/带包鼠/一年全保上门z","weight":"2.95","imagePath":"jfs/t12364/352/1373551962/240054/5bbc4b90/5a1e60b6N307bddc5.jpg","state":"1","brandName":"联想（Lenovo）","productArea":"中国大陆","upc":"80T9CTO1WW","saleUnit":"台","one_category":"670","two_category":"671","three_category":"672","price":"5666.00","jdPrice":"5999.00","marketPrice":"6399.00","return_bean":"216.78","sale":"2147483647","rate":"3.61","all_return_bean":"333.00","is_set_price":"0","isFactoryShip":"0","is_del":"0","jd_price":"0.00","is_card":"1"},{"id":"34044","sku":"5863366","name":"宏碁 Acer TravelMate P249 14英寸商用笔记本 i5-7200U 4G 1T 940MX 2G独显 WIN10 三年保修 首年上门","weight":"3.01","imagePath":"jfs/t14752/251/500919046/228831/7f1e664f/5a2f803cN468cda32.jpg","state":"1","brandName":"宏碁（acer）","productArea":"中国大陆","upc":"191114572603;191114584491","saleUnit":"台","one_category":"670","two_category":"671","three_category":"672","price":"3699.00","jdPrice":"3899.00","marketPrice":"4899.00","return_bean":"130.19","sale":"2147483647","rate":"3.34","all_return_bean":"200.00","is_set_price":"0","isFactoryShip":"0","is_del":"0","jd_price":"0.00","is_card":"1"},{"id":"34045","sku":"5880416","name":"宏碁 Acer TravelMate P449 14英寸商用笔记本 i5-7200U 4G 1T 940MX 2G独显 WIN10专业版 三年保修 首年上门","weight":"2.74","imagePath":"jfs/t13639/13/2026512973/287679/3d36f0db/5a2f7facNdace716e.jpg","state":"1","brandName":"宏碁（acer）","productArea":"中国大陆","upc":"191114555309","saleUnit":"台","one_category":"670","two_category":"671","three_category":"672","price":"4899.00","jdPrice":"5099.00","marketPrice":"6399.00","return_bean":"130.19","sale":"2147483647","rate":"2.55","all_return_bean":"200.00","is_set_price":"0","isFactoryShip":"0","is_del":"0","jd_price":"0.00","is_card":"1"}]
     */

    @SerializedName("indexadvlist")
    public CommJumpBean indexadvlist;
    @SerializedName("card_one")
    public CardOneBean cardOne;
    @SerializedName("card_two")
    public CardTwoBean cardTwo;
    @SerializedName("card_three")
    public CardThreeBean cardThree;
    @SerializedName("middlebannerlist")
    public List<CommJumpBean> middlebannerlist;
    @SerializedName("hotsalelist")
    public List<HotsalelistBean> hotsalelist;


    public static class CommJumpBean {
        /**
         * title : banner1
         * fu_title :
         * type : 54
         * img : admin/images/01e9f79b236c505b51012870f074f995ab96b74d_1.png
         * content : 3470
         * display : 1
         * <p>
         * join_num : 参与人数（热销榜单中使用）
         */

        @SerializedName("title")
        public String title;
        @SerializedName("fu_title")
        public String fuTitle;
        @SerializedName("type")
        public int type;
        @SerializedName("img")
        public String img;
        @SerializedName("content")
        public String content;
        @SerializedName("display")
        public int display;
        //参与人数（热销榜单中使用）
        @SerializedName("join_num")
        public String joinNum;


    }

    public static class Icon {

        /**
         * title : fdgsdfsdf
         * fu_title :
         * type : 9
         * img : admin/images/5b781d757a5b1199692371ef826b1bdb823aa366_1.png
         * content : 购物卡京东品牌精选
         * display : 1
         */

        @SerializedName("title")
        public String title;
        @SerializedName("fu_title")
        public String fuTitle;
        @SerializedName("type")
        public int type;
        @SerializedName("img")
        public String img;
        @SerializedName("content")
        public String content;
        @SerializedName("display")
        public int display;
    }

    public static class CardOneBean {
        /**
         * card_one_title : {"title":"wearweq","fu_title":"无","type":"9","img":"admin/images/1f9dd77a24af32bc2da93b481acbcb2f76e9fd8a_1.png","content":"购物卡京东品牌精选","display":1}
         * card_one_left_up : {"title":"32r132","fu_title":"无","type":"9","img":"admin/images/fd4214f06ad92236e3f433772180474e92dc78a9_1.png","content":"购物卡京东品牌精选","display":1}
         * card_one_right_up : {"title":"erfqgewrf","fu_title":"无","type":"9","img":"admin/images/3da251b420a63c00e9128db42092e64174b06694_1.png","content":"购物卡京东品牌精选","display":1}
         * card_one_left_down : {"title":"ewqrewq","fu_title":"无","type":"9","img":"admin/images/51c733f25770f4d874b370709758ce857ebc5d68_1.png","content":"购物卡京东品牌精选","display":1}
         * card_one_middle_down : {"title":"ewrwe","fu_title":"无","type":"9","img":"admin/images/711cf5eff4480910f6bb048844dee85c6e289d2a_1.png","content":"购物卡京东品牌精选","display":1}
         * card_one_right_down : {"title":"ewqrewq","fu_title":"无","type":"9","img":"admin/images/9543e3f63aae3119f8c4cb8ea26ae8d80533ae4e_1.png","content":"购物卡京东品牌精选","display":1}
         */

        @SerializedName("card_one_title")
        public CommJumpBean cardOneTitle;
        @SerializedName("card_one_left_up")
        public CommJumpBean cardOneLeftUp;
        @SerializedName("card_one_right_up")
        public CommJumpBean cardOneRightUp;
        @SerializedName("card_one_left_down")
        public CommJumpBean cardOneLeftDown;
        @SerializedName("card_one_middle_down")
        public CommJumpBean cardOneMiddleDown;
        @SerializedName("card_one_right_down")
        public CommJumpBean cardOneRightDown;
    }

    public static class CardTwoBean {
        /**
         * card_two_title : {"title":"ewqrew","fu_title":"无","type":"9","img":"admin/images/236eaa3e594b0cd5bc6d34e343ef570251794bce_1.png","content":"购物卡京东品牌精选","display":1}
         * card_two_left_up : {"title":"rewqrewq","fu_title":"部分第三方","type":"9","img":"admin/images/c26d88562c46cedf7526329d2bb0f74a0b0541a8_1.gif","content":"购物卡京东品牌精选","display":1}
         * card_two_right_up : {"title":"去玩儿","fu_title":"去玩儿","type":"9","img":"admin/images/9b85c25882bd22e25a33def36277da0ddb77e536_1.png","content":"购物卡京东品牌精选","display":1}
         * card_two_left_down : {"title":"而我却热无群","fu_title":"而我却热无群","type":"9","img":"admin/images/8ece159e76e7a53fce84b859e7db6e8d013c65cd_1.png","content":"购物卡京东品牌精选","display":1}
         * card_two_right_down : {"title":"而我却绕","fu_title":"而我却绕","type":"9","img":"admin/images/415902cdc792937efeac4bbe068745b0c6da54ce_1.png","content":"购物卡京东品牌精选","display":1}
         */

        @SerializedName("card_two_title")
        public CommJumpBean cardTwoTitle;
        @SerializedName("card_two_left_up")
        public CommJumpBean cardTwoLeftUp;
        @SerializedName("card_two_right_up")
        public CommJumpBean cardTwoRightUp;
        @SerializedName("card_two_left_down")
        public CommJumpBean cardTwoLeftDown;
        @SerializedName("card_two_right_down")
        public CommJumpBean cardTwoRightDown;
    }

    public static class CardThreeBean {
        /**
         * card_three_title : {"title":"热武器二群","fu_title":"无","type":"9","img":"admin/images/c407b99a10b86ae99122061931c827cea22f8b40_1.png","content":"购物卡京东品牌精选","display":1}
         * card_three_middle_four : [{"title":"4444444444","fu_title":"","type":"9","img":"admin/images/9f0a536e40a32d18eec8696e520ecf7a7002a64c_1.png","content":"购物卡京东品牌精选","display":1}]
         */

        @SerializedName("card_three_title")
        public CommJumpBean cardThreeTitle;
        @SerializedName("card_three_middle_four")
        public List<CommJumpBean> cardThreeMiddleFour;
    }

    public static class HotsalelistBean {
        /**
         * id : 34043
         * sku : 5835485
         * name : 联想昭阳E42-80 14英寸商用笔记本i7-6567U/4G/1T/2G/DVD刻录/Win7/带包鼠/一年全保上门z
         * weight : 2.95
         * imagePath : jfs/t12364/352/1373551962/240054/5bbc4b90/5a1e60b6N307bddc5.jpg
         * state : 1
         * brandName : 联想（Lenovo）
         * productArea : 中国大陆
         * upc : 80T9CTO1WW
         * saleUnit : 台
         * one_category : 670
         * two_category : 671
         * three_category : 672
         * price : 5666.00
         * jdPrice : 5999.00
         * marketPrice : 6399.00
         * return_bean : 216.78
         * sale : 2147483647
         * rate : 3.61
         * all_return_bean : 333.00
         * is_set_price : 0
         * isFactoryShip : 0
         * is_del : 0
         * jd_price : 0.00
         * is_card : 1
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
        @SerializedName("isFactoryShip")
        public String isFactoryShip;
        @SerializedName("is_del")
        public String isDel;
        //        @SerializedName("jd_price")
//        public String jd_price;
        @SerializedName("is_card")
        public String isCard;
    }
}
