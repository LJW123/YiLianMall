package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/5/22.
 */

public class JDOrderListEntity extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;
    /**
     * 系统时间
     */
    @SerializedName("systemTime")
    public long systemTime;
    /**
     * 等待付款时长
     */
    @SerializedName("time")
    public long time;

    public static class DataBean {
        /**
         * //0普通京东商品  1购物卡京东商品
         */
        @SerializedName("jd_type")
        public int jdType;
        /**
         * type:0只显示再次购买按钮 原则上不存在 ,1待付款 ,2 待收货, 3已完成, 4已取消
         * order_id : 2018051917223157793
         * jdOrderId : 74040715959
         * orderJdPrice : 37.90
         * state : 1
         * orderState : 1
         * submitState : 1
         * jdOrderState : 19
//         * skuId : 1184648
//         * skuName : 南街村 北京方便面 麻辣味 65g*40袋
//         * skuPic : jfs/t1936/297/1193260445/1385514/6e4d472/564d9594N9678825f.jpg
//         * skuNum : 1
         * orderTime:0 下单时间戳
         */
        @SerializedName("order_quan_price")
        public String orderDaiGouQuanMoney;
        @SerializedName("id")
        public String orderIndex;
        /**
         * 益联订单状态 type:0只显示再次购买按钮 原则上不存在 ,1待付款 ,2 待收货, 3已完成, 4已取消
         */
        @SerializedName("type")
        public int type;
        /**
         * 益联订单
         */
        @SerializedName("order_id")
        public String orderId;
        /**
         * 京东订单
         */
        @SerializedName("jdOrderId")
        public String jdOrderId;
        /**
         *   订单金额
         */
        @SerializedName("orderJdPrice")
        public float orderJdPrice;
        /**
         * 订单运费
         */
        @SerializedName("freight")
        public float freight;
        /**
         * 物流状态  0是新建 1是妥投 2是拒收
         */
        @SerializedName("state")
        public int state;
        /**
         * 订单状态 0 为取消订单 1 为有效
         */
        @SerializedName("orderState")
        public int orderState;
        /**
         * 0为未确认下单订单 1为确认下单订单
         */
        @SerializedName("submitState")
        public int submitState;
        /**
         * 京东状态，1.新单 2.等待支付 3.等待支付确认 4.延迟付款确认 5.订单暂停 6.店长最终审核
         * 7.等待打印 8.等待出库 9.等待打包 10.等待发货 11.自提途中 12.上门提货 13.自提退货
         * 14.确认自提 16.等待确认收货 17.配送退货 18.货到付款确认 19.已完成
         * 21.收款确认 22.锁定 29.等待三方出库 30.等待三方发货 31.等待三方发货完成
         */
        @SerializedName("jdOrderState")
        public String jdOrderState;
        /**
         *  结算状态 0未结算  1已结算
         */
        @SerializedName("settleStatus")
        public int settleStatus;
        /**
         * 0 不可申请结算 1 可申请结算
         */
        @SerializedName("applySettle")
        public int applySettle;
//        /**
//         * 商品sku
//         */
//        @SerializedName("skuId")
//        public String skuId;
//        /**
//         * 商品名称
//         */
//        @SerializedName("skuName")
//        public String skuName;
//        /**
//         * 商品图片
//         */
//        @SerializedName("skuPic")
//        public String skuPic;
//        /**
//         * 商品数量
//         */
//        @SerializedName("skuNum")
//        public String skuNum;

        /**
         * 下单时间戳
         */
        @SerializedName("orderTime")
        public long orderTime;
        /**
         * 订单下商品
         */
        @SerializedName("goods_list")
        public List<GoodsList> goodsist;

    }


    public  static class GoodsList{

        /**
         * skuId : 商品id
         * skuName : 商品名称
         * skuPic : 商品图片
         * skuNum : 商品数量
         * skuPrice : 商品价格
         * skuJdPrice : 商品指导价
         * return_bean : 赠送的算力
         */

        @SerializedName("skuId")
        private String skuId;
        @SerializedName("skuName")
        private String skuName;
        @SerializedName("skuPic")
        private String skuPic;
        @SerializedName("skuNum")
        private int skuNum;
        @SerializedName("skuPrice")
        private String skuPrice;
        @SerializedName("skuJdPrice")
        private String skuJdPrice;
        @SerializedName("return_bean")
        private String returnBean;

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getSkuPic() {
            return skuPic;
        }

        public void setSkuPic(String skuPic) {
            this.skuPic = skuPic;
        }

        public int getSkuNum() {
            return skuNum;
        }

        public void setSkuNum(int skuNum) {
            this.skuNum = skuNum;
        }

        public String getSkuPrice() {
            return skuPrice;
        }

        public void setSkuPrice(String skuPrice) {
            this.skuPrice = skuPrice;
        }

        public String getSkuJdPrice() {
            return skuJdPrice;
        }

        public void setSkuJdPrice(String skuJdPrice) {
            this.skuJdPrice = skuJdPrice;
        }

        public String getReturnBean() {
            return returnBean;
        }

        public void setReturnBean(String returnBean) {
            this.returnBean = returnBean;
        }
    }
}
