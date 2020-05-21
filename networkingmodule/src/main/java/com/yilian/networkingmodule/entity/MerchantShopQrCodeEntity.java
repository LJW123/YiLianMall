package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by  on 2017/6/15 0015.
 */

public class MerchantShopQrCodeEntity extends HttpResultBean {
    /**
     * qrCodeContent : https://yilian.lefenmall.net/wechat/m/pay/marketOrderPay.php?order_index=13
     * merchant_name : 姐弟俩
     * merchant_img : app/images/mall/20170822/6325068780687319_8343792_1503367102763environment.jpg
     * merchant_id : 567
     * payment_fee : 68000
     * filename : app/images/qrcode/20170830/97df0c5530b8df06171cad020465f4d81.png
     */

    @SerializedName("qrCodeContent")
    public String qrCodeContent;
    @SerializedName("merchant_name")
    public String merchantName;
    @SerializedName("merchant_img")
    public String merchantImg;
    @SerializedName("merchant_id")
    public String merchantId;
    @SerializedName("payment_fee")
    public String paymentFee;
    @SerializedName("filename")
    public String filename;
}
