package com.yilianmall.merchant.utils;

import android.content.Context;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yilian.networkingmodule.entity.WeiXinOrderEntity;

/**
 * Created by  on 2017/7/14 0014.
 */

public class PullWXPayUtil {
    public static void pullWxPay(Context context, WeiXinOrderEntity body) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, body.appId);
        wxapi.registerApp(body.appId);
        PayReq payReq = new PayReq();
        payReq.appId = body.appId;
        payReq.partnerId = body.partnerId;
        payReq.prepayId = body.prepayId;
        payReq.nonceStr = body.nonceStr;
        payReq.timeStamp = body.timeStamp;
        payReq.packageValue = body.packageValue;
        payReq.sign = body.sign;
        payReq.extData = body.sign;
        payReq.extData = "app data";
        wxapi.sendReq(payReq);
    }
}
